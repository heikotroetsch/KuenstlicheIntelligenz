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
			index = rand.nextInt(6)+1;
			state.setAiTurn(true, true);
			initialized = true;
			state = new BohnenspielState(state, index);
		}
		// enemy acted and i have to react	---   1-6
		else if (enemyIndex > 0 && enemyIndex <= 6) {
			if (!initialized) {
				state.setAiTurn(false, false);
				initialized = true;				
			}
			index = rand.nextInt(6) + 7;
			state = new BohnenspielState(state, index);
		//enemy acted and i have to react   ---   7-12	
		} else if (enemyIndex > 6 && enemyIndex <= 12) {
			if (!initialized) {
				state.setAiTurn(false, true);
				initialized = true;				
			}
			index = rand.nextInt(6) + 1;
			state = new BohnenspielState(state, index);
		}
		return index;
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
		int bestMove = -1;

		// Traverse all possible moves
		for (BohnenspielState bs : bss.expand()) {
			int moveVal = minimax(bss, 10, bs.getAITurn());
			if (moveVal > bestVal) {
				bestMove = bs.getLastMove();
				bestVal = moveVal;
			}
		}
		System.out.println("Value of the best Move is: " + bestVal);
		return bestMove;
	}

	private boolean isTerminalState(BohnenspielState bss) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		return "MinMax AI";
	}
}
