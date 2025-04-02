package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Card {
	private ArrayList<Card> cards= new ArrayList<Card>();
	private static String setupConfigFile;
	private CardType type;
	
	public static void loadCards() throws BadConfigFormatException {
		return;
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public static void setConfigFile(String setup) {
		setupConfigFile = setup;

	}
}
