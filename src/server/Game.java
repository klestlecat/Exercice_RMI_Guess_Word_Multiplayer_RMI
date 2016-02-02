package server;

public class Game {

	private String gamename;
	private String word;
	private String winner;
	private String loser;
	private String tscreator;
	private String tsjoiner;
	
	
	public Game(String gamename, String word, String winner, String loser, String tscreator, String tsjoiner) {
		
		super();
		this.gamename = gamename;
		this.word = word;
		this.winner = winner;
		this.loser = loser;
		this.tscreator = tscreator;
		this.tsjoiner = tsjoiner;
	}


	public Game(String gamename, String word) {
		
		super();
		this.gamename = gamename;
		this.word = word;
		this.winner = null;
		this.loser = null;
		this.tscreator = "";
		this.tsjoiner = "";
	}


	public String getWinner() {
		return winner;
	}


	public void setWinner(String winner) {
		this.winner = winner;
	}


	public String getLoser() {
		return loser;
	}


	public void setLoser(String loser) {
		this.loser = loser;
	}


	public String getTscreator() {
		return tscreator;
	}


	public void setTscreator(String tscreator) {
		this.tscreator = tscreator;
	}


	public String getTsjoiner() {
		return tsjoiner;
	}


	public void setTsjoiner(String tsjoiner) {
		this.tsjoiner = tsjoiner;
	}
	
	
	
}
