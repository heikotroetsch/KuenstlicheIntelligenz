package eightpuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import SeSo.SeSoState;

public class EightPuzzleState {

	private int[] board;
	private EightPuzzleState parent;
	private int lastMove;
	private int heuristicValue;
	private int costs;

	public EightPuzzleState() {
		parent = null;
		board = EightPuzzleHelper.getStartingBoard();
		lastMove = 0;
		heuristicValue = calculateHeuristicValue();
		costs = 0;
	}

	// Erzeugt einen Zustand, bei dem ausgehend vom Zustand other das leere Feld
	// durch die spezifizierte Figur aufgezogen wurde
	public EightPuzzleState(EightPuzzleState other, int position) {
		parent = other;
		lastMove = position;
		board = Arrays.copyOf(other.board, other.board.length);
		int temp = board[position];
		board[getEmptyField(board)] = temp;
		board[position] = 0;
		costs = other.costs+1;
		heuristicValue = costs + this.calculateHeuristicValue();
	}
	
	public EightPuzzleState(int [] board_i) {
		board = board_i;
		heuristicValue = calculateHeuristicValue();
	}

	private int getEmptyField(int[] board) {
		for (int i = 0; i < board.length; i++) {
			if (board[i] == 0) {
				return i;
			}
		}
		return -1;
	}
	
	private int calculateHeuristicValue() {
		int hv = 0;
		for(int i = 0; i < board.length; i++) {
			//Heuristic Debugging
			//System.out.println("Feld: "+i+" Stein: "+board[i]+" HV "+EightPuzzleHelper.getHeuristicForField(i, board[i]));
			hv += EightPuzzleHelper.getHeuristicForField(i, board[i]);
		}
		return hv;
	}

	public List <EightPuzzleState> expand(){
		ArrayList<EightPuzzleState> children = new ArrayList<EightPuzzleState>();
		int[] neighbours = EightPuzzleHelper.getNeighbours(getEmptyField(board));
		for(int i = 0; i < neighbours.length; i++) {
			children.add(new EightPuzzleState(this,neighbours[i]));
		}
		return children;
	}
	
	public boolean isSolution() {
		return Arrays.hashCode(board) == EightPuzzleHelper.getSolutionHash();
	}
	
	public int getHeuristicValue() {
		return heuristicValue;
	}
	
	public String toString_visual() {
		StringBuffer s = new StringBuffer();
		for(int i = 0; i < board.length; i++) {
			s.append(board[i]+(((i+1)%3 == 0)?"\n":" "));
		}
		return s.toString();
	}
	
	@Override 
	public String toString() {
		if(parent == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		EightPuzzleState cur = this;
		while(cur.parent!=null) {
			sb.insert(0, cur.lastMove+"-");
			cur = cur.parent;
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString()+"\n"+this.toString_visual();
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
		EightPuzzleState epState = (EightPuzzleState) o;
		return this.hashCode() == epState.hashCode();
	}	

}
