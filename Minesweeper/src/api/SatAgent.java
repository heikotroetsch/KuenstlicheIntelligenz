package api;

public class SatAgent extends MSAgent {
	
	private boolean displayActivated = true;
	private boolean firstDecision = true;

	public SatAgent(MSField field) {
		super(field);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean solve() {
		int numOfRows = this.field.getNumOfRows();
		int numOfCols = this.field.getNumOfCols();
		int x,y, feedback;
		
		do {
			if(displayActivated) {
				System.out.println(field);
			}
			if(firstDecision) {
				x = 0; 
				y = 0;
				firstDecision = false;
			} else {
				x = 0; 
				y = 0; 				
			}
			
			if (displayActivated) {
				System.out.println("Uncovering ("+x+","+y+")");
			}
			feedback = field.uncover(x, y);
			insertFeedbackIntoKB(feedback, x,y);
			System.out.println(feedback);
			System.out.println(field.toString());
			break;
		} while (feedback >= 0 && ! field.solved());
		
		if (field.solved()) {
			if(displayActivated) {
				System.out.println("Solved the field");
			}
			return true;
		} else {
			if (displayActivated) {
				System.out.println("BOOM!");
			}
			return false;
		}
	}
	
	private void insertFeedbackIntoKB() {
	
	@Override
	public void activateDisplay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivateDisplay() {
		// TODO Auto-generated method stub

	}
}