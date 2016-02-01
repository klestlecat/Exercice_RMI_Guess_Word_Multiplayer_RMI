package server;

public class Game {

	private String gamename;
	private String word;
	private String winner;
	private String loser;
	
	
	public Game(String gamename, String word, String winner, String loser) {
		
		super();
		this.gamename = gamename;
		this.word = word;
		this.winner = winner;
		this.loser = loser;
	}


	public Game(String gamename, String word) {
		
		super();
		this.gamename = gamename;
		this.word = word;
		this.winner = null;
		this.loser = null;
	}
	
	
	
}
