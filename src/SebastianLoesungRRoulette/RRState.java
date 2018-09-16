package SebastianLoesungRRoulette;
import java.util.*;

/* Diese Klasse repraesentiert einen Zustand des n-RR Spiels
 *
 * 04.09.2009, L. Mainka, D. Gritzner
 */
public class RRState {
	private boolean[] inner, outer; // die beiden Scheiben
	// inner: innere Scheibe. inner[i] == true bedeutet "auf Position i befindet sich eine Kugel"
	// outer: Aeussere Scheibe. outer[i] == true bedeutet "Position i ist bereits belegt"
	
	private RRState parent; // Vaterzustand (fuer Loesungsausgabe)
	private int lastMove; // zuletzt durchgefuehrte Rechtsrotation (fuer Loesungsausgabe)
	private boolean valid; // fuer Bequemlichkeit (beim Bestimmen der Kinder)

	// erzeugt ein Anfangszustand fuer n-RR (n == size)
	public RRState(int size) throws Exception {
		if((size % 2 != 0) || (size < 2))
			throw new Exception("Invalid size (must be even and greater than or equal to 2)");
		
		lastMove = 0;
		valid = true;
		parent = null;
			
		int center = size / 2;
		inner = new boolean[size];
		outer = new boolean[size];
		
		for(int i = 0; i < size; i++) {
			inner[i] = (i == center ? false : true); // inner[center] keine Kugel, da innerer Pfeil
			outer[i] = (i == 0 ? true : false);	// outer[0] belegt, da aeusserer Pfeil
		}
	}
	
	// erzeugt einen Zustand, bei dem ausgehend vom Zustand other eine Rechtsrotation um rot durchgefuehrt wurde
	private RRState(RRState other, int rot) {
		lastMove = rot;
		parent = other;

		int size = other.inner.length;
		inner = new boolean[size];
		outer = new boolean[size];

		/* hier findet gleichzeitig die Rotation und das Umsetzen der Kugel nach aussen statt:
		 * inner[(i+rot)%size] =                 <-- Rotation
		 * (i == 0 ? false : other.inner[i])     <-- Kugel innen rausnehmen (Rest uebernehmen)
		 * (i == rot ? true : other.outer[i])    <-- Kugel außen setzen (Rest uebernehmen)
		 */
		for(int i = 0; i < size; i++) {
			inner[(i+rot)%size] = (i == 0 ? false : other.inner[i]);
			outer[i] = (i == rot ? true : other.outer[i]);
		}
		
		// Pruefen ob der Zug gueltig war
		if((inner[0] || countInner() == 0) && !other.outer[rot])
			valid = true;
		else
			valid = false;
	}
	
	// zaehlt wie viele Kugeln sich noch auf der inneren Scheibe befinden
	private int countInner() {
		int count = 0;
		for(int i = 0; i < inner.length; i++)
			if(inner[i])
				count += 1;
		return count;
	}
	
	// prueft ob der vorliegende Zustand ein Zielzustand ist
	public boolean isSolution() {
		return countInner() == 0;
	}
	
	// ermittelt alle Kinder des vorliegenden Zustands (nur gueltige Zuege werden beruecksichtigt)
	public List<RRState> expand() {
		ArrayList<RRState> children = new ArrayList<RRState>();
		for(int i = 1; i < inner.length; i++) {
			RRState temp = new RRState(this, i);
			if(temp.valid)
				children.add(temp);
		}
		return children;
	}

	// gibt die Zuege/Handlungen aus, die zum vorliegenden Zustand fuehrten (bei Zielzustaenden ist dies eine Loesung)
	public String toString() {
		if(parent == null)
			return "";
	
		StringBuilder sb = new StringBuilder();
		RRState cur = this;
		while(cur.parent != null) {
			sb.append(cur.lastMove);
			sb.append('-');
			cur = cur.parent;
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
}
