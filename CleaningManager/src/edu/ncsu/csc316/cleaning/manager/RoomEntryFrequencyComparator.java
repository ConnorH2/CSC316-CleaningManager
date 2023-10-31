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
public class RoomEntryFrequencyComparator implements Comparator<Entry<String, List<CleaningLogEntry>>> {

	/**
	 * Compares eventsByRoom map entries by their cleaning frequencies then room name if frequencies are equal.
	 * @param one The first map Entry to compare
	 * @param two The second map Entry to compare
	 * @return an int greater than zero if the cleaning frequency of room entry two is greater, 
	 * the result of comparing the two Room names if the frequencies are equal, 
	 * or an int less than zero if the frequency of room entry one is greater.
	 */
	@Override
	public int compare(Entry<String, List<CleaningLogEntry>> one, Entry<String, List<CleaningLogEntry>> two) {
		int diff1 = two.getValue().size() - one.getValue().size();
		if(diff1 != 0) {
			return diff1;
		} else {
			return one.getKey().compareTo(two.getKey());
		}
	}

}