package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.sql.*;
import java.util.TreeMap;


public class ServerWG extends UnicastRemoteObject implements IServerWG {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private WordsDatabase db;
	private static TreeMap<String, Session> sessionarray = new TreeMap <String, Session>();
	private static TreeMap<String, Game> gamearray = new TreeMap<String, Game>();
	
	public ServerWG(String dbName) throws RemoteException{
		
		super();
		this.db = new WordsDatabase(dbName);
	}

	@Override
	public String getWord(String gamename) throws RemoteException {
		
		String word = db.getWord();
		gamearray.put(gamename, new Game(gamename, word));
		sessionarray.get(gamename).setWordchosen(true);
		
		return word;
	}

	@Override
	public String[] getHelp(String word) throws RemoteException {
		
		return db.getHelp(word);
	}

	@Override
	public void sessioncreation(String username, String gamename) throws RemoteException {
		
		db.addPlayer(username);
		sessionarray.put(gamename, new Session(gamename, username));
		db.addSession(username, gamename);
		
	}
	
	@Override
	public String showSession (){
		
		String line = "";
		
		for (String gamename : sessionarray.keySet()){
			if (sessionarray.get(gamename).getJoiner() == null){
				line += sessionarray.get(gamename).getGamename() + "\n";
			}
		}
		
		return line;
	}

	@Override
	public void gameshutdown(String gamename) throws RemoteException {
		
		sessionarray.remove(gamename);
		
	}

	@Override
	public boolean checkplayerconnection(String gamename) throws RemoteException, InterruptedException {
		
		if (sessionarray.get(gamename).getJoiner() == null){
			return false;
		}
		else{
			return true;
		}
	}

	@Override
	public void joinerconnection(String username, String gamename) throws RemoteException {
		
		db.addPlayer(username);
		sessionarray.get(gamename).setJoiner(username);
		db.setJoinerSession(gamename, username);
		
	}

	@Override
	public boolean gameend(String gamename) throws RemoteException {
		
		if (gamearray.get(gamename) != null){
			addGameDB(gamename);
			gamearray.remove(gamename);
			sessionarray.get(gamename).setWordchosen(false);
			return true;
		}
		return false;
	}
	
	@Override
	public void wordTimestamp(String gamename, String username, String word) throws RemoteException {
		String timestamp = TimeStamp();
		
		if (word.equals("")){
			timestamp = "zzzz";
		}
				
		if (username.equals(sessionarray.get(gamename).getCreator())){
			gamearray.get(gamename).setTscreator(timestamp);
		}
		
		else if (username.equals(sessionarray.get(gamename).getJoiner())){
			gamearray.get(gamename).setTsjoiner(timestamp);
		}
	}

	@Override
	public boolean checkwin(String gamename, String username) throws RemoteException, InterruptedException {
		
		while (gamearray.get(gamename).getTscreator().equals("") || gamearray.get(gamename).getTsjoiner().equals("")){
			Thread.sleep(1000);
		}
		
		if (gamearray.get(gamename).getWinner() == null){
			setWinner(gamename);
		}
		
		if (gamearray.get(gamename).getWinner().equals(username)){
			return true;
		}
		
		return false;
	}
	
	public void setWinner(String gamename){
		String creatorts = gamearray.get(gamename).getTscreator();
		String joinerts = gamearray.get(gamename).getTsjoiner();
		
		if (creatorts.equals("zzzz") && joinerts.equals("zzzz")){
			gamearray.get(gamename).setWinner(sessionarray.get(gamename).getCreator());
			gamearray.get(gamename).setLoser(sessionarray.get(gamename).getJoiner());
			gamearray.get(gamename).setTie("tie");
		}
		
		else if (creatorts.compareTo(joinerts) < 0){
			gamearray.get(gamename).setWinner(sessionarray.get(gamename).getCreator());
			gamearray.get(gamename).setLoser(sessionarray.get(gamename).getJoiner());
			gamearray.get(gamename).setTie("not tie");
		}
		
		else if (creatorts.compareTo(joinerts) > 0){
			gamearray.get(gamename).setWinner(sessionarray.get(gamename).getJoiner());
			gamearray.get(gamename).setLoser(sessionarray.get(gamename).getCreator());
			gamearray.get(gamename).setTie("not tie");
		}
	}
	
	@Override
	public boolean checkTie (String gamename) throws InterruptedException{
		
		while (gamearray.get(gamename).getTscreator().equals("") || gamearray.get(gamename).getTsjoiner().equals("")){
			Thread.sleep(1000);
		}
		
		if (gamearray.get(gamename).getWinner() == null){
			setWinner(gamename);
		}
		
		if (gamearray.get(gamename).getTscreator().equals("zzzz") && gamearray.get(gamename).getTsjoiner().equals("zzzz")){
			return true;
		}
		
		else if (gamearray.get(gamename).getWinner() == null){
			return true;
		}
		
		return false;
	}
	
	public void addGameDB(String gamename){
		
		System.out.println("\n " + gamearray.get(gamename).getTie());
		db.addGame(gamearray.get(gamename).getWinner(), gamearray.get(gamename).getLoser(), 
				gamearray.get(gamename).getWord(), gamename, gamearray.get(gamename).getTie());
	}
	
	public String TimeStamp (){
		
		Timestamp timestamp = new Timestamp((new Date()).getTime());
		
		return timestamp.toString();
	}

	@Override
	public String joinergetWord(String gamename) throws RemoteException, InterruptedException {
		
		while (sessionarray.get(gamename).isWordchosen() == false){
			Thread.sleep(30);
		}
		
		return gamearray.get(gamename).getWord();
	}
	
	@Override
	public void waitGameend(String gamename) throws InterruptedException{
		
		while (gamearray.get(gamename).getTscreator().equals("") || gamearray.get(gamename).getTsjoiner().equals("")){
			Thread.sleep(30);
		}
	}
}
