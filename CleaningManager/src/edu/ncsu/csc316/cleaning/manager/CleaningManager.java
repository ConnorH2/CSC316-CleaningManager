

package edu.ncsu.csc316.cleaning.manager;

import java.io.FileNotFoundException;
import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.cleaning.dsa.Algorithm;
import edu.ncsu.csc316.cleaning.dsa.DSAFactory;
import edu.ncsu.csc316.cleaning.dsa.DataStructure;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;

public class CleaningManager {

    public CleaningManager(String pathToRoomFile, String pathToLogFile, DataStructure mapType) throws FileNotFoundException {
        DSAFactory.setListType(...);
        DSAFactory.setComparisonSorterType(...);
        DSAFactory.setNonComparisonSorterType(...);
        DSAFactory.setMapType(mapType);
        
        // TODO: Complete this constructor
    }
    
    public CleaningManager(String pathToRoomFile, String pathToLogFile) throws FileNotFoundException {
        this(pathToRoomFile, pathToLogFile, ...);
    }

    public Map<String, List<CleaningLogEntry>> getEventsByRoom() {
        // TODO: Complete this method
    }

    public int getCoverageSince(LocalDateTime time) {
        // TODO: Complete this method
    }

}

