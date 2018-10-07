package ai;

import bohnenspiel.BohnenspielState;

public class MinMaxThread extends Thread{

	public static int MIN = -1000;
	public static int MAX = 1000;
	private BohnenspielState state;
	private int value;
	
	public MinMaxThread(BohnenspielState state) {
		this.state = state;
		MinMaxAI.threadFinishCounter--;
	}
	
	
	@Override
	public void run() {
		this.value = minimax(state,20,MIN,MAX,true);
		System.out.println(this.value+" ");
		MinMaxAI.threadFinishCounter++;
	}
	
	private int minimax(BohnenspielState bss, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
		System.out.println("time left "+ (System.currentTimeMillis() - MinMaxAI.timer));
		if (depth == 0 || bss.expand().isEmpty() || System.currentTimeMillis() - MinMaxAI.timer > 2900) {
			return bss.calculateHeuristivValue();
		}
		if (isMaximizingPlayer) {
			int value = MIN;
			// Traverse all possible moves
			for (BohnenspielState bs : bss.expand()) {
				value = Math.max(value, minimax(bs, depth - 1, alpha, beta, !isMaximizingPlayer));
				alpha = Math.max(alpha, value);
				if (alpha >= beta) {
					break;
				}
			}
			return value;
		} else {
			int value = MAX;
			// Traverse all possible moves
			for (BohnenspielState bs : bss.expand()) {
				value = Math.min(value, minimax(bs, depth - 1, alpha, beta, !isMaximizingPlayer));
				beta = Math.min(beta, value);
				if (alpha >= beta) {
					break;
				}
			}
			return value;
		}
	}
	
	public int getValue() {
		return value;
	}
	
	public BohnenspielState getBState() {
		return state;
	}

}
