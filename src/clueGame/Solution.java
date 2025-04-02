package clueGame;

public class Solution {

	private static Card room;
	private static Card person;
	private static Card weapon;
	
	public static Card getRoomSol() {
		return room;
	}
	
	public static Card getPersonSol() {
		return person;
	}
	
	public static Card getWeaponSol() {
		return weapon;
	}
	
	public static void setRoomSol(Card r) {
		room = new Card(r.getCardName());
		room.setCardType(r.getCardType());
	}
	
	public static void setPersonSol(Card p) {
		person = new Card(p.getCardName());
		person.setCardType(p.getCardType());
	}
	
	public static void setWeaponSol(Card w) {
		weapon = new Card(w.getCardName());
		weapon.setCardType(w.getCardType());
	}
	
}
