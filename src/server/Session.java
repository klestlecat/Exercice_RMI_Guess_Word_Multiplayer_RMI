package server;

public class Session {
	
	private String gamename;
	private String creator;
	private String joiner;
	public Session(String gamename, String creator, String joiner) {
		
		super();
		this.gamename = gamename;
		this.creator = creator;
		this.joiner = joiner;
	}
	
	public Session (String gamename, String creator){
		
		this.gamename = gamename;
		this.creator = creator;
		this.joiner = null;
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
