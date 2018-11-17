import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SpringerProblemSolver {
	int generationOneSize;
	int nrOfGenerations;
	double rateOfMutation;
	double survivalRate;
	int numberOfChildren;
	int characteristics;

	public SpringerProblemSolver(int startGenSize, int nrGens, double mutRate, double survRate, int nrChil) {
		this.generationOneSize = startGenSize;
		this.nrOfGenerations = nrGens;
		this.rateOfMutation = mutRate;
		this.survivalRate = survRate;
		this.numberOfChildren = nrChil;


	}

	public static void main(String[] args) {
		SpringerProblemSolver s1 = new SpringerProblemSolver(100000, 100, 30, 0.0999, 10);
		System.out.println(s1.solve());
	}

	private SpringerProblemState solve() {

		ArrayList<SpringerProblemState> currentGen = new ArrayList<SpringerProblemState>();
		for (int i = 0; i < this.generationOneSize; i++) {
			currentGen.add(new SpringerProblemState());
		}
		for (int i = 0; i < nrOfGenerations; i++) {
			System.out.println("Generation: " + i + " Population: " + currentGen.size());
			PriorityQueue<SpringerProblemState> nextGen = new PriorityQueue<SpringerProblemState>(
					new Comparator<SpringerProblemState>() {
						@Override
						public int compare(SpringerProblemState s1, SpringerProblemState s2) {
							return s2.evaluateState() - s1.evaluateState();
						}
					});
			for (SpringerProblemState sp : currentGen) {
				nextGen.addAll(sp.evolve(this.rateOfMutation, currentGen.size()!=1?currentGen.get((int) (Math.random() * currentGen.size())):new SpringerProblemState(),
						this.numberOfChildren));
			}
			System.out.println("Next Gen breeded. Size: " + nextGen.size());
			currentGen.clear();
			while(currentGen.size() < this.survivalRate * nextGen.size()) {
				currentGen.add(nextGen.poll());
			}

		}
		currentGen.sort(new Comparator<SpringerProblemState>() {
			@Override
			public int compare(SpringerProblemState s1, SpringerProblemState s2) {
				return s2.evaluateState() - s1.evaluateState();
			}
		});
		return currentGen.get(0);

	}
}
