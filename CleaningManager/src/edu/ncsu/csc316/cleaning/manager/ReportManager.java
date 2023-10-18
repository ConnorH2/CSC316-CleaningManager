package edu.ncsu.csc316.cleaning.manager;

import java.io.FileNotFoundException;
import edu.ncsu.csc316.cleaning.dsa.Algorithm;
import edu.ncsu.csc316.cleaning.dsa.DSAFactory;
import edu.ncsu.csc316.cleaning.dsa.DataStructure;

public class ReportManager {

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

    public ReportManager(String pathToRoomFile, String pathToLogFile, DataStructure mapType) throws FileNotFoundException {
        manager = new CleaningManager(pathToRoomFile, pathToLogFile, mapType);
        DSAFactory.setListType(...);
        DSAFactory.setComparisonSorterType(...);
        DSAFactory.setNonComparisonSorterType(...);
        DSAFactory.setMapType(mapType);
        
        // TODO: Complete this constructor
    }
    
    public ReportManager(String pathToRoomFile, String pathToLogFile) throws FileNotFoundException {
        this(pathToRoomFile, pathToLogFile, ...);
    }

    public String getVacuumBagReport(String timestamp) {
        // TODO: Complete this method
    }

    public String getFrequencyReport(int number) {
        // TODO: Complete this method
    }

    public String getRoomReport() {
        // TODO: Complete this method
    }
}
