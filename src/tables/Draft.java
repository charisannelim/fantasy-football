import java.util.ArrayList;
import java.util.Random;

public class Draft {
	public ArrayList<FootballPlayer> remainingPlayers;
	public UserPlayer currentPlayerChoosing;
	public ArrayList<UserPlayer> draftOrder;
	public UserPlayer[] players;

	public Draft(FootballPlayer[] AllPlayers, UserPlayer[] players) {
		ArrayList<FootballPlayer> remainingPlayers = new ArrayList<FootballPlayer>();
		ArrayList<FootballPlayer> draftOrder = new ArrayList<FootballPlayer>();
		
		for(int i = 0;i < AllPlayers.length;i++) {
			this.remainingPlayers.add(AllPlayers[i]);
		}
		this.players = players;
		
	}

	public void createDraftOrder() {

		// random shuffling
		int index;
		UserPlayer temp;
		Random random = new Random();
		for (int i = this.players.length - 1; i > 0; i--) {
			index = random.nextInt(i + 1);
			temp = this.players[index];
			this.players[index] = this.players[i];
			this.players[i] = temp;
		}

		//snake order draft creation
		boolean snakeOrder = true;
		for (int i = 0; i < players.length * 17; i++) {

			if (snakeOrder) {
				for (int j = 1; j <= players.length; j++) {
					this.draftOrder.add(players[j - 1]);
				}
			} else {
				for (int j = 1; j <= players.length; j++) {
					this.draftOrder.add(players[players.length - 1]);
				}
			}
			snakeOrder = !snakeOrder;
		}

	}
	
	public void footballPlayerDrafted(FootballPlayer draftedPlayer){
		
		this.remainingPlayers.remove(draftedPlayer);
		
		/**
		int index;
		for (int i = 0; i<remainingPlayers.size();i++) {
			if (draftedPlayer.compareTo(this.remainingPlayers.get(i))) {
				index = i;
			}
		**/
		
	}

}