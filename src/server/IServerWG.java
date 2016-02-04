package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServerWG extends Remote {
	
	void sessioncreation (String username, String gamename) throws RemoteException;

	String showSession() throws RemoteException;
	
	void joinerconnection (String username, String gamename) throws RemoteException;
	
	void gameshutdown (String gamename) throws RemoteException;
	
	void checkplayerconnection (String gamename) throws RemoteException, InterruptedException;

	String getWord(String gamename) throws RemoteException;
	
	String[] getHelp(String word) throws RemoteException;
	
	boolean gameend(String gamename) throws RemoteException;
	
	void wordTimestamp(String gamename, String username, String word) throws RemoteException;
	
	boolean checkwin (String gamename, String username) throws RemoteException, InterruptedException;

	boolean checkTie(String gamename) throws RemoteException;
	
}