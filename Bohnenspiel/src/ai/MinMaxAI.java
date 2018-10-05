package ai;

import java.util.Random;

import bohnenspiel.BohnenspielState;

public class MinMaxAI extends AI {

	Random rand = new Random();
	BohnenspielState spiel = new BohnenspielState(true);

	public int getMove(int enemyIndex) {
		int index = 0;
		// have to choose the first move
		if (enemyIndex == -1) {
			index = rand.nextInt(6) + 1;
		}
		// enemy acted and i have to react
		else if (enemyIndex > 0 && enemyIndex <= 6) {
			spiel.makeMove(enemyIndex, false);
			index = rand.nextInt(6) + 7;
		} else if (enemyIndex > 6 && enemyIndex <= 12) {
			spiel.makeMove(enemyIndex, false);
			index = rand.nextInt(6) + 1;
		}

		spiel.makeMove(index, true);
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
				best = Math.max(best, minimax(bs, depth + 1, !isMaximizingPlayer));
			}
			return best;
		} else {
			int best = 1000;

			// Traverse all possible moves
			for (BohnenspielState bs : bss.expand()) {
				best = Math.min(best, minimax(bs, depth + 1, !isMaximizingPlayer));
			}
			return best;
		}

	}
	
	private int findBestMove(BohnenspielState bss) {
		int bestVal = -1000;
		int bestMove = -1;
		
		//Traverse all possible moves
		for(BohnenspielState bs : bss.expand()) {
			int moveVal = minimax(bss, 0, false);
			if(moveVal > bestVal) {
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
		return "Concrete AI";
	}
}
