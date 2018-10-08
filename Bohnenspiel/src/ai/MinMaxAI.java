package ai;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import bohnenspiel.BohnenspielState;

public class MinMaxAI extends AI {

	BohnenspielState state;
	boolean initialized = false;
	Random rand = new Random();

	public static long timer = 0;
	
	
	public MinMaxAI() {
		state = new BohnenspielState();
	}

	public int getMove(int enemyIndex) {
		timer = System.currentTimeMillis();
		int index = 0;
		// have to choose the first move
		if (enemyIndex == -1) {
			index = findBestMove();
			state.setAiTurn(true, true);
			initialized = true;
			state = new BohnenspielState(state, index);
		}
		// enemy acted and i have to react --- 1-6
		else if (enemyIndex > 0 && enemyIndex <= 6) {
			state = new BohnenspielState(state, --enemyIndex);
			if (!initialized) {
				state.setAiTurn(true, false);
				initialized = true;
			}
			index = findBestMove();
			state = new BohnenspielState(state, index);
			// enemy acted and i have to react --- 7-12
		} else if (enemyIndex > 6 && enemyIndex <= 12) {
			state = new BohnenspielState(state, --enemyIndex);
			if (!initialized) {
				state.setAiTurn(true, true);
				initialized = true;
			}
			index = findBestMove();
			state = new BohnenspielState(state, index);
		}
		System.out.println(state);
		return index + 1;
	}

	/**
	 * Finds the best move by using a minimax algorithm. Expands the current node and makes a minimax tree for each possible play in a thread.
	 * @return the best move by int. 
	 */
	private int findBestMove() {
		List<BohnenspielState> bsList = state.expand();
		
		PriorityQueue<MinMaxThread> threadPQueue = new PriorityQueue<MinMaxThread>(new MinMaxThreadComparator());
		MinMaxThread[] threadArray = new MinMaxThread[bsList.size()];
		
		for(int i = 0; i < bsList.size(); i++) {
			MinMaxThread temp = new MinMaxThread(bsList.get(i));
			temp.start();
			threadArray[i] = temp;
		} 

		for(Thread t : threadArray) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
		threadPQueue.addAll(Arrays.asList(threadArray));
		
		MinMaxThread temp = threadPQueue.poll();
		System.out.println("The best move has the Value: "+ temp.getValue());
		return temp.getBState().getLastMove();
	}
	
	@Override
	public String getName() {
		return "Bohnigma";
	}

	
}
