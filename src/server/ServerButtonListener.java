package server;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JPanel;

import clientObjects.Positions;
import exceptions.PlayerNotFound;
import exceptions.ResultsReadError;

/**
 * This Listener observes various buttons across the application
 * and directs the user to the necessary screens accordingly.
 * @author charisannelim
 *
 */
public class ServerButtonListener implements ActionListener {
	Lock csvLock;
	
	/**
	 * This constructor points the parameters as the current instance.
	 * @param screens, the main JPanel with the cardlayout
	 */
	public ServerButtonListener(Lock _csvLock) {
		this.csvLock = _csvLock;
	}

	/**
	 * This method switches screens depending on which button is pressed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == "update") {
			try {
				this.update();
			} catch (ResultsReadError | PlayerNotFound | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if(e.getActionCommand() == "end") {
			try {
				this.endSeason();
			} catch (ResultsReadError | PlayerNotFound | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
	}
	
	private void update() throws ResultsReadError, PlayerNotFound, IOException {
		PlayerScoreDict scoreDict = new PlayerScoreDict();
		Writer csvWriter;
		int i, j, wkPoints, wkNew;
		List<Integer> userPointsList = new ArrayList<Integer>();
		String[] csvHeader = new String[]
				{"username", "password", "email", "gameID", "isHost",
						"points", "week", "GK0", "DF0", "DF1", "DF2",
						"DF3", "MF0", "MF1", "MF2", "MF3", "FW0", "FW1",
						"SUB0", "SUB1", "SUB2", "SUB3", "SUB4", "SUB5"};
		synchronized(this.csvLock) {
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
			scoreDict.populateScores();
			while(csvReader.hasNext()) {
				for(i=0; i<5; i++)
					csvReader.next();
				wkPoints = csvReader.nextInt();
				csvReader.next();
				for(i=0; i<1; i++)
					wkPoints += scoreDict.findScore(csvReader.nextInt(), Positions.GK);
				for(i=0; i<4; i++)
					wkPoints += scoreDict.findScore(csvReader.nextInt(), Positions.DF);
				for(i=0; i<4; i++)
					wkPoints += scoreDict.findScore(csvReader.nextInt(), Positions.MF);
				for(i=0; i<2; i++)
					wkPoints += scoreDict.findScore(csvReader.nextInt(), Positions.FW);
				for(i=0; i<6; i++)
					csvReader.next();
				userPointsList.add(wkPoints);
			}
			csvReader.close();
			// now we need to print the new scores
			csvReader = new Scanner(new File(System.getProperty("user.home") + "/csv_tables/user_list.csv"));
			csvReader.useDelimiter(",|\\n");
			csvWriter = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream(System.getProperty("user.home") + "/csv_tables/user_list.csv.tmp"), "utf-8"));
			
			for(i=0; i<24; i++) {
				csvWriter.write(csvReader.next());
				if(i != 23)
					csvWriter.write(",");
				else
					csvWriter.write("\n");
			}
			for(i=0; i<userPointsList.size(); i++) {
				for(j=0; j<5; j++)
					csvWriter.write(csvReader.next() + ",");
				// write the new points
				csvWriter.write(userPointsList.get(i) + ",");
				csvReader.next();
				// write the new week
				wkNew = csvReader.nextInt() + 1;
				csvWriter.write(wkNew + ",");
				for(j=0; j<17; j++) {
					csvWriter.write(csvReader.next());
					if(j != 16) {
						csvWriter.write(",");
					} else {
						if(csvReader.hasNext())
							csvWriter.write("\n");
					}
				}
			}
	
			csvWriter.close();
			csvReader.close();
			
			// now delete the old file and rename the temporary one
			File oldFile = new File(System.getProperty("user.home") + "/csv_tables/user_list.csv");
			oldFile.delete();
			File newFile = new File(System.getProperty("user.home") + "/csv_tables/user_list.csv.tmp");
			newFile.renameTo(oldFile);
		}
		
	}
	
	// end the season by setting all weeks to -1
	private void endSeason() throws ResultsReadError, PlayerNotFound, IOException {
		Scanner csvReader;
		Writer csvWriter;
		int i, j;
		
		// modify table by simultaneous reader and writer objects
		synchronized(this.csvLock) {
			csvReader = new Scanner(new File(System.getProperty("user.home") + "/csv_tables/user_list.csv"));
			csvReader.useDelimiter(",|\\n");
			csvWriter = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream(System.getProperty("user.home") + "/csv_tables/user_list.csv.tmp"), "utf-8"));
			String heading;
			String[] csvHeader = new String[]
					{"username", "password", "email", "gameID", "isHost",
							"points", "week", "GK0", "DF0", "DF1", "DF2",
							"DF3", "MF0", "MF1", "MF2", "MF3", "FW0", "FW1",
							"SUB0", "SUB1", "SUB2", "SUB3", "SUB4", "SUB5"};
			
			// check that table headers are correct
			for(i=0; i<24; i++) {
				// read first line of headers
				heading = csvReader.next();
				if(!csvHeader[i].equals(heading)) {
					csvWriter.close();
					csvReader.close();
					System.out.println("CSV file formatted incorrectly");
					throw new ResultsReadError();
				}
				csvWriter.write(heading);
				if(i != 23)
					csvWriter.write(",");
			}
			csvWriter.write("\n");
			
			while(csvReader.hasNext()) {
				for(j=0; j<6; j++)
					csvWriter.write(csvReader.next() + ",");
				// write the new week
				csvReader.next();
				csvWriter.write("-1,");
				for(j=0; j<17; j++) {
					csvWriter.write(csvReader.next());
					if(j != 16) {
						csvWriter.write(",");
					} else {
						if(csvReader.hasNext())
							csvWriter.write("\n");
					}
				}
			}
				
			csvWriter.close();
			csvReader.close();
			
			// now delete the old file and rename the temporary one
			File oldFile = new File(System.getProperty("user.home") + "/csv_tables/user_list.csv");
			oldFile.delete();
			File newFile = new File(System.getProperty("user.home") + "/csv_tables/user_list.csv.tmp");
			newFile.renameTo(oldFile);
		}
		
	}
	
}
