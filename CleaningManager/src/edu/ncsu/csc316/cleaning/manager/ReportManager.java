package edu.ncsu.csc316.cleaning.manager;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;

import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.cleaning.dsa.Algorithm;
import edu.ncsu.csc316.cleaning.dsa.DSAFactory;
import edu.ncsu.csc316.cleaning.dsa.DataStructure;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.dsa.sorter.Sorter;

/**
 * ReportManager generates reports for the main ui function 
 * using data structures provided by a CleaningManager object.
 * @author Connor Hekking
 */
public class ReportManager {

	/** DateTimeFormatter object for use in turning a given time string into a LocalDateTime object. */
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
	
    /** CleaningManager object for use in creating data structures for generating reports. */
    private CleaningManager manager;

    /**
     * ReportManager constructor with a specified mapType.
     * This constructor initializes a new CleaningManager object.
     * @param pathToRoomFile The path to the file of RoomRecords which should be used
	 * @param pathToLogFile The path to the file of CleaningLogEntrys which should be used
	 * @param mapType The type of map to use when creating data structures
	 * @throws FileNotFoundException If either of the specified data files cannot be found
     */
    public ReportManager(String pathToRoomFile, String pathToLogFile, DataStructure mapType) throws FileNotFoundException {
        manager = new CleaningManager(pathToRoomFile, pathToLogFile, mapType);
        DSAFactory.setListType(DataStructure.ARRAYBASEDLIST);
        DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
        DSAFactory.setMapType(mapType);
    }
    
    /**
     * ReportManager constructor with the default SkipList mapType.
     * This constructor initializes a new CleaningManager object.
     * @param pathToRoomFile The path to the file of RoomRecords which should be used
	 * @param pathToLogFile The path to the file of CleaningLogEntrys which should be used
	 * @throws FileNotFoundException If either of the specified data files cannot be found
     */
    public ReportManager(String pathToRoomFile, String pathToLogFile) throws FileNotFoundException {
        this(pathToRoomFile, pathToLogFile, DataStructure.SKIPLIST);
    }

    /**
     * Method which creates a report of the estimated vacuum bag life.
     * If the area since replacement is over 5280 square feet, the bag is overdue for replacement.
     * If less than 5280 square feet, the area remaining in bag life will be shown.
     * @param timestamp The time at which the bag was last replaced
     * @return A string of output reporting if the bag needs to be replaced, 
     * or how much area it can cover until then
     */
    public String getVacuumBagReport(String timestamp) {
    	String s = "Vacuum Bag Report (last replaced " + timestamp + ") [\n";
    	
    	LocalDateTime timeObj;
    	// Parse the timestamp string and return an error string if unsuccessful
    	try {
    		timeObj = LocalDateTime.parse(timestamp, DATE_TIME_FORMAT);
    	} catch(DateTimeParseException e) {
    		return "Date & time must be in the format: MM/DD/YYYY HH:MM:SS";
    	}
    	
    	int area = manager.getCoverageSince(timeObj);
    	
    	// Bag needs replacing if area over 5280 ft
    	if(area > 5280) {
    		s = s + "   Bag is overdue for replacement!\n]";
    	} else {
    		// Else, return the area until the bag is due for replacement
        	area = 5280 - area;
        	s = s + "   Bag is due for replacement in " + area + " SQ FT\n]";
    	}
    	return s;
    }

    /**
     * Method which creates a report of the most frequently cleaned rooms.
     * A specified number of rooms will be listed in order of their cleaning frequency.
     * @param number The number of rooms to report the frequency of
     * @return A string of output containing the frequency report
     */
    public String getFrequencyReport(int number) {
    	// Use a local variable which we can change
    	int roomNum = number;
    	
    	// Error check: number must be greater than zero
    	if(roomNum <= 0) {
    		return "Number of rooms must be greater than 0.";
    	}
    	
    	// Initialize our frequency report string
        String s = "Frequency of Cleanings [\n";
        // Standard indent for the report is three spaces.
        String indent = "   ";
        
        Map<String, List<CleaningLogEntry>> cleaningsByRoom = manager.getEventsByRoom();
        
        // If there are no rooms, print an error text
        if(cleaningsByRoom.size() == 0) {
        	return "No rooms have been cleaned.";
        }
        
        // If requested number of rooms is greater than the number of rooms, report all of the rooms
        if(roomNum > cleaningsByRoom.size()) {
        	roomNum = cleaningsByRoom.size();
        }
        
        // Create an array of map entries to be sorted
        @SuppressWarnings("unchecked")
		Entry<String, List<CleaningLogEntry>>[] sortedList = new Entry[cleaningsByRoom.size()];
        
        int idx = 0;
        for(Entry<String, List<CleaningLogEntry>> entry : cleaningsByRoom.entrySet()) {
        	sortedList[idx] = entry;
        	idx++;
        }
        
        // Sort the array of map entries by frequency
        DSAFactory.getComparisonSorter(new RoomEntryFrequencyComparator()).sort(sortedList);
        
        // If there are no cleanings, print an error text.
        if(sortedList[0].getValue().size() == 0) {
        	return "No rooms have been cleaned.";
        }
        
        // Print out the frequency report for each room
        for(int i = 0; i < roomNum; i++) {
        	Entry<String, List<CleaningLogEntry>> current = sortedList[i];
        	s = s + indent + current.getKey() + " has been cleaned " + current.getValue().size() + " times\n";
        }
        
        // Close the bracket
        s = s + "]";
        
        // Return our constructed string
        return s;
    }

    /**
     * Method which creates a report of each time every room was cleaned. 
     * Each room(in alphabetical order) will have its cleanings(in chronological order) printed.
     * @return A string containing the room report
     */
    public String getRoomReport() {
    	// Initialize our room report string
        String s = "Room Report [\n";
        // Standard indent for the report is three spaces.
        String indent = "   ";
        
        Map<String, List<CleaningLogEntry>> cleaningsByRoom = manager.getEventsByRoom();
        
        // If there are no rooms, report an error message
        if(cleaningsByRoom.size() == 0) {
        	return "No rooms have been cleaned.";
        }
        
        // Create an iterator to access the map entries in O(n) runtime 
        //Iterator<Entry<String, List<CleaningLogEntry>>> it1 = cleaningsByRoom.entrySet().iterator();
        
        // Create a Sorter to sort the CleaningLogEntries by time
        Sorter<CleaningLogEntry> cleSorter = DSAFactory.getComparisonSorter(new CLERoomTimeComparator());
        
        //*
        // Create an array of map entries to be sorted
        @SuppressWarnings("unchecked")
		Entry<String, List<CleaningLogEntry>>[] sortedList = new Entry[cleaningsByRoom.size()];
        
        int idx1 = 0;
        for(Entry<String, List<CleaningLogEntry>> entry : cleaningsByRoom.entrySet()) {
        	sortedList[idx1] = entry;
        	idx1++;
        }
        
        // Sort the array of map entries by frequency
        DSAFactory.getComparisonSorter(new RoomComparator()).sort(sortedList);
        //*
        
        // Create a boolean flag to detect if there are no cleaning entries
        boolean hasEntries = false;
        
        // Print out the report for each room
        for(Entry<String, List<CleaningLogEntry>> currentEntry : sortedList) {
        	s = s + indent + currentEntry.getKey() + " was cleaned on [\n";
        	
        	// Convert the list to an array so we can sort it
        	CleaningLogEntry[] currentList = new CleaningLogEntry[currentEntry.getValue().size()];
        	
        	// There are cleaning entries, update our flag
        	if(currentList.length > 0) {
        		hasEntries = true;
        	}
        	
        	int idx = 0;
        	for(CleaningLogEntry c : currentEntry.getValue()) {
        		currentList[idx] = c;
        		idx++;
        	}
        	
        	// Sort the new array
        	cleSorter.sort(currentList);
        	
        	// Print the timestamp for each cleaning
        	for(CleaningLogEntry currentCleaning : currentList) {
        		s = s + indent + indent + currentCleaning.getTimestamp().format(DATE_TIME_FORMAT) + "\n";
        	}
        	
        	// If the room has no cleanings, print (never cleaned)
        	if(currentEntry.getValue().size() == 0) {
        		s = s + indent + indent + "(never cleaned)\n";
        	}
        	// Close the bracket
        	s = s + indent + "]\n";
        }
        
        // Close the bracket
        s = s + "]";
        
        // Check if there are no entries, if not then report error message
        if(!hasEntries) {
        	return "No rooms have been cleaned.";
        }
        
        // Return our constructed string
        return s;
    }
}
