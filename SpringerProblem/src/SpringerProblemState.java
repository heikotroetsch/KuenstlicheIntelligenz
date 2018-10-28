import java.util.ArrayList;
import java.util.PriorityQueue;

public class SpringerProblemState {

	// Board 2 = schwarz, 1 = weiß
	private int board[][] = new int[8][8];

	// Create Random State
	public SpringerProblemState() {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				this.board[i][j] = (int)(Math.random()*3);
			}
		}
		
	}

	private SpringerProblemState(int[] pos) {
		board[0][0] = pos[0];
		for (int i = 1; i < pos.length; i++) {
			board[i / 8][i % 8] = pos[i];
		}
	}

	public int evaluateState() {
		int res = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != 0) {
					// jeder Springer erhöht den Wert des Zustandes um 3(s) / 2(w) und die Summe
					// seines Positionsindex
					res += (board[i][j] == 2) ? 3 + i + j : 2 + i + j;

					// Paar benachbarter Springer unterschiedlicher Farbe erhöht den Wert des
					// Zustandes um 1
					for (int x = -1; x < 2; x += 2) {
						if (i + x < 8 && i + x > -1 && board[i + x][j] != 0 && board[i + x][j] != board[i][j]) {
							res += 0.5;
						}
						if (j + x < 8 && j + x > -1 && board[i][j + x] != 0 && board[i][j + x] != board[i][j]) {
							res += 0.5;
						}
					}

					// Jedes Paar von Springern, die sich schlagen können, reduziert die Qualität um
					// 3
					for (int x = -1; x < 2; x += 2) {
						for (int y = -1; y < 2; y += 2) {

							if (i + x * 2 < 8 && i + x * 2 >= 0 && j + y * 1 < 8 && j + y * 1 >= 0
									&& board[i + x * 2][j + y * 1] != 0) {
								res -= 1.5;
							}
							if (i + x * 1 < 8 && i + x * 1 >= 0 && j + y * 2 < 8 && j + y * 2 >= 0
									&& board[i + x * 1][j + y * 2] != 0) {
								res -= 1.5;
							}
						}
					}

				}
			}
		}
		return res;

	}
	
	@Override
	public String toString() {
		StringBuffer s1 = new StringBuffer();
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				s1.append(board[i][j]+" ");
			}
			s1.append("\n");
		}
		s1.append("Score: "+this.evaluateState());
		return s1.toString();
	}

	public ArrayList<SpringerProblemState> evolve(double mutRate, SpringerProblemState partner, int nrChil) {
		ArrayList<SpringerProblemState> children = new ArrayList<SpringerProblemState>();
		int[] dna = new int[64];
		for (int i = 0; i < 64; i++) {
			dna[i] = i % 2 == 0 ? this.board[i / 8][i % 8] : partner.board[i / 8][i % 8];
		}
		for (int i = 0; i < nrChil; i++) {
			int[] dnaCopy = dna.clone();
			for (int j = 0; j < 64; j++) {
				if (Math.random() < mutRate) {
					dnaCopy[i] = (int) (Math.random() * 3);
				}
			}
			children.add(new SpringerProblemState(dnaCopy));
		}
		
		return children;
	}

	public static void main(String[] args) {
		SpringerProblemState s = new SpringerProblemState(new int[64]);

	}
}