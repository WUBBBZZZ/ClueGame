package clueGame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Player {

	private static ArrayList<String> people = new ArrayList<String>();
	private static String setupConfigFile;
	
	public static void loadPeople() throws BadConfigFormatException {
		//Loads in all 6 characters from ClueSetup.txt and
		//places them in the corresponding ArrayList defined above
		
		String finishedSetup = "./data/" + setupConfigFile;
		try(Scanner setupScanner = new Scanner(new File(finishedSetup))){
			
			// Read from setupConfigFile
			while(setupScanner.hasNext()) {
				String line = setupScanner.nextLine().trim();
				String[] parts = line.split("\\s*,\\s*");
				
				// Skip empty lines or comments
	            if (line.isEmpty() || line.startsWith("//")) {
	                continue;
	            }
				
				// Check each line for rooms and spaces
				if(line.startsWith("Person")) {
					if(parts.length != 2) {
						String message = "Invalidformat, expecting a name for each player, bad line: \" " + line + "\".";
						throw new BadConfigFormatException(message);
					}
					
					String playerName = parts[1].trim();
					people.add(playerName);			
					
				} else {
					continue;
				}
			}
		}
		catch(FileNotFoundException e){
			System.out.println("File not found, please try again.");
		}
			
		
	}
	
	public static ArrayList<String> getPeople() {
		return people;
	}
	
	public static void setConfigFile(String setup) {
		setupConfigFile = setup;

	}
	
}
