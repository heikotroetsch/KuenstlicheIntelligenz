package SeSo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;




public class SeSoSolver {
	
	public static void main(String[] args) {
		System.out.println("Solution: ");
		long start = System.currentTimeMillis();
		System.out.println(solve());
		System.out.println("Time: "+(System.currentTimeMillis()-start)+"ms");
	}
	
	private static String solve() {
		HashSet<SeSoState> visitedNodes = new HashSet<SeSoState>();
		List<SeSoState> todo = new ArrayList<SeSoState>();
		todo.add(new SeSoState());
		while(!todo.isEmpty()) {
				SeSoState s = todo.get(0);
				if(!visitedNodes.contains(s)) {
					if(s.isSolution()) {
						return s.toString(); 
					} else {
						visitedNodes.add(s);
						todo.addAll(s.expand());
						todo.remove(0);
					}
				}else {
					todo.remove(0);
				}
			}
		System.out.println(visitedNodes.size());
		return "failed";
	}
}