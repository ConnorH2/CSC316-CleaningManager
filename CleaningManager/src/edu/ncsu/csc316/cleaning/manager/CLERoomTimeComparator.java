package edu.ncsu.csc316.cleaning.manager;

import java.util.Comparator;

import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;


/**
 * Comparator for comparing CleaningLogEntry based first on roomID, then on time
 * @author Connor Hekking
 */
public class CLERoomTimeComparator implements Comparator<CleaningLogEntry> {

	/**
	 * Compares CleaningLogEntry object first using their roomID, then using their time
	 * @param one The first CleaningLogEntry to compare
	 * @param two The second CleaningLogEntry to compare
	 * @return an int greater than zero if the roomID or time of CLE one is greater,
	 *  an int less than zero if the roomId or time of CLE one is less, 
	 *  zero if both roomID's and times are equivalent
	 */
	@Override
	public int compare(CleaningLogEntry one, CleaningLogEntry two) {
		return -one.getTimestamp().compareTo(two.getTimestamp());
	}

}