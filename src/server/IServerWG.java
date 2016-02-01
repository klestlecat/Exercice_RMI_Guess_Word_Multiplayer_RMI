package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServerWG extends Remote {
	
	void gamecreation (String username, String gamename) throws RemoteException;
	
	void joinerconnection (String username, String gamename) throws RemoteException;
	
	void gameshutdown (String gamename) throws RemoteException;
	
	boolean checkplayerconnection (String gamename) throws RemoteException;

	String getWord(String gamename) throws RemoteException;
	
	String[] getHelp(String word) throws RemoteException;
	
	boolean chackgameend(String gamename) throws RemoteException;
}