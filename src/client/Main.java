package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import server.IServerWG;

public class Main {

	public static String getFreshGuess(String word){
		
		String freshGuess = "";
		
		for (int i = 0; i < word.length(); i++)
			freshGuess += "_ ";
		
		return freshGuess;
	}
	
	public static String constructGuessWord(String guess, String word, String letter){
		
		String newGuess = "";
		
		for (int i = 0; i < word.length(); i++){
			
			if (guess.charAt(2 * i) == '_'){
				
				if (word.toLowerCase().charAt(i) == letter.toLowerCase().charAt(0)){
					
					newGuess += letter.toLowerCase() + " "; 
				}
				else
					newGuess += "_ ";
			}
			else
				newGuess += guess.toLowerCase().charAt(2 * i) + " ";
		}
		
		return newGuess;
	}
	
	public static void main(String[] args) {
		
		IServerWG server;
		
		String word;
		String errorLetters;
		String guess;
		String aux;
		String[] help;
		
		String gamename;
		String username;
		
		int errors, modulo, count;
		boolean found;
		
		try {
			
			Registry registry = LocateRegistry.getRegistry(1099);
			server = (IServerWG) registry.lookup("wordGame");
			
			Scanner in = new Scanner(System.in);
			String input;
			
			//username input
			System.out.println("Please enter your username");
			username = in.nextLine();
			
			while (true){
				
				help = null;
				System.out.println("Choose \"n\" for New Game \"j\" for Join Game (\"exit\" to quit): ");
				
				input = in.nextLine();
				
				if(input.toLowerCase().equals("n")){
					
					//gamename input
					System.out.println("Please enter gamename");
					gamename = in.nextLine();
					server.sessioncreation(username, gamename);

					word = server.getWord(gamename);
					
					System.out.println("waiting for a second player to join");
					while (true){
						try {
							Thread.sleep(30);
							if(server.checkplayerconnection(gamename)){
								break;
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					
					while (true){
						
						if (word.equals(""))
							System.out.println("Internal Server Error");
						else
							help = server.getHelp(word);
							
						if (help == null)
							System.out.println("Internal Server Error");
						else {
							
							errors = 0;
							errorLetters = "";
							guess = getFreshGuess(word);
							modulo = help.length;
							count = 0;
							
							
							while (errors < 6){
								
								System.out.println("Word: " + guess.toUpperCase() + " , errors: " + errors + ", false: " + errorLetters.toUpperCase());
								System.out.println("Your input: ");
								
								input = in.nextLine();
								
								if (input.toLowerCase().equals("help")){
									System.out.println(help[count % modulo]);
									count++;
									errors++;
								}
								else if (input.toLowerCase().equals("quit")){
									System.out.println("Goodbye!");
									
									break;
								}
								else if (input.length() == 1){
									aux = guess;
									
									found = false;
									for (int j = 0; j < errorLetters.length(); j++){
										if (errorLetters.charAt(j) == input.charAt(0)){
											found = true;
											break;
										}
									}
									
									if(!found){
										guess = constructGuessWord(guess, word, input);
										
										if (guess.toLowerCase().equals(aux.toLowerCase())){
											errorLetters += input;
											errors++;
										}
										else if (!guess.contains("_")){
											System.out.println(word.toUpperCase() + ", Congratulations!!");
											break;
										}
									}
									else{
										System.out.println("Letter already chosen.");
									}
								}
								else{
									if (word.toLowerCase().equals(input.toLowerCase())){
										server.wordTimestamp(gamename, username, input.toLowerCase());
										System.out.println(word.toUpperCase() + ", is the right word!!");
										
										try {
											server.waitGameend(gamename);
											if (server.checkwin(gamename, username) == false){
												System.out.println("sorry, you were too slow!");
												server.gameend(gamename);
											}
											else{
												System.out.println("Congratulation");
												server.gameend(gamename);
											}
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										break;
									}
									else
										errors++;
								}
							}
							
							if (errors == 6){
								server.wordTimestamp(gamename, username, "");
								System.out.println("Game Over..");
							}
							server.waitGameend(gamename);
							if(server.checkTie(gamename) == false){
								server.gameend(gamename);
								break;
							}
							else{
								server.gameend(gamename);
								word = server.getWord(gamename);
							}
						}
					}
				}
				else if (input.toLowerCase().equals("j")){
					
					System.out.println("Please chose one of the following pending games:");
					System.out.println(server.showSession());
					
					gamename = in.nextLine();
					
					server.joinerconnection(username, gamename);
					
					while (true){
						try {
								word = server.joinergetWord(gamename);
							
							
							
							if (word.equals(""))
								System.out.println("Internal Server Error");
							else
								help = server.getHelp(word);
								
							if (help == null)
								System.out.println("Internal Server Error");
							else {
								
								errors = 0;
								errorLetters = "";
								guess = getFreshGuess(word);
								modulo = help.length;
								count = 0;
								
								
								while (errors < 6){
									
									System.out.println("Word: " + guess.toUpperCase() + " , errors: " + errors + ", false: " + errorLetters.toUpperCase());
									System.out.println("Your input: ");
									
									input = in.nextLine();
									
									if (input.toLowerCase().equals("help")){
										System.out.println(help[count % modulo]);
										count++;
										errors++;
									}
									else if (input.toLowerCase().equals("quit")){
										System.out.println("Goodbye!");
										
										break;
									}
									else if (input.length() == 1){
										aux = guess;
										
										found = false;
										for (int j = 0; j < errorLetters.length(); j++){
											if (errorLetters.charAt(j) == input.charAt(0)){
												found = true;
												break;
											}
										}
										
										if(!found){
											guess = constructGuessWord(guess, word, input);
											
											if (guess.toLowerCase().equals(aux.toLowerCase())){
												errorLetters += input;
												errors++;
											}
											else if (!guess.contains("_")){
												System.out.println(word.toUpperCase() + ", Congratulations!!");
												break;
											}
										}
										else{
											System.out.println("Letter already chosen.");
										}
									}
									else{
										if (word.toLowerCase().equals(input.toLowerCase())){
											server.wordTimestamp(gamename, username, input.toLowerCase());
											System.out.println(word.toUpperCase() + ", is the right word!!");
											
											try {
												server.waitGameend(gamename);
												if (server.checkwin(gamename, username) == false){
													System.out.println("sorry, you were too slow!");
													server.gameend(gamename);
												}
												else{
													System.out.println("Congratulation");
													server.gameend(gamename);
												}
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											break;
										}
										else
											errors++;
									}
								}
								
								if (errors == 6){
									server.wordTimestamp(gamename, username, "");
									System.out.println("Game Over..");
								}
								server.waitGameend(gamename);
								if(server.checkTie(gamename) == false){
									server.gameend(gamename);
									break;
								}
								else{
									server.gameend(gamename);
								}
							}
						}catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
				else if (input.equals("exit")){
					break;
				}
				else{
					System.out.println("Invalid input.\n");
				}
			}
			
			in.close();
			System.out.println("Goodbye!");
			
		} catch (RemoteException | NotBoundException | InterruptedException e) {
			
			e.printStackTrace();
		}
	}
}