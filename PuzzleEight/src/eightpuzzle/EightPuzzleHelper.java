package eightpuzzle;

import java.util.Arrays;

public class EightPuzzleHelper {

	private static int[] board = { 0, 6, 3, 1, 4, 7, 2, 5, 8 };
	private static int[] solution = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private static int[][] neighbours = { { 1, 3 }, { 0, 2, 4 }, { 1, 5 }, { 0, 4, 6 }, { 1, 3, 5, 7 }, { 2, 4, 8 },
			{ 3, 7 }, { 4, 6, 8 }, { 5, 7 } };
	private static int solutionHash = Arrays.hashCode(solution);

	// Heuristik-Werte f�r Gaschnik + Manhattan
	private static int[][] heuristicValues =   {{ 0, 1, 2, 1, 2, 3, 2, 3, 4 },
												{ 1, 0, 1, 2, 1, 2, 3, 2, 3 },
												{ 2, 1, 0, 3, 2, 1, 4, 3, 2 },
												{ 1, 2, 3, 0, 1, 2, 1, 2, 3 },
												{ 2, 1, 2, 1, 0, 1, 2, 1, 2 },
												{ 3, 2, 1, 2, 1, 0, 3, 2, 1 },
												{ 2, 3, 4, 1, 2, 3, 0, 1, 2 },
												{ 3, 2, 3, 2, 1, 2, 1, 0, 1 },
												{ 4, 3, 2, 3, 2, 1, 2, 1, 0 }};

	public static int[] getStartingBoard() {
		return board;
	}

	public static int[] getNeighbours(int x) {
		return neighbours[x];
	}

	public static int getSolutionHash() {
		return solutionHash;
	}
	
	public static int[] getSolution() {
		return solution;
	}
	
	public static int getHeuristicForField(int field, int stone) {
		return heuristicValues[field][stone];
	}
	
}
