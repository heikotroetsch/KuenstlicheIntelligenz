package ai;

import java.util.ArrayList;
import java.util.List;

import game.Move;

public class MCTSAI extends AI{
	private int[][] board;
	private ArrayList<Integer> hand;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Bohnigma";
	}

	@Override
	public void initialize(List<String> hand, int mid) {
		this.board = new int[5][5];
		for(int i = 0; i<5; i++) {
			for(int j = 0; j<5; j++) {
				this.board[i][j] = -1;
			}
		}
		this.board[2][2] = mid;
		this.hand = new ArrayList<Integer>();
		for(String c : hand) {
			this.hand.add(Integer.parseInt(c));
		}		
	}

	@Override
	public Move action(Move move) {

		if (move != null) {
			board[move.getX()][move.getY()] = move.getV();
		}

		if(hand.isEmpty()) {
			return null;
		}
		
		// attention, it's gonna be dirty!
		int x, y;
		do {
			x = (int) (Math.random() * 5);
			y = (int) (Math.random() * 5);
		} while(!validPosition(x,y));

		int value = hand.remove((int) (Math.random() * hand.size()));
		board[x][y] = value;
		return new Move(x, y, value);
	}
	
	private boolean validPosition(int x, int y) {
		if(board[x][y]>=0) {
			return false;
		}
		for (int i = -1; i < 2; i += 2) {
			if (x + i < 5 && x + i >= 0 && board[x + i][y] >= 0) {
				return true;
			} else if (y + i < 5 && y + i >= 0 && board[x][y + i] >= 0) {
				return true;
			}
		}
		return false;
	}
}
