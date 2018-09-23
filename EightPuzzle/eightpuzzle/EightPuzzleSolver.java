package eightpuzzle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;

public class EightPuzzleSolver {

	public static void main(String[] args) {
		System.out.println("Solution: ");
		long start = System.currentTimeMillis();
		System.out.println(solve());
		System.out.println("Time: " + (System.currentTimeMillis() - start) + "ms");
	}

	private static String solve() {
		PriorityQueue<EightPuzzleState> todo = new PriorityQueue<EightPuzzleState>(new EightPuzzleComparator());
		todo.add(new EightPuzzleState());
			while (!todo.isEmpty()) {		
			EightPuzzleState s = todo.poll();
				if (s.isSolution()) {
					return "found one";
				} else {
					todo.addAll(s.expand());
				}
			}
		return "failed";
	}

}
