package game;

public class Move {

	private int x;
	private int y;
	private int v;

	public Move(int x, int y, int v) {
		this.x = x;
		this.y = y;
		this.v = v;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/**
	 * @return The value at the position (x,y).
	 */
	public int getV() {
		return v;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + v + ")";
	}

	@Override
	public boolean equals(Object o) {
		Move m = (Move) o;
		return (x == m.x) && (y == m.y) && (v == m.v);
	}

}
