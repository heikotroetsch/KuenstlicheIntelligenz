package SeSo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;




public class SeSoSolver {
	
	public static void main(String[] args) {
		System.out.println("Solution: ");
		System.out.println(solve());
	}
	
	private static String solve() {
		HashSet<SeSoState> visitedNodes = new HashSet<SeSoState>();
		List<SeSoState> todo = new ArrayList<SeSoState>();
		todo.add(new SeSoState());
		int depth = 0;
		while(!todo.isEmpty()) {
			System.out.println("f1");
			System.out.println(depth++);
			for(int i = 0; i< todo.size(); i++) {
				System.out.println(todo.size());
				SeSoState s = todo.get(i);
				if(s.isSolution()) {
					return s.toString();
				} else {
					List<SeSoState> expandedStates = s.expand();
					todo.addAll(addWithoutDuplicates(visitedNodes,expandedStates));
				}
			}
		}
		return "failed";
	}
	
	private static List<SeSoState>addWithoutDuplicates(HashSet<SeSoState> visitedNodes, List<SeSoState> nodes) {
		List<SeSoState> noDuplicates = new ArrayList<SeSoState>();
		for(SeSoState s : nodes) {
			if(!visitedNodes.contains(s)) {
				noDuplicates.add(s);
				System.out.println("no Duplicate");
			} else {
				System.out.println("Duplicate eliminated");
			}
		}		
		return noDuplicates;
	}
}
