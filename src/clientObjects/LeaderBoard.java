package clientObjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import exceptions.ResultsReadError;
import exceptions.UserNotFound;

/**
 * This class holds the number of users in this game, the gameID, the ranking and the points for everyone.
 * points for everyone.
 * @author adamtonks
 *
 */
public class LeaderBoard {
	private final int gameID;
	// ordered list of (username, points) pairs in descending order
	private final int boardLen;
	private final List<UserPoints> userPointsList;

	/**
	 * It takes in the unique game ID and retrieves the relevant information
	 * @param _gameID, an int
	 * @throws ResultsReadError, the results are not read correctly
	 * @throws FileNotFoundException, file cannot be found
	 * @throws UserNotFound, the user cannot be found in
	 */
	public LeaderBoard(int _gameID) throws ResultsReadError, FileNotFoundException, UserNotFound {
		this.gameID = _gameID;
		this.userPointsList = new ArrayList<UserPoints>();
		String currUsername;
		int i;
		String[] csvHeader = new String[]
				{"username", "password", "email", "gameID", "isHost",
						"points", "week", "GK0", "DF0", "DF1", "DF2",
						"DF3", "MF0", "MF1", "MF2", "MF3", "FW0", "FW1",
						"SUB0", "SUB1", "SUB2", "SUB3", "SUB4", "SUB5"};
		Scanner csvReader = new Scanner(new File(System.getProperty("user.home") + "/csv_tables/user_list.csv"));
		csvReader.useDelimiter(",|\\n");
		// check that table headers are correct
		for(i=0; i<24; i++) {
			// read first line of headers
			if(!csvHeader[i].equals(csvReader.next())) {
				csvReader.close();
				System.out.println("CSV file formatted incorrectly");
				throw new ResultsReadError();
			}

		}
		
		if(!csvReader.hasNext()) {
			csvReader.close();
			System.out.println("CSV file empty");
			throw new ResultsReadError();
		}
		while(csvReader.hasNext()) {
			currUsername = csvReader.next();
			for(i=0; i<2; i++)
				csvReader.next();
			if(_gameID == csvReader.nextInt()) {
				csvReader.next();
				userPointsList.add(new UserPoints(currUsername, csvReader.nextInt()));
				for(i=0; i<18; i++)
					csvReader.next();
			} else {
				for(i=0; i<20; i++)
					csvReader.next();
			}
		}
		
		csvReader.close();
		
		Collections.sort(userPointsList, new Comparator<UserPoints>() {
			public int compare(UserPoints up1, UserPoints up2) {
				return Integer.compare(up2.points, up1.points);
			}
		});
		
		this.boardLen = userPointsList.size();
		
	}
	
	/**
	 * This method constructs the leaderboard from the CSV line
	 * @param input, the CSVline 
	 */
	public LeaderBoard(CSVline input) {
		Scanner csvReader = new Scanner(input.string);
		csvReader.useDelimiter(",");
		this.gameID = csvReader.nextInt();
		this.boardLen = csvReader.nextInt();
		this.userPointsList = new ArrayList<UserPoints>();
		while(csvReader.hasNext()) {
			userPointsList.add(new UserPoints(
					csvReader.next(), csvReader.nextInt()));
		}
	}

	/**
	 * A getter of the game ID
	 * @return int, gameID
	 */
	public int getGameID() {
		return gameID;
	}

	/**
	 * The getter of the number of users in the game
	 * @return boardLen, number of users
	 */
	public int getBoardLen() {
		return boardLen;
	}
	
	/**
	 * The getter of each user and their updated points
	 * @return userPointsList, List of users and points
	 */
	public List<UserPoints> getUserPointsList() {
		return userPointsList;
	}
	
	/**
	 * Translates the Leaderboard details into a string to be stored in the csv file.
	 * @return
	 */
	public String toCSVrow() {
		int i;
		String csvRow = this.gameID + "," + this.boardLen;
		for(i=0; i<this.userPointsList.size(); i++) {
			csvRow += "," + userPointsList.get(i).username +
					"," + userPointsList.get(i).points;
		}
		return(csvRow);
		
	}
}
