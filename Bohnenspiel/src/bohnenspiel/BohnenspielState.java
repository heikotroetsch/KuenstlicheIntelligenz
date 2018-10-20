package bohnenspiel;

import java.util.ArrayList;
import java.util.List;


public class BohnenspielState {
	
	int[] board;
	int scoreAI,scoreEnemy;
	private boolean aiTurn;
	//position for AI 1-6 = true, 7-12 = false
	private boolean aiPosition;
	private int lastMove;
	
	//Erzeugt das anfaengliche Spielfeld
	public BohnenspielState() {
		board = BohnenspielHelper.getBoard();
		scoreAI = 0;
		scoreEnemy = 0;
		lastMove = 0;
		}
	
	//Erzeugt einen Zustand, bei dem ausgehend vom Zustand old, ein Zug ausgef�hrt wurde. 
	public BohnenspielState(BohnenspielState old, int index) {
		board = old.board.clone();
		scoreAI = old.scoreAI;
		scoreEnemy = old.scoreEnemy;
		this.aiTurn = !old.aiTurn;
		this.aiPosition = old.aiPosition;
		lastMove = index;
		makeMove(index);		
	}
	
	public void makeMove(int index) {
//		index--;
		int beans = board[index];
		board[index] = 0;
		
		while(beans>0) {
			index++;
			board[(index)%12]++;
			beans--;
		}
		checkScore((index)%12);
	//	System.out.println(this);
	}


	public void checkScore(int index){
		if(board[index]==2||board[index]==4||board[index]==6) {
			if(aiTurn) {
				scoreAI += board[index];
			}else {
				scoreEnemy += board[index];
			}
			board[index] = 0;
			checkScore(Math.floorMod(index-1, 12));
		}
	}
	
	public int calculateHeuristivValue() {
		int heuristic = this.scoreAI - scoreEnemy;
		heuristic += countPossiblePlays() * 10;
		heuristic += countBeans() / 4;
		//heuristic += evaluateKill() * 4;
		return heuristic;		
	}
	
	private int countPossiblePlays() {
		int playCountAI = 0;
		int playCountEnemy = 0;
		if(this.aiPosition) {
			for(int i = 0; i < 12; i++) {
				if(i < 6 && board[i]==0) {
					playCountAI++;
				}else if(i >= 6 && board[i]==0){
					playCountEnemy++;
				}
			}
		} else{
			for(int i = 0; i < 12; i++) {
				if(i >= 6  && board[i]==0) {
					playCountAI++;
				}else if(i < 6 && board[i]==0){
					playCountEnemy++;
				}
			}
		}
		return playCountEnemy - playCountAI;
	}
	
	private int countBeans() {
		int beanCountAI = 0;
		int beanCountEnemy = 0;
		if(this.aiPosition) {
			for(int i = 0; i < 12; i++) {
				if(i < 6) {
					beanCountAI += board[i];
				}else {
					beanCountEnemy += board[i];
				}
			}
		} else{
			for(int i = 0; i < 12; i++) {
				if(i >= 6) {
					beanCountAI += board[i];
				}else {
					beanCountEnemy += board[i];
				}
			}
		}
		return beanCountAI - beanCountEnemy;
	}
	
	private int evaluateKill() {
		int beanScoreAI = 0;
		int beanScoreEnemy = 0;
		if(this.aiPosition) {
			for(int i = 0; i < 12; i++) {
				if(i < 6) {
					if(board[i]== 2 || board[i]== 4 || board[i]== 6) {
						beanScoreAI += board[i];
					}
				}else {
					if(board[i]== 2 || board[i]== 4 || board[i]== 6) {
						beanScoreAI += board[i];
					}
				}
			}
		} else{
			for(int i = 0; i < 12; i++) {
				if(i >= 6) {
					if(board[i]== 2 || board[i]== 4 || board[i]== 6) {
						beanScoreAI += board[i];
					}
				}else {
					if(board[i]== 2 || board[i]== 4 || board[i]== 6) {
						beanScoreAI += board[i];
					}
				}
			}
		}
		return beanScoreAI - beanScoreEnemy;
	}
	
	public int getLastMove() {
		return this.lastMove;
	}
	
	public boolean getAITurn() {
		return this.aiTurn;
	}
	
	public void setAiTurn(boolean aiTurn, boolean aiPosition) {
		this.aiTurn = aiTurn;
		this.aiPosition = aiPosition;
	}
	
	
	public List<BohnenspielState> expand(){
		ArrayList<BohnenspielState> children = new ArrayList<BohnenspielState>();
		if(this.aiTurn&&this.aiPosition) {
			for(int i = 0; i < 6; i++) {
				if(board[i]!= 0) {
					children.add(new BohnenspielState(this,i));
				}
			}
		} else if(this.aiTurn&&!this.aiPosition) {
			for(int i = 6; i < 12; i++) {
				if(board[i]!= 0) {
					children.add(new BohnenspielState(this,i));
				}
			}
		}else if(!this.aiTurn&&this.aiPosition) {
			for(int i = 6; i < 12; i++) {
				if(board[i]!= 0) {
					children.add(new BohnenspielState(this,i));
				}
			}
		}else if(!this.aiTurn&&!this.aiPosition) {
			for(int i = 0; i < 6; i++) {
				if(board[i]!= 0) {
					children.add(new BohnenspielState(this,i));
				}
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
