package clueGame;

public class ComputerPlayer extends Player {

	private static int numPlayers;
	
	public ComputerPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
		numPlayers++;
	}
	
	@Override
	public void updateHand(Card card) {
		
	}
	
	public static int getNumPlayers() {
		return numPlayers;
	}
	
}
