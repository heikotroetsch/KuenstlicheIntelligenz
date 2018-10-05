package ai;

import java.util.Random;

import bohnenspiel.BohnenspielState;

public class MinMaxAI extends AI {

	BohnenspielState state;
	boolean initialized = false;
	Random rand = new Random();

	public MinMaxAI() {
		state = new BohnenspielState();
	}

	public int getMove(int enemyIndex) {
		int index = 0;
		// have to choose the first move
		if (enemyIndex == -1) {
			index = findBestMove(state);
			state.setAiTurn(true, true);
			initialized = true;
			state = new BohnenspielState(state, index);
		}
		// enemy acted and i have to react	---   1-6
		else if (enemyIndex > 0 && enemyIndex <= 6) {
			state = new BohnenspielState(state, --enemyIndex);
			if (!initialized) {
				state.setAiTurn(true, false);
				initialized = true;				
			}
			index = findBestMove(state);
			state = new BohnenspielState(state, index);
		//enemy acted and i have to react   ---   7-12	
		} else if (enemyIndex > 6 && enemyIndex <= 12) {
			state = new BohnenspielState(state, --enemyIndex);
			if (!initialized) {
				state.setAiTurn(true, true);
				initialized = true;				
			}
			index = findBestMove(state);
			state = new BohnenspielState(state, index);
		}
		System.out.println(state);
		return index+1;
	}

	private int minimax(BohnenspielState bss, int depth, boolean isMaximizingPlayer) {
		if (depth == 0) {
			return bss.calculateHeuristivValue();
		}
		if (isMaximizingPlayer) {
			int best = -1000;
			// Traverse all possible moves
			for (BohnenspielState bs : bss.expand()) {
				best = Math.max(best, minimax(bs, depth - 1, !isMaximizingPlayer));
			}
			return best;
		} else {
			int best = 1000;
			// Traverse all possible moves
			for (BohnenspielState bs : bss.expand()) {
				best = Math.min(best, minimax(bs, depth - 1, !isMaximizingPlayer));
			}
			return best;
		}
	}

	private int findBestMove(BohnenspielState bss) {
		int bestVal = -1000;
		int bestMove = 0;

		// Traverse all possible moves
		for (BohnenspielState bs : bss.expand()) {
			int moveVal = minimax(bss, 10, true);
//			System.out.println("moveVal: "+moveVal);
			if (moveVal > bestVal) {
				bestMove = bs.getLastMove();
				bestVal = moveVal;
			}
		}
	System.out.println("Value of the best Move is: " + bestVal);
		return bestMove;
	}


	@Override
	public String getName() {
		return "MinMax AI";
	}
}
