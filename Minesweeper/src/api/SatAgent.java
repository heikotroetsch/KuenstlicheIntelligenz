package api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.sat4j.core.VecInt;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;


public class SatAgent extends MSAgent {

	final int MAXVAR = 100000000;
	final int NBCCLAUSES = 500000;
	private boolean displayActivated = true;
	private boolean firstDecision = true;
	private ISolver solver;

	public SatAgent(MSField field) {
		super(field);
		solver = SolverFactory.newDefault();
		solver.newVar(100);
		solver.setExpectedNumberOfClauses(NBCCLAUSES);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean solve() {
		int numOfRows = this.field.getNumOfRows();
		int numOfCols = this.field.getNumOfCols();
		int x, y, feedback;
		ArrayList<Integer> leftFields = getFieldList();
		do {
			if (displayActivated) {
				//System.out.println(field);
			}
			if (firstDecision) {
				x = 0;
				y = 0;
				firstDecision = false;
				leftFields.remove(new Integer(0));
				//TODO CHECK 
				try {
					solver.addClause(new VecInt(new int[] {-999}));
				} catch (ContradictionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				int saveField = 0;
				for(int field : leftFields) {
					//1. Check field for mines -> if detected add to KB
					try {
						if(!solver.isSatisfiable(new VecInt(new int[] {-field}))) {
							////System.out.println("field "+field+" has Mine");
							solver.addClause(new VecInt(new int[] {field}));
						}
						if(!solver.isSatisfiable(new VecInt(new int[] {field}))) {
							////System.out.println("field "+field+" is good to go");
							solver.addClause(new VecInt(new int[] {-field}));
							saveField = field;
							break;
						}
					} catch (TimeoutException | ContradictionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
				
				if(saveField == 0) {
					System.out.println("picked Random");
					saveField = leftFields.get((int)(Math.random()*leftFields.size()));					
				}
				leftFields.remove(new Integer(saveField));
				////System.out.println(saveField);
				x = saveField / 10; 
				y = saveField % 10;
			}

			if (displayActivated) {
				////System.out.println("Uncovering (" + x + "," + y + ")");
			}
			feedback = field.uncover(x, y);
			//System.out.println(feedback);
			//System.out.println(field.toString());
			insertFeedbackIntoKB(feedback, x, y);
		} while (feedback >= 0 && !field.solved());

		if (field.solved()) {
			if (displayActivated) {
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

	private void insertFeedbackIntoKB(int fb, int x, int y) {
		//System.out.println(fb+" Minen an Stelle ("+x+","+y+")");
		ArrayList<Integer> nbs = neighbours(x,y);
		// Keine Miene um Stelle x,y -> Alle Nachbarn auf False 
//		if (fb == 0) {
//			try {
//				solver.addClause(new VecInt(new int[] { parseAtom(x, y) }));
//			} catch (ContradictionException e) {
//				// TODO Auto-generated catch block
//				//System.out.println("Contradiction Exception");
//			}
//		} else {
			// eine bis acht Minen
			for (int k = 0; k <= nbs.size(); k++) {
				// Alle Fälle, bis auf den Fall, der FB entspricht
				if (k != fb) {
					generateClauses(k,nbs);
				}
			}
		}
//	}


	// generates all possible positions for putting @param k mines around (x,y)
	private void generateClauses(int k, ArrayList<Integer>nbs) {
	//	ArrayList<Integer> nbs = neighbours(x, y);
		for (int i = 0; i <= nbs.size(); i++) {
			if (k != i) {
				for (int[] clause : mapPermToNeighbouts(nbs, new Permutations().permuteUnique(k, nbs.size()))) {
					try {
						solver.addClause(new VecInt(clause));
					} catch (ContradictionException e) {
						//System.out.println("Contradiction Exception");
					}
				}
			}

		}
	}

	private ArrayList<Integer> neighbours(int x, int y) {
		ArrayList<Integer> neighbours = new ArrayList<>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (!(i == 0 && j == 0)) {
					if (x + i < this.field.getNumOfCols() && x + i >= 0 && y + j < this.field.getNumOfRows()
							&& y + j >= 0) {
						neighbours.add(parseAtom(x + i, y + j));
					}
				}
			}
		}
//		//System.out.println("Field: ("+x+","+y+")");
//		//System.out.println(neighbours);
		return neighbours;
	}

	private static ArrayList<int[]> mapPermToNeighbouts(ArrayList<Integer> neighbours, List<List<Integer>> perms) {
		ArrayList<int[]> clauses = new ArrayList<int[]>();
		for (List<Integer> perm : perms) {
			int[] clause = new int[neighbours.size()];
			for (int i = 0; i < neighbours.size(); i++) {
				clause[i] = perm.get(i) == 0 ? neighbours.get(i)==0? 999:neighbours.get(i) : -neighbours.get(i)==0? -999:-neighbours.get(i);
			}
			//System.out.println("Clause: "+Arrays.toString(clause));
			clauses.add(clause);
		}

		return clauses;
	}

	private static int parseAtom(int x, int y) {
		String s = x + "" + y;
		return Integer.parseInt(s);
	}
	
	private ArrayList<Integer> getFieldList() {
		ArrayList<Integer> fieldList = new ArrayList<Integer>();
		for(int i = 0; i < field.getNumOfCols(); i++) {
			for(int j = 0; j < field.getNumOfRows(); j++) {
				fieldList.add(parseAtom(i, j));
			}
		}
		return fieldList;
	}

	

	@Override
	public void activateDisplay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivateDisplay() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		//System.out.println(new Permutations().permuteUnique(1, 3));
	}
}