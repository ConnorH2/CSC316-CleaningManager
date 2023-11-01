package edu.ncsu.csc316.cleaning.manager;

import java.util.Comparator;

import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map.Entry;


/**
 * Comparator for comparing eventsByRoom map entries by their cleaning frequencies then 
 * by room name if frequencies are equal.
 * @author Connor Hekking
 */
public class RoomComparator implements Comparator<String> {

	/**
	 * Compares eventsByRoom map entries by their cleaning frequencies then room name if frequencies are equal.
	 * @param one The first map Entry to compare
	 * @param two The second map Entry to compare
	 * @return an int greater than zero if the cleaning frequency of room entry two is greater, 
	 * the result of comparing the two Room names if the frequencies are equal, 
	 * or an int less than zero if the frequency of room entry one is greater.
	 */
	@Override
	public int compare(String one, String two) {
		return one.compareTo(two);
	}

}