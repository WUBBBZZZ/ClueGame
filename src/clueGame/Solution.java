package clueGame;

public class Solution {
	
	private Card room;
	private Card person;
	private Card weapon;
	
	public Solution(Card room, Card person, Card weapon) {
		this.room = room;
		this.person = person;
		this.weapon = weapon;
	}
	
	public Card getRoomSol() {
		return room;
	}
	
	public Card getPersonSol() {
		return person;
	}
	
	public Card getWeaponSol() {
		return weapon;
	}
	
}
