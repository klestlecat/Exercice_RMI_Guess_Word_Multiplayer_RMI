package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Main {

	public static void main(String[] args) {
		
		try {
			String dbName = "gameWords.db";
			
			LocateRegistry.createRegistry(1099);
			IServerWG server = new ServerWG(dbName);
			Naming.rebind("wordGame", server);
			System.out.println("Waiting client...");
			
		} catch (RemoteException | MalformedURLException e) {
			
			System.out.println("Server failed to launch");
			e.printStackTrace();
		}
	}


}
