package bohnenspiel;

public class Bohnenspiel {
	
	int[] board = new int[12];
	int scoreAI = 0;
	int scoreEnemy = 0;
	
	public Bohnenspiel() {
		//fill board with 6 stones in each mole.
		for(int i = 0;i<board.length;i++) {
			board[i] = 6;
		}
		System.out.println(this.toString());
	}
	
	public void makeMove(int index, boolean aiTurn) {
		index--;
		System.out.println("Played Index: "+index);
		int beans = board[index];
		board[index] = 0;
		
		while(beans>0) {
			index++;
			board[(index)%12]++;
			beans--;
		}
		checkScore((index)%12, aiTurn);
		System.out.println(this.toString());
	}
	

	public void checkScore(int index, boolean aiTurn){
		if(board[index]==2||board[index]==4||board[index]==6) {
			if(aiTurn) {
				scoreAI += board[index];
			}else {
				scoreEnemy += board[index];
			}
			board[index] = 0;
			checkScore(Math.floorMod(index-1, 12), aiTurn);
		}
	}
	
	public String toString() {
		return "AI Score: "+scoreAI+"\n"+
				"Enemy Score: "+scoreEnemy+"\n"+
				board[11]+" "+board[10]+" "+board[9]+" "+board[8]+" "+board[7]+" "+board[6]+" "+"\n"+
				board[0]+" "+board[1]+" "+board[2]+" "+board[3]+" "+board[4]+" "+board[5]+"\n";
	}
	
}
