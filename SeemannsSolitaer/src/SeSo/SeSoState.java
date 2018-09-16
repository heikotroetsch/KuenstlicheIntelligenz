package SeSo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
	public SeSoState(SeSoState other, int token) {
		parent = other;	
		lastMove = token;
		board = other.board.clone();
		int temp = board[token];
		board[getEmptyField(board)] = temp;
		board[token] = 0;				
	}	
	
	public int getEmptyField(int[] board) {
		for(int i = 0; i<board.length;i++) {
			if(board[i]==0) {
				return i;
			}
		}
		return -1;
	}
	
	public List<SeSoState> expand(){
		ArrayList<SeSoState> children = new ArrayList<SeSoState>();
		int[] neighbours = SeSoHelper.getNeighbours(getEmptyField(board));
		for(int i = 0; i < neighbours.length; i++) {
			children.add(new SeSoState(this,neighbours[i]));
		}
		return children;
	}
	
	public boolean isSolution() {
		return Arrays.hashCode(board)==SeSoHelper.getSolutionHash();	

	}
	
	public String toString_visual() {
		StringBuffer s = new StringBuffer("    "+gs(board[14])+" "+gs(board[15])+" "+gs(board[16])+"\n");
								 s.append("    "+gs(board[11])+" "+gs(board[12])+" "+gs(board[13])+"\n");
								 s.append(gs(board[6])+" "+gs(board[7])+" "+gs(board[8])+" "+gs(board[9])+" "+gs(board[10])+"\n");
								 s.append(gs(board[3])+" "+gs(board[4])+" "+gs(board[5])+"    \n");
								 s.append(gs(board[0])+" "+gs(board[1])+" "+gs(board[2])+"    \n");
		return s.toString();
	}
	
	public String toStringVisualMoves() {
		if(parent == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		SeSoState cur = this;
		while(cur.parent!=null) {
			sb.insert(0, cur.toString_visual()+"\n");
			cur = cur.parent;
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	@Override 
	public String toString() {
		if(parent == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		SeSoState cur = this;
		while(cur.parent!=null) {
			sb.insert(0, cur.lastMove+"-");
			cur = cur.parent;
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString()+"\n"+this.toStringVisualMoves();
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
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if(this.getClass() != o.getClass()) {
			return false;
		}
		SeSoState sesoState = (SeSoState) o;
		return this.hashCode() == sesoState.hashCode();
	}
	
}
