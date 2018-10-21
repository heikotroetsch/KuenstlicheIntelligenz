package monteCarlo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.javafx.geom.transform.GeneralTransform3D;

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
	//	opponent = 3 - playerNr;
		
		
		
		//SIMULATE A POSSIBLE HAND
		
		
		
		Tree tree = new Tree(state);
		Node rootNode = tree.getRoot();
		rootNode.setState(state);
		//rootNode.getState().setPlayerNo(opponent);
		int counter = 0;
		while (counter++  < 1000) {
			System.out.println("Counter: " +counter);
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

			// Phase 4 - Update
			System.out.println("4 Update");
			backPropagation(nodeToExplore, playoutResult);
		}
		
		Node winnerNode = rootNode.getChildWithMaxScore();
		tree.setRoot(winnerNode);
		return winnerNode.getState().getLastTurn();
	}
	
	private ArrayList<Integer> simulatePossibleHands(State state){
		
		
		return null;
	}
	
	private Node selectPromisingNode(Node rootNode) {
		Node node = rootNode; 
		while(node.getChildArray().size()!=0) {
			node = UCT.findBestNodeWithUCT(node);
		}
		return node;
	}
	
	private void expandNode(Node node) {
		List<State> possibleStates = node.getState().getAllPossibleStates();
		possibleStates.forEach(state -> {
			//TODO CHECK
			Node newNode = new Node(state);
			newNode.setParent(node);
			//TODO Change Opponent
			//newNode.getState().setPlayerNo(node.getState().)
			node.getChildArray().add(newNode);
		});
	}
	//Propagate back with the win Score of the player
	private void backPropagation(Node nodeToExplore, int playerNr) {
		Node tempNode = nodeToExplore;
		while(tempNode != null){
			System.out.println("bp");
			tempNode.getState().incrementVisit();
			if(tempNode.getState().getPlayerNo() == playerNr) {
				tempNode.getState().addScore(WIN_SCORE);
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
		State s1 = new State(4, h1, h2);
		
		MonteCarloTreeSearch mct = new MonteCarloTreeSearch();
	System.out.println(mct.findNextMove(s1));
	}

}
