package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

import ai.*;
import game.Move;


/**
 *	An dieser Klasse soll ausser an der gekennzeichneten Stelle nichts veraendert werden.
 */
public class Main {
	private static final String SERVER = "http://robotmaster.informatik.uni-mannheim.de/";
	private static String name;

	public static void main(String[] args) throws Exception {
		
		AI ai = new MCTSAI(); // TODO Hier Instanz eurer KI erzeugen.
		
		name = ai.getName();
		String id = null;
		try {
			System.out.println("Name: " + name);
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			System.out.print("Spielnummer eingeben (RETURN für neues Spiel): ");
			id = br.readLine();
			
			
		} catch (NumberFormatException e) {
			System.err.println("Invalid number. Leave empty to create server or ID to join ID and client name.");
		}
		
		if (id.isEmpty()) {
			id = createGame();
			System.out.println("ID: " + id);
		} else {
			joinGame(id);
		}

		Play p = new Play(id, ai);
		p.start();
	}

	public static String createGame() throws Exception {
		String id = load("createGame/" + name);
		return id;
	}

	public static String joinGame(String gameID) throws Exception {
		String join = load("joinGame/" + gameID + "/" + name);
		return join;
	}

	public static String getOpenGames() throws Exception {
		String games = load("openGames");
		return games;
	}

	public static String load(String url) throws Exception {
		URL ts = new URL(SERVER + "/" + url);
		URLConnection con = ts.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String input;
		StringBuffer sb = new StringBuffer();
		while ((input = in.readLine()) != null) {
			sb.append(input);
		}
		in.close();
		return sb.toString();
	}
}

class Play extends Thread {
	AI ai;
	String gameid = "";

	public Play(String id, AI ai) {
		this.ai = ai;
		this.gameid = id;
	}

	public void run() {
		try {
			while (true) {
				String s;
				s = Main.load("check/" + gameid + "/" + ai.getName());
				if (s.equals("-3") || s.equals("-4")) {
					// not your turn or waiting for opponent
					Thread.sleep(1000);
				} else if (s.equals("0")) {
					// get your hand
					String hand = Main.load("getHand/" + gameid + "/" + ai.getName());
					String[] cut = hand.split("_");
					int middle = Integer.parseInt(cut[cut.length - 1]);
					ArrayList<String> playerhand = new ArrayList<String>(Arrays.asList(cut));
					playerhand.remove((cut.length - 1));
					ai.initialize(playerhand, middle);
				} else if (s.equals("-1")) {
					// your turn
					move("");
				} else if (s.equals("-2")) {
					// game ended
					System.out.println(Main.load("getWinner/" + gameid));
					System.out.println("Game has ended! Shutting down.");
					System.exit(0);
				} else {
					// s is your opponents last move
					move(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void move(String opponentsMove) throws Exception {
		// here you decide on the next move
		Move selected;
		if (!opponentsMove.equals("")) {
			String[] split = opponentsMove.split("_");
			selected = ai.action(
					new Move(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[0])));
		} else {
			selected = ai.action(null);
		}

		// this sends the move to the server, format: move/gameid/clientname/card/x/y
		if (selected == null) {
			Main.load("getWinner/" + gameid);
		} else {
			Main.load("move/" + gameid + "/" + ai.getName() + "/" + selected.getV() + "/" + selected.getX() + "/"
					+ selected.getY());
		}
	}

}