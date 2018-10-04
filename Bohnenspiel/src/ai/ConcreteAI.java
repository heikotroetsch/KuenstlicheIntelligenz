package ai;

import java.util.Random;

import bohnenspiel.Bohnenspiel;

public class ConcreteAI extends AI {

Random rand = new Random();
Bohnenspiel spiel = new Bohnenspiel();
	
	public int getMove(int enemyIndex) {
		int index = 0;
		// have to choose the first move
		if (enemyIndex == -1) {
			index = rand.nextInt(6) + 1;
		}
		// enemy acted and i have to react
		else if (enemyIndex > 0 && enemyIndex <= 6) {
			spiel.makeMove(enemyIndex, false);
			index = rand.nextInt(6) + 7;
		}
		else if (enemyIndex > 6 && enemyIndex <= 12) {
			spiel.makeMove(enemyIndex, false);
			index = rand.nextInt(6) + 1;
		}
		spiel.makeMove(index, true);
		return index;
	}

	@Override
	public String getName() {
		return "Concrete AI";
	}
}
