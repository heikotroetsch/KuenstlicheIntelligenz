package monteCarlo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Move;

public class State {
	public int level = 0;
	private int[][] board;
	private int playerNo = 1;
	private int visitCount;
	private double winScore;
	private int[] playedCards = { 6, 6, 6, 6, 6, 6 };
	private ArrayList<ArrayList<Integer>> playerHands = new ArrayList<ArrayList<Integer>>();
	private Move lastTurn;

	public static final int IN_PROGRESS = -1;
	public static final int DRAW = 0;
	public static final int WINP1 = 1;
	public static final int WINP2 = 2;

	public State(int mid, ArrayList<Integer> player1card, ArrayList<Integer> player2card) {
		board = new int[5][5];
		for (int[] row : board) {
			Arrays.fill(row, -1);
		}
		board[2][2] = mid;
		playerHands.add(player1card);
		playerHands.add(player2card);

	}

	public State(State state) {
		this.board = new int[5][5];
		for(int i = 0; i < state.board.length; i++) {
			this.board[i] = Arrays.copyOf(state.board[i], state.board[i].length);
		}
		this.visitCount = state.getVisitCount();
		this.winScore = state.getWinScore();
		ArrayList<Integer> player1Cards = new ArrayList<Integer>();
		ArrayList<Integer> player2Cards = new ArrayList<Integer>();
		for(int card : state.playerHands.get(0)) {
			player1Cards.add(card);
		}
		for(int card : state.playerHands.get(1)) {
			player2Cards.add(card);
		}
		this.playerHands.add(player1Cards);
		this.playerHands.add(player2Cards);
		
	}
	
	//dirty fix
	private int getCurrentPlayer() {
		return playerHands.get(1).size() > playerHands.get(0).size() ? 1:0;
	}

	public int[] getPlayedCards() {
		return playedCards;
	}

	public void setPlayedCards(int[] playedCards) {
		this.playedCards = playedCards;
	}

	public ArrayList<ArrayList<Integer>> getPlayerHands() {
		return playerHands;
	}

	public void setPlayerHands(ArrayList<ArrayList<Integer>> playerHands) {
		this.playerHands = playerHands;
	}

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public int getPlayerNo() {
		return playerNo;
	}

	public void setPlayerNo(int playerNo) {
		this.playerNo = playerNo;
	}

	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	public double getWinScore() {
		return winScore;
	}

	public void setWinScore(double winScore) {
		this.winScore = winScore;
	}

	public List<int[]> getAllPossiblePositions() {
		System.out.println("posspos called");
		List<int[]> possibleMoves = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (this.validPosition(i, j)) {
					possibleMoves.add(new int[] { i, j });
				}
			}
		}
		System.out.println(this);
		return possibleMoves;
	}

	public List<State> getAllPossibleStates() {
		List<State> possibleStates = new ArrayList<>();

		// simulate Enemys hand
		for (int card : playerHands.get(getCurrentPlayer())) {
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					if (this.validPosition(i, j)) {
						State newState = new State(this);
						newState.performMove(new Move(i, j, card));
						newState.getPlayerHands().get(getCurrentPlayer()).remove(new Integer(card));
						newState.togglePlayer();
						possibleStates.add(newState);
					}
				}
			}
		}
		return possibleStates;
	}

	private boolean validPosition(int x, int y) {
		if (board[x][y] >= 0) {
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

	public void performMove(Move mv) {
		this.board[mv.getX()][mv.getY()] = mv.getV();
		this.setLastTurn(mv);
		level++;
	}

	public Move getLastTurn() {
		return lastTurn;
	}

	public void setLastTurn(Move lastTurn) {
		this.lastTurn = lastTurn;
	}

	void incrementVisit() {
		this.visitCount++;
	}

	void randomPlay() {
		// get a random position on the board
		List<int[]> availablePositions = getAllPossiblePositions();
		int totalPossibilities = availablePositions.size();
		int selectRandom = (int) (Math.random() * totalPossibilities);
		int[] movePos = availablePositions.get(selectRandom);
		// get a random card from the hand of the player whose turn it is
		totalPossibilities = playerHands.get(getCurrentPlayer()).size();
		selectRandom = (int) (Math.random() * totalPossibilities);
		System.out.println("flag");
		Move move = new Move(movePos[0], movePos[1], playerHands.get(getCurrentPlayer()).remove(selectRandom));
		this.performMove(move);
	}

	void togglePlayer() {
		this.playerNo = 3 - this.playerNo;
	}

	public int checkStatus() {
		if (!(playerHands.get(0).isEmpty() && playerHands.get(1).isEmpty())) {
			return IN_PROGRESS;
		}
		int rowScoreLow = Integer.MAX_VALUE;
		int lineScoreLow = Integer.MAX_VALUE;
		for (int i = 0; i < 5; i++) {
			int rowScoreCurr = calculateLineScore(
					new int[] { board[i][0], board[i][1], board[i][2], board[i][3], board[i][4] });
			if (rowScoreCurr < rowScoreLow) {
				rowScoreLow = rowScoreCurr;
			}
		}
		for (int i = 0; i < 5; i++) {
			int lineScoreCurr = calculateLineScore(
					new int[] { board[0][i], board[1][i], board[2][i], board[3][i], board[4][i] });
			if (lineScoreCurr < lineScoreLow) {
				lineScoreLow = lineScoreCurr;
			}
		}
		System.out.println("P1: " + rowScoreLow + "| P2: " + lineScoreLow);
		return rowScoreLow == lineScoreLow ? DRAW : rowScoreLow > lineScoreLow ? WINP1 : WINP2;
	}

	// bisschen stolz o.O
	public int calculateLineScore(int[] line) {
		int score = 0;
		String strline = "";
		for (int i : line) {
			strline += "" + i;
		}
		String[] dups = strline.split("(?<=(.))(?!\\1)");
		for (String ele : dups) {
			switch (ele.length()) {
			case 1:
				score += Integer.parseInt(ele);
				break;
			case 2:
				score += 10 * Integer.parseInt("" + ele.charAt(0));
				break;
			case 3:
				score += 100;
				break;
			case 4:
				score += 100 + Integer.parseInt("" + ele.charAt(0));
				break;
			case 5:
				score += 100 + 10 * Integer.parseInt("" + ele.charAt(0));
			}
		}
		return score;
	}

	public void addScore(double score) {
		if (this.winScore != Integer.MIN_VALUE) {
			this.winScore += score;
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(board[0][0] + " " + board[0][1] + " " + board[0][2] + " " + board[0][3] + " " + board[0][4] + "\n");
		sb.append(board[1][0] + " " + board[1][1] + " " + board[1][2] + " " + board[1][3] + " " + board[1][4] + "\n");
		sb.append(board[2][0] + " " + board[2][1] + " " + board[2][2] + " " + board[2][3] + " " + board[2][4] + "\n");
		sb.append(board[3][0] + " " + board[3][1] + " " + board[3][2] + " " + board[3][3] + " " + board[3][4] + "\n");
		sb.append(board[4][0] + " " + board[4][1] + " " + board[4][2] + " " + board[4][3] + " " + board[4][4] + "\n");

		sb.append("Hand P1: " + Arrays.toString(playerHands.get(0).toArray()) + "\n");
		sb.append("Hand P2: " + Arrays.toString(playerHands.get(1).toArray()) + "\n");

		return sb.toString();
	}

	public static void main(String[] args) {
		ArrayList<Integer> h1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5, 5, 5));
		ArrayList<Integer> h2 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1, 5, 5, 5));
		State s1 = new State(4, h1, h2);
		System.out.println(s1.getAllPossibleStates().size());
		while (s1.checkStatus() == IN_PROGRESS) {
			s1.randomPlay();
			s1.togglePlayer();
			System.out.println(s1);
		}
		System.out.println(s1.checkStatus());

	}

}
