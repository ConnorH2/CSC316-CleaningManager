

package edu.ncsu.csc316.cleaning.manager;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Iterator;

import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.cleaning.data.RoomRecord;
import edu.ncsu.csc316.cleaning.dsa.Algorithm;
import edu.ncsu.csc316.cleaning.dsa.DSAFactory;
import edu.ncsu.csc316.cleaning.dsa.DataStructure;
import edu.ncsu.csc316.cleaning.io.InputReader;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;

/**
 * CleaningManager assists in creating reports by 
 * creating data structures holding RoomRecords and CleaningLogEntries, 
 * and providing methods to access those data structures.
 * @author Connor Hekking
 */
public class CleaningManager {
	
	/** A map holding RoomRecords which are accessible by the room's ID. */
	private Map<String, RoomRecord> rooms;
	
	/** A map holding Lists of CleaningLogEntries which belong to a certain room ID. */
	private Map<String, List<CleaningLogEntry>> eventsByRoom;
	
	/** An unsorted list of all cleaning log entries. */
	private List<CleaningLogEntry> cleanings;
	
	/**
	 * Constructor which creates a new CleaningManager object with a specified mapType.
	 * This constructor initializes and fills RoomRecord and CleaningLogEntry data structures using IO methods.
	 * @param pathToRoomFile The path to the file of RoomRecords which should be used
	 * @param pathToLogFile The path to the file of CleaningLogEntrys which should be used
	 * @param mapType The type of map to use when creating data structures
	 * @throws FileNotFoundException If either of the specified data files cannot be found
	 */
    public CleaningManager(String pathToRoomFile, String pathToLogFile, DataStructure mapType) throws FileNotFoundException {
        // Setup DSAFactory
    	DSAFactory.setListType(DataStructure.ARRAYBASEDLIST);
        DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
        DSAFactory.setMapType(mapType);
        
        // Create new empty map to hold rooms
        rooms = DSAFactory.getMap(null); // Should use default comparator(alphabetic)
        // Create new empty map to hold events by room
        eventsByRoom = DSAFactory.getMap(null);
        
        // Insert RoomRecords from InputReader's List to our Map, nlog(n) runtime
        Iterator<RoomRecord> it1 = InputReader.readRoomFile(pathToRoomFile).iterator();
        while(it1.hasNext()) {
        	RoomRecord c = it1.next();
        	// Insert the RoomRecord into our sorted map based on roomID
        	rooms.put(c.getRoomID(), c);
        	// Initialize lists for eventsByRoom
        	eventsByRoom.put(c.getRoomID(), DSAFactory.getIndexedList());
        }
        
        // Import the CleaningLogEntrys into our list
        cleanings = InputReader.readLogFile(pathToLogFile);
        
        // Transfer the CleaningLogEntrys into our eventsByRoom map
        for(CleaningLogEntry c : cleanings) {
        	String a = c.getRoomID();
        	List<CleaningLogEntry> b = eventsByRoom.get(a);
        	b.addLast(c);
            //eventsByRoom.get(c.getRoomID()).addLast(c);
        }   
    }
    
    /**
	 * Constructor which creates a new CleaningManager object with the default SkipList mapType.
	 * This constructor initializes and fills RoomRecord and CleaningLogEntry data structures using IO methods.
	 * @param pathToRoomFile The path to the file of RoomRecords which should be used
	 * @param pathToLogFile The path to the file of CleaningLogEntrys which should be used
	 * @throws FileNotFoundException If either of the specified data files cannot be found
	 */
    public CleaningManager(String pathToRoomFile, String pathToLogFile) throws FileNotFoundException {
        this(pathToRoomFile, pathToLogFile, DataStructure.SKIPLIST);
    }

    /**
     * This function returns a map containing lists of cleaning log entries, 
     * with keys of which room the entries belong to.
     * @return A map with keys of the room's ID and values of lists of each room's recorded cleanings.
     */
    public Map<String, List<CleaningLogEntry>> getEventsByRoom() {
    	return eventsByRoom;
    }

    /**
     * Method that returns the total area covered by this vacuum after the given replacement date.
     * @param time The last date and time the vacuum bag was replaced.
     * @return The total area covered by the cleaner since the given time.
     */
    public int getCoverageSince(LocalDateTime time) { // UC4
        // Initialize the sum of the total area covered since last replacement
    	int sum = 0;

    	// Loop over each cleaning event
    	for(int i = 0; i < cleanings.size(); i++) {
    		CleaningLogEntry currentEntry = cleanings.get(i);
    		// Compare the dates using LocalDateTime.compareTo
    		if(currentEntry.getTimestamp().compareTo(time) > 0) {
    			// Current cleaning occurred after the bag replacement
    			RoomRecord currentRoom = rooms.get(currentEntry.getRoomID());
    			
    			// Area of each CleaningEvent = (length*width*percentCleaned)/ 100
    			sum += currentRoom.getLength() * currentRoom.getWidth() * currentEntry.getPercentCompleted() / 100;
    		}
    	}
    	
        // Return the total area covered since last replacement
    	return sum;
    }

}

