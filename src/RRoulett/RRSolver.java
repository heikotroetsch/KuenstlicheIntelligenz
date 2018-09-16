package RRoulett;
import java.util.LinkedList;
import java.util.List;

/**
 * Diese Klasse sucht nach Loesungen des n-RR Spiels
 */
public class RRSolver {

  public static void main(String[] args) {

    /** Gerade Zahl groesser 1. */
    int size = 12;

    try {
      System.out.print("Solution: ");
      System.out.println(solve(size));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Sucht nach einer Loesung fuer n-Robinson-Roulette mit Hilfe einer Tiefensuche und gibt diese
   * als String zurueck
   */
  private static String solve(int n) throws Exception {
    /*
     * Hier soll eure Loesung implementiert werden. Wichtige Methoden der Klasse RRState:
     * RRState(size) - Konstruktor fuer den Startzustand. isSolution() - Gibt true zurueck, falls
     * das Spiel geloest wurde. toString() - Gibt bisherige Zuege als String zurueck. expand() -
     * Gibt alle moeglichen Folgezustaende als Liste zurueck.
     * 
     * Tipp: Haltet euch eng an den in der Vorlesung vorgestellten Algorithmus und waehlt eine
     * geeignete Datenstruktur fuer die ToDo-Liste.
     */

    List<RRState> todo = new LinkedList<RRState>();
    todo.add(new RRState(n));
    while (true) {
      System.out.println(todo.size());
      if (todo.size() == 0) {
        return "No solution found!";
      } else {
        RRState s = todo.get(todo.size() - 1);
        if (s.isSolution()) {
          return s.toString();
        } else {
          List<RRState> expandedStates = s.expand();
          todo.remove(todo.size() - 1);
          todo.addAll(expandedStates);
        }
      }
    }
  }
}
