package edu.ncsu.csc316.cleaning.manager;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.cleaning.dsa.Algorithm;
import edu.ncsu.csc316.cleaning.dsa.DSAFactory;
import edu.ncsu.csc316.cleaning.dsa.DataStructure;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.dsa.sorter.Sorter;

public class ReportManager {

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
	
    private CleaningManager manager;

    public ReportManager(String pathToRoomFile, String pathToLogFile, DataStructure mapType) throws FileNotFoundException {
        manager = new CleaningManager(pathToRoomFile, pathToLogFile, mapType);
        DSAFactory.setListType(DataStructure.ARRAYBASEDLIST);
        DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
        //DSAFactory.setNonComparisonSorterType(...); TODO ?
        DSAFactory.setMapType(mapType);
        
        // TODO: Complete this constructor
    }
    
    public ReportManager(String pathToRoomFile, String pathToLogFile) throws FileNotFoundException {
        this(pathToRoomFile, pathToLogFile, DataStructure.SKIPLIST);
    }

    public String getVacuumBagReport(String timestamp) {
    	// TODO
    }

    public String getFrequencyReport(int number) {
    	// Initialize our frequency report string
        String s = "Room Report [\n";
        // Standard indent for the report is three spaces.
        String indent = "   ";
        
        Map<String, List<CleaningLogEntry>> cleaningsByRoom = manager.getEventsByRoom();
        
        // 
        DSAFactory.getComparisonSorter(null);
    }

    public String getRoomReport() {
    	// Initialize our room report string
        String s = "Room Report [\n";
        // Standard indent for the report is three spaces.
        String indent = "   ";
        
        Map<String, List<CleaningLogEntry>> cleaningsByRoom = manager.getEventsByRoom();
        
        // Create an iterator to access the map entries in O(n) runtime 
        Iterator<Entry<String, List<CleaningLogEntry>>> it1 = cleaningsByRoom.entrySet().iterator();
        
        // Create a Sorter to sort the CleaningLogEntries by time
        DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
        Sorter<CleaningLogEntry> cleSorter = DSAFactory.getComparisonSorter(new CLERoomTimeComparator());
        
        // Print out the report for each room
        while(it1.hasNext()) {
        	Entry<String, List<CleaningLogEntry>> currentEntry = it1.next();
        	s = s + indent + currentEntry.getKey() + " was cleaned on [\n";
        	
        	// Convert the list to an array so we can sort it
        	CleaningLogEntry[] currentList = new CleaningLogEntry[currentEntry.getValue().size()];
        	
        	int idx = 0;
        	for(CleaningLogEntry c : currentEntry.getValue()) {
        		currentList[idx] = c;
        		idx++;
        	}
        	
        	// Sort the new array
        	cleSorter.sort(currentList);
        	
        	// Print the timestamp for each cleaning
        	for(CleaningLogEntry currentCleaning : currentList) {
        		s = s + indent + indent + currentCleaning.getTimestamp().toString() + "\n";
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
        
        // Return our constructed strings
        return s;
    }
}
