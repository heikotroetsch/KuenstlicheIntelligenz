package eightpuzzle;

import java.util.HashSet;
import java.util.PriorityQueue;

public class EightPuzzleSolver {

	public static void main(String[] args) {
		System.out.println("Solution: ");
		long start = System.currentTimeMillis();
		EightPuzzleState sol = solve();
		long end = System.currentTimeMillis() - start;
		System.out.println(sol);
		System.out.println("in " + end + " ms");
	}

	private static EightPuzzleState solve() {
		PriorityQueue<EightPuzzleState> todo = new PriorityQueue<EightPuzzleState>(new EightPuzzleComparator());
		HashSet<EightPuzzleState> visitedNodes = new HashSet<EightPuzzleState>();
		todo.add(new EightPuzzleState());
		while (!todo.isEmpty()) {
			EightPuzzleState s = todo.poll();
			if (!visitedNodes.contains(s)) {
				if (s.isSolution()) {
					return s;
				} else {
					visitedNodes.add(s);
					todo.addAll(s.expand());
				}
			}
		}
		return null;
	}

}
