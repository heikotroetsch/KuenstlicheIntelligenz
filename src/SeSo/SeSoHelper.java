package SeSo;
class SeSoHelper {
	private static int[] board = {-1,-1,-1,-1,-1,-1,-1,-1,0,1,1,1,1,1,1,1,1};
	private static int[][] neighbours = {{1,2,3,6},{0,2,4,7},{0,1,5,8},{0,4,5,6},{1,3,5,7},{2,3,4,8,11},{0,3,7,8},{1,4,6,8,9},{2,5,6,7,9,10,11,14},{7,8,10,12,15},{8,9,13,16},{5,8,12,13,14},{9,11,13,15},{10,11,12,16},{8,11,15,16},{9,12,14,16},{10,13,14,15}};
	
	
	public static int[] getStartingBoard() {
		return board;
	}
	public static int[] getNeighbours(int x) {
		return neighbours[x];
	}
}

	

