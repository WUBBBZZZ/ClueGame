package clueGame;
import java.util.ArrayList;

public abstract class Player {

	private static ArrayList<String> people = new ArrayList<String>();
	
	public static void loadPeople() {
		//Loads in all 6 characters from ClueSetup.txt and
		//places them in the corresponding ArrayList defined above
		
		people.add("Fake Person");
	}
	
	public static ArrayList<String> getPeople() {
		return people;
	}
	
}
