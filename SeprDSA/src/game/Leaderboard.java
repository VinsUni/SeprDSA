package game;

import game.LeaderboardEntry;

import java.io.*;
import java.util.Arrays;

/**
 * Leaderboard - holds / displays multiple LeaderboardEntries.
 */

public class Leaderboard /* implements Drawable */{

	/**
	 * Array containing the LeaderboardEntries
	 */
	LeaderboardEntry[] leaderboardEntries = new LeaderboardEntry[5];
	/**
	 * Path to the file in which leaderboardScores are stored.
	 */
	String leaderboardFile = "leaderboardscores.txt";

	/**
	 * Constructor - if first load, initialise file, else load values from file
	 * into data structure.
	 */
	public Leaderboard() {
		// Initialise
		for (int i = 0; i < leaderboardEntries.length; i++) {
			leaderboardEntries[i] = new LeaderboardEntry();
		}

		// Initialise values if first run.
		File leaderboardFileCheckForFile = new File(leaderboardFile);
		// If game run before and scores exist:
		if (!leaderboardFileCheckForFile.exists()) {
			System.out
					.println("Is first load, initialising leaderboard values");
			for (int i = 0; i < leaderboardEntries.length; i++) {
				leaderboardEntries[i].setName("Sam");
				leaderboardEntries[i].setScore((double) 5 + i);	
			}

			sortLeaderboard(leaderboardEntries);
			saveLeaderboard();
		} else {
			addLeaderBoardEntries();
			sortLeaderboard(leaderboardEntries);
			saveLeaderboard();
				}
		}
		
	//==========================================================================
	//This is a test of the leaderboard
	// TODO(Dan) Turn this block into a test, so we can add these values and
	// check we get the expected output array of LeaderboardEntries.
	// printLeaderboard(); // Expect a 17, s 9, d 8.3, s 8, s 7*/

	public void addLeaderBoardEntries(){
			try{
				addLeaderboardEntry("a", 17);
				addLeaderboardEntry("b", 1);
				addLeaderboardEntry("c", 5.5);
				addLeaderboardEntry("d", 2.3);
				addLeaderboardEntry("e", 6.2);
				}
			catch (Exception ex){
				System.out.println("Adding leaderboard data caused error");}
	
			try{
				for (int i=0; i<leaderboardEntries.length; i++){
					System.out.println(leaderboardEntries[i].getName());
					System.out.println(leaderboardEntries[i].getScore());
					}
				}
			catch(Exception ex){
				System.out.println("Error printing leaderboard data");}

		}
	
	//==========================================================================
	
	/**
	 * Add entry to the leaderboard - creates an instance of LeaderboardEntry
	 * and adds it to the List.
	 * @param String name, double score.
	 */
	public void addLeaderboardEntry(String name, double score) {
		LeaderboardEntry[] tempLeaderboardEntries = new LeaderboardEntry[6];
		System.arraycopy(leaderboardEntries, 0, tempLeaderboardEntries, 0,
				leaderboardEntries.length);
		tempLeaderboardEntries[5] = new LeaderboardEntry(name, score);
		sortLeaderboard(tempLeaderboardEntries);
		System.arraycopy(tempLeaderboardEntries, 0, leaderboardEntries, 0,
				leaderboardEntries.length);
		saveLeaderboard();
	}

	/**
	 * Sorts the List of LeaderboardEntries according to their CompareTo method.
	 * @param leaderboardToSort
	 */
	private void sortLeaderboard(LeaderboardEntry[] leaderboardToSort) {
		Arrays.sort(leaderboardToSort);
	}

	/**
	 * Save the leaderboard data to file using OutputStream (streams an Object).
	 */
	private void saveLeaderboard() {
		try {
			// create a new file with an ObjectOutputStream
			FileOutputStream out = new FileOutputStream(leaderboardFile);
			ObjectOutputStream oout = new ObjectOutputStream(out);

			// write something in the file
			for (int i = 0; i < leaderboardEntries.length; i++) {
				oout.writeObject(leaderboardEntries[i].getName());
				oout.writeObject(leaderboardEntries[i].getScore());
			}
			oout.close();
		} catch (Exception ex) {
			System.out.println("Saving leaderboard raised exception.");
		}
	}

	/**
	 * Reads the leaderboard data from file using InputStream.
	 */
	private void readLeaderboard() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					leaderboardFile));
			for (int i = 0; i < leaderboardEntries.length; i++) {
				leaderboardEntries[i].setName((String) ois.readObject());
				leaderboardEntries[i].setScore((Double) ois.readObject());
			}

			ois.close();
		} catch (Exception ex) {
			System.out.println("Saving leaderboard raised exception.");
		}
	}

}
