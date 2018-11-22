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
	private boolean displayActivated = false;
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
		int x = 0, y = 0, feedback =-1;
		ArrayList<Integer> leftFields = getFieldList();
		int lastUncovered;
		do {
			if (displayActivated) {
				// System.out.println(field);
			}
			if (firstDecision) {
				x = 0;
				y = 0;
				firstDecision = false;
				leftFields.remove(new Integer(0));
				// TODO CHECK
				try {
					solver.addClause(new VecInt(new int[] { -999 }));
				} catch (ContradictionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (displayActivated) {
					System.out.println("Uncovering (" + x + "," + y + ")");
				}
				// System.out.println(x+" , "+y);
				feedback = field.uncover(x, y);
				lastUncovered = parseAtom(x, y);
				// System.out.println(feedback);
				System.out.println(field.toString());
				insertFeedbackIntoKB(feedback, x, y);
			} else {
				// TODO change int with ArrayList for all available fields
				ArrayList<Integer> saveFields = new ArrayList<Integer>();
				ArrayList<Integer> mineFields = new ArrayList<Integer>();
				for (int field : leftFields) {

					try {
						// CHECK FOR MINE
						if (!solver.isSatisfiable(new VecInt(new int[] { -field }))) {
							//// System.out.println("field "+field+" has Mine");
							solver.addClause(new VecInt(new int[] { field }));
							mineFields.add(field);
						}
						// CHECK FOR OPEN
						if (!solver.isSatisfiable(new VecInt(new int[] { field }))) {
							//// System.out.println("field "+field+" is good to go");
							solver.addClause(new VecInt(new int[] { -field }));
							saveFields.add(field);
							// TODO rm
						}
					} catch (TimeoutException | ContradictionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for(int f : mineFields) {
					leftFields.remove(new Integer(f));
				}
				if (saveFields.isEmpty()) {
					System.out.println("picked Random");
					pickRandField(lastUncovered);
					saveFields.add(leftFields.get((int) (Math.random() * leftFields.size())));
				}
				for(int f : saveFields) {
				// TODO LOOP OVER ALL FIELDS IN LIST
				
				leftFields.remove(new Integer(f));
				//// System.out.println(saveField);
				// System.out.println("Precalculus " +saveField);
				// DIRTY
				int[] res = getFieldFromAtom(f);
				x = res[0];
				y = res[1];
				// System.out.println(x+" "+y);
				if (displayActivated) {
					System.out.println("Uncovering (" + x + "," + y + ")");
				}
				// System.out.println(x+" , "+y);
				feedback = field.uncover(x, y);
				lastUncovered = parseAtom(x,y);
				// System.out.println(feedback);
				System.out.println(field.toString());
				insertFeedbackIntoKB(feedback, x, y);
				}
			}

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

	private int[][] pickRandField(int lastUncovered) {
	    int x = getFieldFromAtom(lastUncovered)[0];
	    int y = getFieldFromAtom(lastUncovered)[1];
	    return null;
  }

  private void insertFeedbackIntoKB(int fb, int x, int y) {
		// System.out.println(fb+" Minen an Stelle ("+x+","+y+")");
		ArrayList<Integer> nbs = neighbours(x, y);
		// TODO Keine Miene um Stelle x,y -> Alle Nachbarn (nbs) auf false
		if (fb == 0) {
			System.out.println("0 found");
			for (int i : nbs) {
				try {
					System.out.println(i);
					solver.addClause(new VecInt(new int[] { -i }));
				} catch (ContradictionException e) {
					// TODO Auto-generated catch block
					// System.out.println("Contradiction Exception");
				}
			}
		} else {
			// eine bis acht Minen
			for (int k = 0; k <= nbs.size(); k++) {
				// Alle F�lle, bis auf den Fall, der FB entspricht
				if (k != fb) {
					generateClauses(k, nbs);
				}
			}
		}
	}

	// generates all possible positions for putting @param k mines around (x,y)
	private void generateClauses(int k, ArrayList<Integer> nbs) {
		// ArrayList<Integer> nbs = neighbours(x, y);
		for (int i = 0; i <= nbs.size(); i++) {
			if (k != i) {
				for (int[] clause : mapPermToNeighbouts(nbs, new Permutations().permuteUnique(k, nbs.size()))) {
					try {
						solver.addClause(new VecInt(clause));
					} catch (ContradictionException e) {
						// System.out.println("Contradiction Exception");
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
				clause[i] = perm.get(i) == 0 ? neighbours.get(i) == 0 ? 999 : neighbours.get(i)
						: -neighbours.get(i) == 0 ? -999 : -neighbours.get(i);
			}
			// System.out.println("Clause: "+Arrays.toString(clause));
			clauses.add(clause);
		}

		return clauses;
	}

	private static int parseAtom(int x, int y) {
		int lengthX = x == 0 ? 1 : (int) (Math.log10(x) + 1);
		String s = lengthX + "" + x + "" + y;
		// System.out.println(s);
		return Integer.parseInt(s);
	}

	private int[] getFieldFromAtom(int x) {
		String s = String.valueOf(x);
		// System.out.println("X: "+x+" , S: "+s);
		int[] vals = new int[2];
		int length = Integer.parseInt("" + s.charAt(0));
		// System.out.println("length: "+length);

		vals[0] = Integer.parseInt(s.substring(1, 1 + length));
		vals[1] = Integer.parseInt(s.substring(1 + length));
		// System.out.println("vals1: "+vals[0]+" vals2: "+vals[1]);
		return vals;

	}

	private ArrayList<Integer> getFieldList() {
		ArrayList<Integer> fieldList = new ArrayList<Integer>();
		for (int i = 0; i < field.getNumOfCols(); i++) {
			for (int j = 0; j < field.getNumOfRows(); j++) {
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
		// System.out.println(new Permutations().permuteUnique(1, 3));
	}
}