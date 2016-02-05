package server;

import java.sql.*;

public class WordsDatabase {
	
	private final String sqlPlayerTable = "CREATE TABLE IF NOT EXISTS PLAYERS " +
            "(ID 	INTEGER		PRIMARY KEY	AUTOINCREMENT," +
            " PLAYER	CHAR(50)	NOT NULL)";
	
	private final String sqlGameTable = "CREATE TABLE IF NOT EXISTS GAME " +
            "(ID 	INTEGER		PRIMARY KEY	AUTOINCREMENT," +
            " IDSESSION	INTEGER	NOT NULL," +
			" IDWORD	INTEGER	NOT NULL," +
			" IDWINNER	INTEGER	NOT NULL," +
			" IDLOSER	INTEGER	NOT NULL," +
			" STATUS CHAR(50)	NOT NULL)";
	
	private final String sqlSessionTable = "CREATE TABLE IF NOT EXISTS SESSIONS " +
			"(ID	INTEGER	PRIMARY KEY AUTOINCREMENT," +
			" IDCREATOR	INTEGER	NOT NULL," +
			" IDJOINER	INTEGER	NOT NULL," +
			" SESSION	TEXT	NOT NULL)";

	private final String sqlWordTable = "CREATE TABLE IF NOT EXISTS WORDS " +
            "(ID 	INTEGER		PRIMARY KEY	AUTOINCREMENT," +
            " WORD	CHAR(50)	NOT NULL)";
	
	private final String sqlHelpTable = "CREATE TABLE IF NOT EXISTS HELP " +
            "(ID 	INTEGER	PRIMARY KEY	AUTOINCREMENT," +
            " WORD	INTEGER	NOT NULL," +
            " LINE	TEXT	NOT NULL)";
	
	private Connection c = null;
	private String dbName;
	
	public WordsDatabase(String dbName){
		
		this.dbName = dbName;
		this.createTable(sqlPlayerTable, "PLAYER");
		this.createTable(sqlGameTable, "GAME");
		this.createTable(sqlSessionTable, "SESSIONS");
		this.createTable(sqlWordTable, "WORDS");
		this.createTable(sqlHelpTable, "HELP");
	}
	
	private void connect(){
		
	    try {
	    	
			Class.forName("org.sqlite.JDBC");
			this.c = DriverManager.getConnection("jdbc:sqlite:" + this.dbName);
			System.out.println("Opened database successfully");
	      
	    } catch (Exception e) {
	      
	    	System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    }
	}
	
	private void disconnect(){
		
	    try {
	    	
			this.c.close();
	      
	    } catch (Exception e) {
	      
	    	System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    }
	}
	
	private boolean createTable(String tableSQL, String tableName){
		
	   Statement stmt = null;
	   
	   try{
		   
		   this.connect();

		   stmt = this.c.createStatement();

		   stmt.executeUpdate(tableSQL);
		   stmt.close();
		   this.disconnect();
		   
		   System.out.println("Table " + tableName + " created successfully");
		   
		   return true;
		   
	   } catch (Exception e) {
		   
		   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	   }
	   
	   return false;
	}
	
	public boolean addPlayer (String username){
		Statement stmt = null;
		String sqlPlayer = "INSERT INTO PLAYERS (PLAYER)" +
							"VALUES ('" + username + "');";
		
		try {
			this.connect();
			
			stmt = this.c.createStatement();
			
			stmt.executeUpdate(sqlPlayer);
			stmt.close();
			this.disconnect();
			
			System.out.println("Player" + username + "added to table PLAYER successfully" );
			
			return true;
			
		} catch (Exception e) {
			
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		
		return false;
	}
	
	
	// il est possible de faire une seule methode getID qui recuperera dans la table l'ID?
	
	public int getPlayerID(String username){
		
		Statement stmt = null;
		String getPlayerID = "SELECT ID FROM PLAYERS WHERE PLAYER='" + username + "';";
		int playerid = -1;
		
		try{
			this.connect();
			
			stmt = this.c.createStatement();
			
			ResultSet rsc = stmt.executeQuery(getPlayerID);
			
			if (rsc.next()){
				playerid = rsc.getInt("ID");
				rsc.close();
			}
			
			else {
				rsc.close();
				System.out.println(username +"'s ID not found");
			}
			
			stmt.close();
			this.disconnect();
			
		} catch (SQLException e) {
			
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		
		return playerid;
	}
	
	public int getSessionID (String session){
		
		Statement stmt = null;
		String getSessionID = "SELECT ID FROM SESSIONS WHERE SESSION='" + session + "';";
		int sessionid = -1;
		
		try{
			
			this.connect();
			
			stmt = this.c.createStatement();
			
			ResultSet rss = stmt.executeQuery(getSessionID);
			
			if (rss.next()){
				sessionid = rss.getInt("ID");
				rss.close();
			}
			
			else {
				rss.close();
				System.out.println(session + "'s ID not found");
			}
			stmt.close();
			this.disconnect();
			
		} catch (SQLException e) {
			
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		
		return sessionid;
		
	}
	
	
	public int getwordID (String word){
		
		Statement stmt = null;
		String getWordID = "SELECT ID FROM WORLDS WHERE WORD='" + word + "';";
		int wordID = -1;
		
		try{
			
			this.connect();
			
			stmt = this.c.createStatement();
			
			ResultSet rss = stmt.executeQuery(getWordID);
			
			if (rss.next()){
				wordID = rss.getInt("ID");
				rss.close();
			}
			
			else {
				rss.close();
				System.out.println(word + "'s ID not found");
			}
			stmt.close();
			this.disconnect();
			
		} catch (SQLException e) {
			
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		
		return wordID;
		
	}
	
	public boolean addSession (String creator, String gamename){
		
		Statement stmt = null;
		int cid = getPlayerID(creator);
		int jid = 5;
		
		try{
			this.connect();
			
			stmt = this.c.createStatement();
			
			
			String sqlSession = "INSERT INTO SESSIONS (IDCREATOR, IDJOINER, SESSION) " +
					"VALUES (" + cid + ", '" + jid + "', '" + gamename + "');";
			
			System.out.println(sqlSession);
/*			"INSERT INTO HELP (WORD, LINE) " +
				"VALUES (" + id + ", '" + help[i] + "');";
			
*/			stmt.executeUpdate(sqlSession);
			
			stmt.close();
			this.disconnect();
			
			System.out.println("Information added successfully");
			   
			return true;
			   
		   } catch (Exception e) {
			   
			   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			   System.out.println("addsession error");
		   }
		   
		return false;
	}
	
	public boolean setJoinerSession (String gamename, String joiner){
		Statement stmt = null;
		
		int joinerid = getPlayerID(joiner);
		
		String sqlJoinerUpdate = "UPDATE SESSIONS"
				+ "SET IDJOINER ='" + joinerid + "'"
				+ "WHERE SESSION ='" + gamename + "';";
		
		try {
			this.connect();
			
			stmt = this.c.createStatement();
			
			stmt.executeUpdate(sqlJoinerUpdate);
			
			stmt.close();
			this.disconnect();
			   
			System.out.println("Information added successfully");
			   
			return true;
			
		} catch (SQLException e) {
			
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		
		return false;
	}
	
	public boolean addGame (String winner, String loser, String word, String gamename, String tie){
		Statement stmt = null;
		int wid, lid, wordid, gid; 
		
		wid = getPlayerID(winner);
		lid = getPlayerID(loser);
		wordid = getwordID(word);
		gid = getSessionID(gamename);
		
		try{
			this.connect();
			
			stmt = this.c.createStatement();
			
			
			String sqlSession = "INSERT INTO GAME (IDSESSION, IDWORD, IDWINNER, IDLOSER, STATUS)" +
					"VALUES ('" + gid + ", '" + wordid + ", '" + wid +", '" + lid +", '" + tie + "');";
			System.out.println(sqlSession);
			
			stmt.executeUpdate(sqlSession);
			
			stmt.close();
			   this.disconnect();
			   
			   System.out.println("Information added successfully");
			   
			   return true;
			   
		   } catch (Exception e) {
			   
			   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		   }
		   
		   return false;
	}
	
	
	
	public boolean addRow(String word, String[] help){
	
	   Statement stmt = null;
	   String sqlWord = "INSERT INTO WORDS (WORD) " +
	            		"VALUES ('" + word + "');";
	   
	   String getWordID = "SELECT ID FROM WORDS WHERE WORD='" + word + "';";
	   
	   try{
		   
		   this.connect();

		   stmt = this.c.createStatement();

		   stmt.executeUpdate(sqlWord);
		   
		   ResultSet rs = stmt.executeQuery(getWordID);
		   int id;
		   if(rs.next()){
			   id = rs.getInt("ID");
			   rs.close();
		   }
		   else{
			   rs.close();
			   stmt.close();
			   this.disconnect();
			   return false;
		   }
		   
		   String[] sqlHelp = new String[help.length];
		   
		   for (int i = 0; i < sqlHelp.length; i++){
			   
			   sqlHelp[i] = "INSERT INTO HELP (WORD, LINE) " +
	           				"VALUES (" + id + ", '" + help[i] + "');";
			   
			   stmt.executeUpdate(sqlHelp[i]);
		   }
		   
		   stmt.close();
		   this.disconnect();
		   
		   System.out.println("Information added successfully");
		   
		   return true;
		   
	   } catch (Exception e) {
		   
		   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	   }
	   
	   return false;
	}
	
	public String getWord(){
		
	   Statement stmt = null;
	   String query = "SELECT * FROM WORDS ORDER BY RANDOM() LIMIT 1";
	   
	   try{
		   
		   this.connect();

		   stmt = this.c.createStatement();
		   ResultSet rs = stmt.executeQuery(query);
		   
		   String word;
		   if(rs.next()){
			   word = rs.getString("WORD");
			   rs.close();
			   stmt.close();
			   this.disconnect();
			   return word;
		   }
		   else{
			   rs.close();
			   stmt.close();
			   this.disconnect();
			   return "";
		   }
		   
	   } catch (Exception e) {
		   
		   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	   }
	   
	   return "";
	}
	
	public String[] getHelp(String word){
		
	   Statement stmt = null;
	   
	   String getWordID = "SELECT ID FROM WORDS WHERE WORD='" + word + "';";
	   String[] help;
	   
	   try{
		   
		   this.connect();

		   stmt = this.c.createStatement();
		   ResultSet rs = stmt.executeQuery(getWordID);
		   
		   int id;
		   if(rs.next()){
			   id = rs.getInt("ID");
			   rs.close();
		   }
		   else{
			   rs.close();
			   stmt.close();
			   this.disconnect();
			   return null;
		   }
		   
		   rs = stmt.executeQuery("SELECT COUNT() FROM HELP WHERE WORD='" + id + "';");
		   int num = rs.getInt("COUNT()");
		   
		   String getHelpLines = "SELECT LINE FROM HELP WHERE WORD='" + id + "';";
		   rs = stmt.executeQuery(getHelpLines);
		   
		   help = new String[num];
		   for (int i = 0; i < help.length; i++){
			   
			   rs.next();
			   help[i] = rs.getString("LINE");
		   }
		   
		   rs.close();
		   stmt.close();
		   this.disconnect();
		   
		   return help;
		   
	   } catch (Exception e) {
		   
		   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	   }
	   
	   return null;
	}
}
