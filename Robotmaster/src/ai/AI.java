package ai;

import java.util.List;

import game.Move;

public abstract class AI {

	/**
	 * @return The name of your AI which must not change during the game.
	 */
	public abstract String getName();

	/**
	 * Initialize your AI.
	 * 
	 * @param hand   The starting hand of your AI.
	 * @param middle The card in the middle of the board.
	 */
	public abstract void initialize(List<String> hand, int middle);

	/**
	 * Most important method: Here you get informed about the opponent's moves and
	 * select your own move.
	 * 
	 * @param move Last move of the opponent. NULL if the game just starts and we
	 *             are the starting player!
	 * @return Move to be played.
	 */
	public abstract Move action(Move move);

}