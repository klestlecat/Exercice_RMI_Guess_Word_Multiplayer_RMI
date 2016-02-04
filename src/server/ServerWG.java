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
		db.addSession(username, " ", gamename);
		
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
	public void checkplayerconnection(String gamename) throws RemoteException, InterruptedException {
		
		while (sessionarray.get(gamename).getJoiner() == null){
			Thread.sleep(30);
		}
		
	}

	@Override
	public void joinerconnection(String username, String gamename) throws RemoteException {
		
		sessionarray.get(gamename).setJoiner(username);
		db.setJoinerSession(gamename, username);
		
	}

	@Override
	public boolean gameend(String gamename) throws RemoteException {
		
		if (gamearray.get(gamename) != null){
			setWinner(gamename);
			addGameDB(gamename);
			gamearray.remove(gamename);
			return true;
			
		}
		return false;
	}
	
	@Override
	public void wordTimestamp(String gamename, String username, String word) throws RemoteException {
		String timestamp = TimeStamp();
		
		if (word.equals("")){
			timestamp = "0000";
		}
				
		else if (username.equals(sessionarray.get(gamename).getCreator())){
			gamearray.get(gamename).setTscreator(timestamp);
		}
		
		else if (username.equals(sessionarray.get(gamename).getJoiner())){
			gamearray.get(gamename).setTsjoiner(timestamp);
		}
	}

	@Override
	public boolean checkwin(String gamename, String username) throws RemoteException, InterruptedException {
		
		while (gamearray.get(gamename).getTscreator().equals("") && gamearray.get(gamename).getTsjoiner().equals("")){
			Thread.sleep(30);
		}
		
		if (gamearray.get(gamename).getWinner().equals(username)){
			return true;
		}
		
		return false;
	}
	
	public void setWinner(String gamename){
		String creatorts = gamearray.get(gamename).getTscreator();
		String joinerts = gamearray.get(gamename).getTsjoiner();
		
		if (creatorts.equals("0000") && joinerts.equals("0000")){
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
	public boolean checkTie (String gamename){
		
		if (gamearray.get(gamename).getTscreator().equals("0000") && gamearray.get(gamename).getTsjoiner().equals("0000")){
			return true;
		}
		
		return false;
	}
	
	public void addGameDB(String gamename){
		db.addGame(gamearray.get(gamename).getWinner(), gamearray.get(gamename).getLoser(), 
				gamearray.get(gamename).getWord(), gamename, gamearray.get(gamename).getTie());
	}
	
	public String TimeStamp (){
		
		Timestamp timestamp = new Timestamp((new Date()).getTime());
		
		return timestamp.toString();
	}

	
}
