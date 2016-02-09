package server;

public class Session {
	
	private String gamename;
	private String creator;
	private String joiner;
	private boolean wordchosen;
	public Session(String gamename, String creator, String joiner, boolean wordchosen) {
		
		super();
		this.gamename = gamename;
		this.creator = creator;
		this.joiner = joiner;
		this.wordchosen = wordchosen;
	}
	
	public Session (String gamename, String creator){
		
		this.gamename = gamename;
		this.creator = creator;
		this.joiner = null;
		this.wordchosen = false;
	}
	
	public boolean isWordchosen() {
		return wordchosen;
	}

	public void setWordchosen(boolean wordchosen) {
		this.wordchosen = wordchosen;
	}

	public String getGamename(){
		return gamename;
	}

	public String getJoiner() {
		return joiner;
	}

	public void setJoiner(String joiner) {
		this.joiner = joiner;
	}

	public String getCreator() {
		return creator;
	}
	
	

}
