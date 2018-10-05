package eightpuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EightPuzzleState {

	private int[] board;
	private EightPuzzleState parent;
	private int lastMove;
	private int heuristicValue;
	private int costs;
	private int emptyField;

	public EightPuzzleState() {
		parent = null;
		board = EightPuzzleHelper.getStartingBoard();
		lastMove = 0;
		heuristicValue = calculateHeuristicValue();
		costs = 0;
		emptyField = 0;
	}

	// Erzeugt einen Zustand, bei dem ausgehend vom Zustand other das leere Feld
	// durch die spezifizierte Figur aufgezogen wurde
	public EightPuzzleState(EightPuzzleState other, int position) {
		parent = other;
		lastMove = position;
		board = other.board.clone();
		int temp = board[position];
		board[other.getEmptyField()] = temp;
		board[position] = 0;
		emptyField = position;
		costs = other.costs+1;
		heuristicValue = costs + this.calculateHeuristicValue();
	}
	
	public EightPuzzleState(int [] board_i) {
		board = board_i;
		heuristicValue = calculateHeuristicValue();
	}

	private int getEmptyField() {
		return emptyField;
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
		int[] neighbours = EightPuzzleHelper.getNeighbours(this.getEmptyField());
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
	
	public int getCosts() {
		return costs;
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
		int counter = 0;
		while(cur.parent!=null) {
			counter++;
			sb.insert(0, cur.lastMove+"-");
			cur = cur.parent;
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString()+" in "+counter+" Zügen\n"+this.toStringVisualMoves();
	}
	
	public String toStringVisualMoves() {
		if(parent == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		EightPuzzleState cur = this;
		while(cur.parent!=null) {
			sb.insert(0, cur.toString_visual()+"\n");
			cur = cur.parent;
		}
		sb.insert(0, new EightPuzzleState().toString_visual()+"\n");
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
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
