package edu.ncsu.csc316.cleaning.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;

/**
 * Test class for CleaningManager
 * @author Connor Hekking
 */
class CleaningManagerTest {
// TODO JAVADOC TEST CODE
	/** CleaningManager object for testing. */
	CleaningManager manager;
	
	@BeforeEach
    public void setUp() {
        try {
			manager = new CleaningManager("input/rooms1.txt", "input/logs1.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
	
	@Test
	void testGetEventsByRoom() {
		assertThrows(FileNotFoundException.class, () -> new CleaningManager("input/rooms0.txt", "input/logs0.txt"));
		assertThrows(NoSuchElementException.class, () -> new CleaningManager("input/rooms3.txt", "input/logs3.txt"));
		
		Map<String, List<CleaningLogEntry>> m = manager.getEventsByRoom();
		
		Iterator<Entry<String, List<CleaningLogEntry>>> it1 = m.entrySet().iterator();
		
		assertEquals(6, m.size());
		
		Entry<String, List<CleaningLogEntry>> e1 = it1.next();
		Entry<String, List<CleaningLogEntry>> e2 = it1.next();
		Entry<String, List<CleaningLogEntry>> e3 = it1.next();
		Entry<String, List<CleaningLogEntry>> e4 = it1.next();
		Entry<String, List<CleaningLogEntry>> e5 = it1.next();
		Entry<String, List<CleaningLogEntry>> e6 = it1.next();
		assertFalse(it1.hasNext());
		
		assertEquals("Dining Room", e1.getKey());
		assertEquals(6, e1.getValue().size());
		
		assertEquals("Dining Room", e1.getValue().get(0).getRoomID());
		assertEquals(100, e1.getValue().get(0).getPercentCompleted());
		assertEquals(80, e1.getValue().get(1).getPercentCompleted());
		
		assertEquals("2023-09-13T20:08:05", e1.getValue().get(0).getTimestamp().toString());
		assertEquals("2023-09-11T20:21:19", e1.getValue().get(1).getTimestamp().toString());
		assertEquals("2023-09-08T20:14:52", e1.getValue().get(2).getTimestamp().toString());
		assertEquals("2023-09-06T20:19:39", e1.getValue().get(3).getTimestamp().toString());
		assertEquals("2023-09-04T20:29:12", e1.getValue().get(4).getTimestamp().toString());
		assertEquals("2023-09-01T20:00", e1.getValue().get(5).getTimestamp().toString());
		
		assertEquals("Entryway", e2.getKey());
		assertEquals(0, e2.getValue().size());
		
		assertEquals("Kitchen", e3.getKey());
		assertEquals(6, e3.getValue().size());
		
		assertEquals("Living Room", e4.getKey());
		assertEquals(2, e4.getValue().size());
		
		assertEquals("Main Bathroom", e5.getKey());
		assertEquals(2, e5.getValue().size());
		
		assertEquals("Main Bedroom", e6.getKey());
		assertEquals(2, e6.getValue().size());
		
		
		assertEquals("2023-09-08T19:12:16", e3.getValue().get(2).getTimestamp().toString());
		assertEquals("2023-09-02T13:06:45", e4.getValue().get(1).getTimestamp().toString());
		assertEquals("2023-09-09T12:46:42", e5.getValue().get(0).getTimestamp().toString());
		assertEquals("2023-09-02T08:18:39", e6.getValue().get(1).getTimestamp().toString());
		
		try {
			manager = new CleaningManager("input/rooms2.txt", "input/logs2.txt");
		} catch (FileNotFoundException e) {
			fail();
		}
		
		assertEquals(1, manager.getEventsByRoom().size());
		assertEquals(0, manager.getEventsByRoom().entrySet().iterator().next().getValue().size());
		assertEquals("TestEmpty", manager.getEventsByRoom().entrySet().iterator().next().getKey());
	}

	@Test
	void testGetCoverageSince() {
		LocalDateTime t0 = LocalDateTime.parse("10/31/2023 13:20:43", ReportManager.DATE_TIME_FORMAT);
		LocalDateTime t1 = LocalDateTime.parse("09/13/2023 20:08:06", ReportManager.DATE_TIME_FORMAT);
		LocalDateTime t2 = LocalDateTime.parse("09/13/2023 20:08:04", ReportManager.DATE_TIME_FORMAT);
		LocalDateTime t3 = LocalDateTime.parse("09/12/2023 00:00:00", ReportManager.DATE_TIME_FORMAT);
		LocalDateTime t4 = LocalDateTime.parse("09/09/2023 11:52:48", ReportManager.DATE_TIME_FORMAT);
		LocalDateTime t5 = LocalDateTime.parse("09/09/2023 09:30:40", ReportManager.DATE_TIME_FORMAT);
		LocalDateTime t6 = LocalDateTime.parse("09/08/2023 20:14:52", ReportManager.DATE_TIME_FORMAT);
		LocalDateTime t7 = LocalDateTime.parse("09/08/2023 20:14:51", ReportManager.DATE_TIME_FORMAT);
		LocalDateTime t8 = LocalDateTime.parse("09/03/2023 20:41:13", ReportManager.DATE_TIME_FORMAT);
		LocalDateTime t9 = LocalDateTime.parse("09/01/2023 10:56:33", ReportManager.DATE_TIME_FORMAT);
		LocalDateTime t10 = LocalDateTime.parse("09/08/1993 20:14:51", ReportManager.DATE_TIME_FORMAT);
		
		assertEquals(0, manager.getCoverageSince(t0));
		assertEquals(0, manager.getCoverageSince(t1));
		assertEquals(64, manager.getCoverageSince(t2));
		assertEquals(181, manager.getCoverageSince(t3));
		assertEquals(570, manager.getCoverageSince(t4));
		assertEquals(4600, manager.getCoverageSince(t5));
		assertEquals(4600, manager.getCoverageSince(t6));
		assertEquals(4649, manager.getCoverageSince(t7));
		assertEquals(5047, manager.getCoverageSince(t8));
		assertEquals(9267, manager.getCoverageSince(t9));
		assertEquals(9267, manager.getCoverageSince(t10));
		
		
		try {
			manager = new CleaningManager("input/rooms2.txt", "input/logs2.txt");
		} catch (FileNotFoundException e) {
			fail();
		}
		
		assertEquals(0, manager.getCoverageSince(t10));
		
	}

}
