package bohnenspiel;

import java.util.ArrayList;
import java.util.List;


public class BohnenspielState {
	
	int[] board;
	int scoreAI,scoreEnemy;
	private BohnenspielState parent;
	private int lastMove; 
	private boolean aiTurn;
	
	//Erzeugt das anfaengliche Spielfeld
	public BohnenspielState(boolean aiTurn) {
		board = BohnenspielHelper.getBoard();
		scoreAI = 0;
		scoreEnemy = 0;
		parent = null;
		lastMove = 0; 
		this.aiTurn = aiTurn;
	}
	
	//Erzeugt einen Zustand, bei dem ausgehend vom Zustand old, ein Zug ausgeführt wurde. 
	public BohnenspielState(BohnenspielState old, int index) {
		parent = old;
		lastMove = index;
		board = old.board.clone();
		scoreAI = old.scoreAI;
		scoreEnemy = old.scoreEnemy;
		this.aiTurn = !old.aiTurn;
		makeMove(index, aiTurn);		
	}
	
	public void makeMove(int index, boolean aiTurn) {
		index--;
		int beans = board[index];
		board[index] = 0;
		
		while(beans>0) {
			index++;
			board[(index)%12]++;
			beans--;
		}
		checkScore((index)%12, aiTurn);
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
	
	public int calculateHeuristivValue() {
		int hv = this.scoreAI - scoreEnemy;
		hv += countBeans(this.aiTurn);
		//TODO implement countHolesWithScore
		return hv;		
	}
	
	private int countBeans(boolean aiTurn) {
		int beanScoreAI = 0;
		int beanScoreEnemy = 0;
		for(int i = 0; i < 12; i++) {
			if(i < 6) {
				beanScoreAI += board[i];
			}else {
				beanScoreEnemy += board[i];
			}
		}
		return beanScoreAI - beanScoreEnemy;
	}
	
	//TODO implement
	private int countHolesWithScore() {
		return -1;
	}
	
	public int getLastMove() {
		return this.lastMove;
	}
	
	
	public List<BohnenspielState> expand(){
		ArrayList<BohnenspielState> children = new ArrayList<BohnenspielState>();
		//TODO TURN Berücksichtigen
		for(int i = 0; i < 6; i++) {
			if(board[i]!= 0) {
				children.add(new BohnenspielState(this,i));
			}
		}
		return children;
	}
	
	public String toString() {
		return "AI Score: "+scoreAI+"\n"+
				"Enemy Score: "+scoreEnemy+"\n"+
				board[11]+" "+board[10]+" "+board[9]+" "+board[8]+" "+board[7]+" "+board[6]+" "+"\n"+
				board[0]+" "+board[1]+" "+board[2]+" "+board[3]+" "+board[4]+" "+board[5]+"\n";
	}
	
}
