package edu.ncsu.csc316.cleaning.manager;

import java.util.Comparator;



/**
 * Comparator for comparing eventsByRoom map entries by their room name.
 * @author Connor Hekking
 */
public class RoomAlphabeticComparator implements Comparator<String> {

	/**
	 * Compares eventsByRoom map entries by their room name.
	 * @param one The first map Entry to compare
	 * @param two The second map Entry to compare
	 * @return TODO
	 */
	@Override
	public int compare(String one, String two) {
		return one.compareTo(two);
	}

}