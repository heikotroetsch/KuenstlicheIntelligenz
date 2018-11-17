package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Move;
import monteCarlo.MonteCarloTreeSearch;
import monteCarlo.State;

public class MCTSAI extends AI {
	private State status;
	private ArrayList<Integer> hand;
	public static boolean initialized = false;
	public static int playerNumber = 1;
	MonteCarloTreeSearch mcts;
	public static ArrayList<Integer> leftCards = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2,
			2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5));

	public MCTSAI() {
		mcts = new MonteCarloTreeSearch();

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "test";
	}

	@Override
	public void initialize(List<String> hand, int mid) {
		System.out.println("init");
		ArrayList<Integer> handAI = new ArrayList<Integer>();
		hand.forEach(c -> handAI.add(Integer.parseInt(c)));
		this.status = new State(mid);
		leftCards.remove(new Integer(mid));
		this.hand = new ArrayList<Integer>();
		hand.forEach(c -> {
			this.hand.add(Integer.parseInt(c));
			MCTSAI.leftCards.remove(new Integer(Integer.parseInt(c)));
		});
	}

	@Override
	public Move action(Move move) {
		Move mv = new Move(0, 0, 0);
		if (move != null) {
			if (!initialized) {
				initialized = true;
				playerNumber = 2;
				this.status.setPlayerHand2(hand);
			}
			status.performMove(move);
			leftCards.remove(new Integer(move.getV()));
			// remove PLayers Card
			status.togglePlayer();
		} else {
			if (!initialized) {
				initialized = true;
				playerNumber = 1;
				this.status.setPlayerHand1(hand);
			}
		}
		mv = mcts.findNextMove(status);
		System.out.println("mcts finished");
		status.performMove(mv);
		System.out.println("move performed");
		status.removeHandfromMyHand(mv);
		System.out.println("Card removed");
		status.togglePlayer();
		System.out.println(mv);
		return mv;

	}

}
