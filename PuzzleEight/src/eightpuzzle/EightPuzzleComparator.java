package eightpuzzle;

import java.util.Comparator;

public class EightPuzzleComparator implements Comparator<EightPuzzleState>{

	@Override
	public int compare(EightPuzzleState o1, EightPuzzleState o2) {
		return o1.getHeuristicValue() != o2.getHeuristicValue() ? o1.getHeuristicValue() < o2.getHeuristicValue() ? -1 : 1 : 0;
	}
	

}
