package ai;

import java.util.ArrayList;
import java.util.List;

import game.Move;
import monteCarlo.State;

public class MCTSAI extends AI {
	private State status;
	private ArrayList<Integer> hand;
	public static boolean initialized = false;
	private int playerNumber;

	public MCTSAI() {

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Bohnigma";
	}

	@Override
	public void initialize(List<String> hand, int mid) {
		ArrayList<Integer> handAI = new ArrayList<Integer>();
		hand.forEach(c -> handAI.add(Integer.parseInt(c)));
		this.status = new State(mid, handAI, null);

		this.hand = new ArrayList<Integer>();
		hand.forEach(c -> this.hand.add(Integer.parseInt(c)));
	}

	@Override
	public Move action(Move move) {
		if (!initialized) {
			if (move != null) {
				status.performMove(move);
				initialized = true;
				playerNumber = 2;
			} else {
				playerNumber = 1;
			}
			if (hand.isEmpty()) {
				return null;
			}
		} else {
			// DO MCTS
			//Enemy Move Input
			status.performMove(move);
			// DO MCTS
			
		}
		return null;

	}

}
