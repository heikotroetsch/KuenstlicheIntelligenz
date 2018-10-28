package monteCarlo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ai.MCTSAI;
import game.Move;
import tree.Node;
import tree.Tree;

public class MonteCarloTreeSearch {

	private int level;
	private int opponent;
	private static final int WIN_SCORE = 10;

	public Move findNextMove(State state) {
		long start = System.currentTimeMillis();
		long end = start + 10000;
		ArrayList<Node> winnerNodes = new ArrayList<>();
		opponent = 3 - MCTSAI.playerNumber;
		System.out.println(opponent);

		ArrayList<Tree> trees = new ArrayList<Tree>();
		for (int i = 0; i < 100; i++) {
			ArrayList<Integer> simHand = simulatePossibleHands(state);
			State s1 = new State(state);
			s1.setPlayerHand2(simHand);
			Tree tree = new Tree(s1);
			trees.add(tree);
		}

		for (Tree tree : trees) {
			Node rootNode = tree.getRoot();
			// rootNode.getState().setPlayerNo(opponent);
			int counter = 0;
			while (counter++ < 500) {
				System.out.println("Counter: " + counter);
				// PHASE 1 - Selection
				System.out.println("1 Select");
				Node promisingNode = selectPromisingNode(rootNode);

				// Phase 2 - Expansion
				System.out.println("2 Expand");
				if (promisingNode.getState().checkStatus() == State.IN_PROGRESS) {
					expandNode(promisingNode);
				}

				// Phase 3 - Simulation
				System.out.println("3 Simulate");
				Node nodeToExplore = promisingNode;
				if (promisingNode.getChildArray().size() > 0) {
					nodeToExplore = promisingNode.getRandomChildNode();
				}
				int playoutResult = simulateRandomPlayout(nodeToExplore);
				System.out.println("player " + playoutResult+" won");

				// Phase 4 - Update
				System.out.println("4 Update");
				backPropagation(nodeToExplore, playoutResult);
			}
			Node winnerNode = rootNode.getChildWithMaxScore();
			winnerNodes.add(winnerNode);
			tree.setRoot(winnerNode);
		}
		//Aggregate Node scores??
		ArrayList<Node> aggregatedNodes = new ArrayList<Node>();
		for(Node n : winnerNodes) {
			if(!(aggregatedNodes.contains(n))) {
				aggregatedNodes.add(n);
			} else {
				aggregatedNodes.get(aggregatedNodes.indexOf(n)).getState().addScore(n.getState().getWinScore());
			}
		}
		Node winnerNode = Collections.max(aggregatedNodes, Comparator.comparing(c -> {
			return c.getState().getWinScore();
		}));
		System.out.println("TIME "+(System.currentTimeMillis()-start));
		return winnerNode.getState().getLastTurn();
		
	}

	private ArrayList<Integer> simulatePossibleHands(State state) {
		int handSize = 12 - state.getCardsPlayed()[state.getPlayerNo()];
		ArrayList<Integer> leftCardsCopy = new ArrayList<>();
		MCTSAI.leftCards.forEach(card -> leftCardsCopy.add(card));
		// int leftCards = all Cards - mid - myHand - playedCardsEnemy
		ArrayList<Integer> hand = new ArrayList<Integer>();
		while (hand.size() != handSize) {
			int selectRandom = (int) (Math.random() * leftCardsCopy.size());
			hand.add(leftCardsCopy.remove(selectRandom));
		}
		return hand;
	}

	private Node selectPromisingNode(Node rootNode) {
		Node node = rootNode;
		while (node.getChildArray().size() != 0) {
			node = UCT.findBestNodeWithUCT(node);
		}
		return node;
	}

	private void expandNode(Node node) {
		List<State> possibleStates = node.getState().getAllPossibleStates();
		possibleStates.forEach(state -> {
			Node newNode = new Node(state);
			newNode.setParent(node);
			node.getChildArray().add(newNode);
		});
	}

	// Propagate back with the win Score of the player
	private void backPropagation(Node nodeToExplore, int playerNr) {
		Node tempNode = nodeToExplore;
		while (tempNode != null) {
			tempNode.getState().incrementVisit();
			System.out.println("playerNumber"+tempNode.getState().getPlayerNo());
			if (tempNode.getState().getPlayerNo() == playerNr) {
				tempNode.getState().addScore(WIN_SCORE);
				System.out.println("Match");
			}
			tempNode = tempNode.getParent();
		}

	}

	private int simulateRandomPlayout(Node node) {
		Node tempNode = new Node(node);
		State tempState = tempNode.getState();
		int status = tempState.checkStatus();

		if (status == opponent) {
			tempNode.getParent().getState().setWinScore(Integer.MIN_VALUE);
			return status;
		}

		while (status == State.IN_PROGRESS) {
			tempState.togglePlayer();
			tempState.randomPlay();

			status = tempState.checkStatus();
		}

		return status;
	}

	public static void main(String[] args) {
		ArrayList<Integer> h1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5, 5, 5));
		ArrayList<Integer> h2 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5, 5, 5));
		State s1 = new State(4);
		s1.setPlayerHand1(h1);
		s1.setPlayerHand2(h2);

		MonteCarloTreeSearch mct = new MonteCarloTreeSearch();
		System.out.println(mct.findNextMove(s1));
	}

}
