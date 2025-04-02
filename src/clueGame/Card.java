package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Card {
	private String cardName;
	private CardType cardType;
	
	public Card(String name) {
		cardName = name;
		
	}
	
	public boolean equals(Card target) {
		if (this.getCardName().equals(target.getCardName())) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getCardName() {
		return cardName;
	}
	
	public CardType getCardType() {
		return cardType;
	}
	
	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}
	
}
