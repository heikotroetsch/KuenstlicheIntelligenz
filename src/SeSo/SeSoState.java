package SeSo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeSoState {

	private int[] board;
	private SeSoState parent;
	private int lastMove;
		
	public SeSoState() {
		parent = null;
		board = SeSoHelper.getStartingBoard();
		lastMove = 0;
	}
	
	//Erzeugt einen Zustand, bei dem ausgehend vom Zustand other das leere Feld durch die spezifizierte Figur aufgezogen wurde. 
	private SeSoState(SeSoState other, int token) {
		parent = other;	
		lastMove = token;
		board = new int[17];
		for(int i = 0; i < other.board.length; i++) {
			board[i] = other.board[i];
		}
		int temp = board[token];
		board[getEmptyField(board)] = temp;
		board[token] = 0;				
	}	
	
	public static int getEmptyField(int[] board) {
		for(int i = 0; i < board.length; i++) {
			if (board[i] == 0) {
				return i;
			}
		}
		return -1;
	}
	
	public List<SeSoState> expand(){
		ArrayList<SeSoState> children = new ArrayList<SeSoState>();
		int[] neighbours = SeSoHelper.getNeighbours(getEmptyField(board));
		for(int i = 1; i < neighbours.length; i++) {
			children.add(new SeSoState(this,neighbours[i]));
		}
		return children;
	}
	
	public boolean isSolution() {
		for(int i = 0 ; i < 17; i++) {
			if(i < 8) {
				if(board[i] != 1) {
					return false;
				}
			} else if(i == 8) {
				if(board[i] != 0) {
					return false;
				}
			} else {
				if(board[i] != -1) {
					return false;
				}				
			}
		}
		return true;
	}
	
	public String toString_visual() {
		StringBuffer s = new StringBuffer("    "+gs(board[14])+" "+gs(board[15])+" "+gs(board[16])+"\n");
								 s.append("    "+gs(board[11])+" "+gs(board[12])+" "+gs(board[13])+"\n");
								 s.append(gs(board[6])+" "+gs(board[7])+" "+gs(board[8])+" "+gs(board[9])+" "+gs(board[10])+"\n");
								 s.append(gs(board[3])+" "+gs(board[4])+" "+gs(board[5])+"    \n");
								 s.append(gs(board[0])+" "+gs(board[1])+" "+gs(board[2])+"    \n");
		return s.toString();
	}
	
	@Override 
	public String toString() {
		if(parent == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		SeSoState cur = this;
		while(cur.parent!=null) {
			sb.append(cur.lastMove);
			sb.append('-');
			cur = cur.parent;
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	private char gs(int n) {
		switch (n) {
		case 0: return 'o';
		case 1: return 's';
		case -1: return 'w';
		default: return 'x';
		}
		 
	}
	
	@Override 
	public int hashCode() {
		return Arrays.hashCode(board);		
	}
	
	@Override 
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if(this.getClass() != o.getClass()) return false;
		SeSoState sesoState = (SeSoState) o;
		return this.board.hashCode() == sesoState.board.hashCode();
	}
	
	public static void main(String[] args) {
		SeSoState x = new SeSoState();
		SeSoState y = new SeSoState(x, 7);
		SeSoState z = new SeSoState(y, 8);
		System.out.println(z.toString_visual());
		System.out.println(x.toString_visual());
		System.out.println(x.equals(z));
		System.out.println(x.hashCode());
		System.out.println(y.hashCode());
		System.out.println(z.hashCode());
		
	}
}
