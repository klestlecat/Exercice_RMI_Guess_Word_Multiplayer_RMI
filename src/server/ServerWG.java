package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
		
		return db.getWord();
	}

	@Override
	public String[] getHelp(String word) throws RemoteException {
		
		return db.getHelp(word);
	}

	@Override
	public void gamecreation(String username, String gamename) throws RemoteException {
		
		db.addPlayer(username);
		sessionarray.put(gamename, new Session(gamename, username));
		db.addSession(username, " ", gamename);
		
	}

	@Override
	public void gameshutdown(String gamename) throws RemoteException {
		
		sessionarray.remove(gamename);
		
	}

	@Override
	public boolean checkplayerconnection(String gamename) throws RemoteException {
		
		if (sessionarray.get(gamename).getJoiner() == null){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public void joinerconnection(String username, String gamename) throws RemoteException {
		
		sessionarray.get(gamename).setJoiner(username);
		db.setJoinerSession(gamename, username);
		
	}

	@Override
	public boolean chackgameend(String gamename) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

}
