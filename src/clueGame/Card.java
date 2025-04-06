package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Card {
	private String cardName;
	private CardType cardType;
	
	public Card(String name, CardType cardType) {
		cardName = name;
		this.cardType = cardType;
	}
	
	public boolean equals(Card target) {
		if (this.getCardName().equals(target.getCardName())) {
			return true;
		} else {
			return false;
		}
	}
	@Override 
	public String toString(){
		return cardName;
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
