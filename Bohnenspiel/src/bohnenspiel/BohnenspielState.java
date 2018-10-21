package bohnenspiel;

import java.util.ArrayList;
import java.util.List;

public class BohnenspielState {

	int[] board;
	int scoreAI, scoreEnemy;
	private boolean aiTurn;
	// position for AI 1-6 = true, 7-12 = false
	private boolean aiPosition;
	private int lastMove;

	// Erzeugt das anfaengliche Spielfeld
	public BohnenspielState() {
		board = BohnenspielHelper.getBoard();
		scoreAI = 0;
		scoreEnemy = 0;
		lastMove = 0;
	}

	// Erzeugt einen Zustand, bei dem ausgehend vom Zustand old, ein Zug
	// ausgefï¿½hrt wurde.
	public BohnenspielState(BohnenspielState old, int index) {
		board = old.board.clone();
		scoreAI = old.scoreAI;
		scoreEnemy = old.scoreEnemy;
		this.aiTurn = !old.aiTurn;
		this.aiPosition = old.aiPosition;
		lastMove = index;
		makeMove(index);
	}

	/**
	 * Makes the move on the board array and checks the score. 
	 * @param index index of the move beeing made
	 */
	public void makeMove(int index) {
		int beans = board[index];
		board[index] = 0;

		while (beans > 0) {
			index++;
			board[(index) % 12]++;
			beans--;
		}
		checkScore((index) % 12);
	}

	/**
	 * Checks the score recursively
	 * @param index 
	 */
	public void checkScore(int index) {
		if (board[index] == 2 || board[index] == 4 || board[index] == 6) {
			if (!aiTurn) {
				scoreAI += board[index];
			} else {
				scoreEnemy += board[index];
			}
			board[index] = 0;
			checkScore(Math.floorMod(index - 1, 12));
		}
	}

	/**
	 * Check if the node is terminal and then transfares all points
	 * @return
	 */
	public boolean isTerminalNode() {
		if (this.expand().isEmpty()) {
			transferFinalPoints();
			return true;
		} else {
			if (scoreAI > 36 || scoreEnemy > 36) {
				return true;
			}
		}
		return false;

	}
	
	/**
	 * Adds the beans on each side to the score board. 
	 */
	public void transferFinalPoints(){
		if(aiPosition) {
			for(int i = 0; i < 6; i++) {
				scoreAI += board[i];
			}
			for(int i = 6; i < 12; i++) {
				scoreEnemy += board[i];
			}
		} else {
			for(int i = 0; i < 6; i++) {
				scoreEnemy += board[i];
			}
			for(int i = 6; i < 12; i++) {
				scoreAI += board[i];
			}
		}
		
	}

	/**
	 * Die Heuristik zur Evaluation der Spielst ̈ande ber ̈ucksicht folgende Aspekte desSpielzustandes:•x1: Die Punktzahlen der Spieler (Score AI - Score Gegner)•x2: Anzahl der Bohnen auf der Seite der AI im Verh ̈altnis zu den Bohnenauf dem Spielfeld.(Bohnen AI / Bohnen im Spiel)•x3: Anzahl der m ̈oglichen Z ̈uge (Anzahl Nullmulden Gegner - Anzahl Null-mulden AI)1
Inklusive Gewichtung der einzelnen Faktoren lautet die Heuristik folgenderma-ßen:HV=x1 +x2∗18 +x3∗2
	 * @return the heuristic value
	 */
	public int calculateHeuristivValue() {
		int heuristic = this.scoreAI - scoreEnemy;
		heuristic += (countMyBeans()/(countBeans()==0?36:countBeans())) * 18;
		heuristic += countPossiblePlays() * 2;
		return heuristic;
	}
	
	/**
	 * @return returns the difference scoreAI - scoreEnemy
	 */
	public int scoreDifference() {
		return  scoreAI - scoreEnemy;
	}

	/**
	 * Evaluates how many possibilities each player has to play. The difference between that will be returned. 
	 * @return difference of number of possible plays
	 */
	private int countPossiblePlays() {
		int playCountAI = 0;
		int playCountEnemy = 0;
		if (this.aiPosition) {
			for (int i = 0; i < 12; i++) {
				if (i < 6 && board[i] == 0) {
					playCountAI++;
				} else if (i >= 6 && board[i] == 0) {
					playCountEnemy++;
				}
			}
		} else {
			for (int i = 0; i < 12; i++) {
				if (i >= 6 && board[i] == 0) {
					playCountAI++;
				} else if (i < 6 && board[i] == 0) {
					playCountEnemy++;
				}
			}
		}
		return playCountEnemy - playCountAI;
	}

	/**
	 * @return count of the beans on the AI side
	 */
	private int countMyBeans() {
			int beanCountAI = 0;
			if(this.aiPosition) {
				for(int i = 0; i < 12; i++) {
					if(i < 6) {
						beanCountAI += board[i];
					}
				}
			} else{
				for(int i = 0; i < 12; i++) {
					if(i >= 6) {
						beanCountAI += board[i];
					}
				}
			}
			return beanCountAI;
	}

	/**
	 * Counts the difference between the beans on the AI side and on the Enemy Side
	 * @return the differences of counted beans
	 */
	private int countBeans() {
		int beanCountAI = 0;
		int beanCountEnemy = 0;
		if (this.aiPosition) {
			for (int i = 0; i < 12; i++) {
				if (i < 6) {
					beanCountAI += board[i];
				} else {
					beanCountEnemy += board[i];
				}
			}
		} else {
			for (int i = 0; i < 12; i++) {
				if (i >= 6) {
					beanCountAI += board[i];
				} else {
					beanCountEnemy += board[i];
				}
			}
		}
		return beanCountAI + beanCountEnemy;
	}


	public int getLastMove() {
		return this.lastMove;
	}

	public boolean getAITurn() {
		return this.aiTurn;
	}

	public void setAiTurn(boolean aiTurn, boolean aiPosition) {
		this.aiTurn = aiTurn;
		this.aiPosition = aiPosition;
	}

	public List<BohnenspielState> expand() {
		ArrayList<BohnenspielState> children = new ArrayList<BohnenspielState>();
		if (this.aiTurn == this.aiPosition) {
			for (int i = 0; i < 6; i++) {
				if (board[i] != 0) {
					children.add(new BohnenspielState(this, i));
				}
			}
		} else{
			for (int i = 6; i < 12; i++) {
				if (board[i] != 0) {
					children.add(new BohnenspielState(this, i));
				}
			}
		}
		return children;
	}

	public String toString() {
		return "AI Score: " + scoreAI + "\n" + "Enemy Score: " + scoreEnemy + "\n" + board[11] + " " + board[10] + " "
				+ board[9] + " " + board[8] + " " + board[7] + " " + board[6] + " " + "\n" + board[0] + " " + board[1]
				+ " " + board[2] + " " + board[3] + " " + board[4] + " " + board[5] + "\n";
	}
}
