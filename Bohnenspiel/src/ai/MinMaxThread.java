package ai;

import bohnenspiel.BohnenspielState;

public class MinMaxThread extends Thread{

	public static int MIN = Integer.MIN_VALUE;
	public static int MAX = Integer.MAX_VALUE;
	private BohnenspielState state;
	private int value;
	
	public MinMaxThread(BohnenspielState state) {
		this.state = state;
	}
	
	
	@Override
	public void run() {
		this.value = minimax(state,14,MIN,MAX,false);
		System.out.println(this.value+" ");
	} 
	
	/**
	 * Recruisive Minimax algorithm. 
	 * @param bss the state that needs to be evaluated and expanded if the current node is not a terminal one
	 * @param depth the depth that the tree should reach before it terminates and finds the best result
	 * @param alpha alpha is the worst play for the maximizing player
	 * @param beta is the worst play for the minimizing player
	 * @param isMaximizingPlayer
	 * @return the value of the play
	 */
	private int minimax(BohnenspielState bss, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
		if (depth == 0 || bss.isTerminalNode()) {	
			return bss.calculateHeuristivValue();
		} else if(System.currentTimeMillis() - MinMaxAI.timer > 2800) {
			System.out.println(System.currentTimeMillis() - MinMaxAI.timer);
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
