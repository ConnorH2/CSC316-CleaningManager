/**
 * 
 */
package edu.ncsu.csc316.cleaning.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for ReportManager
 * @author Connor Hekking
 */
class ReportManagerTest {

	/** ReportManager object for testing. */
	ReportManager manager;
	
	@BeforeEach
    public void setUp() {
        try {
			manager = new ReportManager("input/rooms1.txt", "input/logs1.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }

	/**
	 * Test method for {@link edu.ncsu.csc316.cleaning.manager.ReportManager#getVacuumBagReport(java.lang.String)}.
	 */
	@Test
	void testGetVacuumBagReport() {
		String t0 = "10/31/2023 13:20:43";
		String t1 = "09/13/2023 20:08:06";
		String t2 = "09/13/2023 20:08:04";
		String t3 = "09/12/2023 00:00:00";
		String t4 = "09/09/2023 11:52:48";
		String t5 = "09/09/2023 09:30:40";
		String t6 = "09/08/2023 20:14:52";
		String t7 = "09/08/2023 20:14:51";
		String t8 = "09/03/2023 20:41:13";
		
		
		assertEquals("Vacuum Bag Report (last replaced 10/31/2023 13:20:43) [\n"
				+ "   Bag is due for replacement in 5280 SQ FT\n]", manager.getVacuumBagReport(t0));
		assertEquals("Vacuum Bag Report (last replaced 09/13/2023 20:08:06) [\n"
				+ "   Bag is due for replacement in 5280 SQ FT\n]", manager.getVacuumBagReport(t1));
		assertEquals("Vacuum Bag Report (last replaced 09/13/2023 20:08:04) [\n"
				+ "   Bag is due for replacement in 5216 SQ FT\n]", manager.getVacuumBagReport(t2));
		assertEquals("Vacuum Bag Report (last replaced 09/12/2023 00:00:00) [\n"
				+ "   Bag is due for replacement in 5099 SQ FT\n]", manager.getVacuumBagReport(t3));
		assertEquals("Vacuum Bag Report (last replaced 09/09/2023 11:52:48) [\n"
				+ "   Bag is due for replacement in 4710 SQ FT\n]", manager.getVacuumBagReport(t4));
		assertEquals("Vacuum Bag Report (last replaced 09/09/2023 09:30:40) [\n"
				+ "   Bag is due for replacement in 680 SQ FT\n]", manager.getVacuumBagReport(t5));
		assertEquals("Vacuum Bag Report (last replaced 09/08/2023 20:14:52) [\n"
				+ "   Bag is due for replacement in 680 SQ FT\n]", manager.getVacuumBagReport(t6));
		assertEquals("Vacuum Bag Report (last replaced 09/08/2023 20:14:51) [\n"
				+ "   Bag is due for replacement in 631 SQ FT\n]", manager.getVacuumBagReport(t7));
		assertEquals("Vacuum Bag Report (last replaced 09/03/2023 20:41:13) [\n"
				+ "   Bag is due for replacement in 233 SQ FT\n]", manager.getVacuumBagReport(t8));
		
		String t9 = "09/01/2023 10:56:33";
		String t10 = "09/08/1993 20:14:51";
		
		assertEquals("Vacuum Bag Report (last replaced 09/01/2023 10:56:33) [\n"
				+ "   Bag is overdue for replacement!\n]", manager.getVacuumBagReport(t9));
		assertEquals("Vacuum Bag Report (last replaced 09/08/1993 20:14:51) [\n"
				+ "   Bag is overdue for replacement!\n]", manager.getVacuumBagReport(t10));
		
		
		String t11 = "09/08/202320:14:52";
		String t12 = "09-08/2023 20:14:52";
		String t13 = "09-08-2023 20:14:52";
		String t14 = "09:08:2023 20/14/52";
		String t15 = "09/08/2023 20:14:62";
		
		assertEquals("Date & time must be in the format: MM/DD/YYYY HH:MM:SS", manager.getVacuumBagReport(t11));
		assertEquals("Date & time must be in the format: MM/DD/YYYY HH:MM:SS", manager.getVacuumBagReport(t12));
		assertEquals("Date & time must be in the format: MM/DD/YYYY HH:MM:SS", manager.getVacuumBagReport(t13));
		assertEquals("Date & time must be in the format: MM/DD/YYYY HH:MM:SS", manager.getVacuumBagReport(t14));
		assertEquals("Date & time must be in the format: MM/DD/YYYY HH:MM:SS", manager.getVacuumBagReport(t15));
		
		
		assertThrows(FileNotFoundException.class, () -> new ReportManager("input/rooms0.txt", "input/logs0.txt"));
		assertThrows(NoSuchElementException.class, () -> new ReportManager("input/rooms3.txt", "input/logs3.txt"));
		
		try {
			manager = new ReportManager("input/rooms2.txt", "input/logs2.txt");
		} catch (FileNotFoundException e) {
			fail();
		}
		
		assertEquals("Vacuum Bag Report (last replaced 09/08/1993 20:14:51) [\n"
				+ "   Bag is due for replacement in 5280 SQ FT\n]", manager.getVacuumBagReport(t10));
	}

	/**
	 * Test method for {@link edu.ncsu.csc316.cleaning.manager.ReportManager#getFrequencyReport(int)}.
	 */
	@Test
	void testGetFrequencyReport() {
		// Test different valid nums of cleanings
		assertEquals(
				"Frequency of Cleanings [\n"
				+ "   Dining Room has been cleaned 6 times\n"
				+ "   Kitchen has been cleaned 6 times\n"
				+ "   Living Room has been cleaned 2 times\n"
				+ "   Main Bathroom has been cleaned 2 times\n"
				+ "   Main Bedroom has been cleaned 2 times\n"
				+ "   Entryway has been cleaned 0 times\n"
				+ "]", manager.getFrequencyReport(6));
		
		assertEquals(
				"Frequency of Cleanings [\n"
				+ "   Dining Room has been cleaned 6 times\n"
				+ "   Kitchen has been cleaned 6 times\n"
				+ "   Living Room has been cleaned 2 times\n"
				+ "   Main Bathroom has been cleaned 2 times\n"
				+ "   Main Bedroom has been cleaned 2 times\n"
				+ "]", manager.getFrequencyReport(5));
		
		assertEquals(
				"Frequency of Cleanings [\n"
				+ "   Dining Room has been cleaned 6 times\n"
				+ "]", manager.getFrequencyReport(1));
		
		assertEquals(
				"Frequency of Cleanings [\n"
				+ "   Dining Room has been cleaned 6 times\n"
				+ "   Kitchen has been cleaned 6 times\n"
				+ "   Living Room has been cleaned 2 times\n"
				+ "   Main Bathroom has been cleaned 2 times\n"
				+ "]", manager.getFrequencyReport(4));
		
		assertEquals(
				"Frequency of Cleanings [\n"
				+ "   Dining Room has been cleaned 6 times\n"
				+ "   Kitchen has been cleaned 6 times\n"
				+ "   Living Room has been cleaned 2 times\n"
				+ "]", manager.getFrequencyReport(3));
		
		// Test boundary values
		assertEquals(
				"Frequency of Cleanings [\n"
				+ "   Dining Room has been cleaned 6 times\n"
				+ "   Kitchen has been cleaned 6 times\n"
				+ "   Living Room has been cleaned 2 times\n"
				+ "   Main Bathroom has been cleaned 2 times\n"
				+ "   Main Bedroom has been cleaned 2 times\n"
				+ "   Entryway has been cleaned 0 times\n"
				+ "]", manager.getFrequencyReport(7));
		
		assertEquals(
				"Frequency of Cleanings [\n"
				+ "   Dining Room has been cleaned 6 times\n"
				+ "   Kitchen has been cleaned 6 times\n"
				+ "   Living Room has been cleaned 2 times\n"
				+ "   Main Bathroom has been cleaned 2 times\n"
				+ "   Main Bedroom has been cleaned 2 times\n"
				+ "   Entryway has been cleaned 0 times\n"
				+ "]", manager.getFrequencyReport(999));
		
		assertEquals("Number of rooms must be greater than 0.", manager.getFrequencyReport(0));
		assertEquals("Number of rooms must be greater than 0.", manager.getFrequencyReport(-1));
		assertEquals("Number of rooms must be greater than 0.", manager.getFrequencyReport(-6));
		
		
		// test with no cleanings
		try {
			manager = new ReportManager("input/rooms2.txt", "input/logs2.txt");
		} catch (FileNotFoundException e) {
			fail();
		}
		
		assertEquals("No rooms have been cleaned.", manager.getFrequencyReport(5));
	}

	/**
	 * Test method for {@link edu.ncsu.csc316.cleaning.manager.ReportManager#getRoomReport()}.
	 */
	@Test
	void testGetRoomReport() {
		assertEquals(
				"Room Report [\n"
				+ "   Dining Room was cleaned on [\n"
				+ "      09/13/2023 20:08:05\n"
				+ "      09/11/2023 20:21:19\n"
				+ "      09/08/2023 20:14:52\n"
				+ "      09/06/2023 20:19:39\n"
				+ "      09/04/2023 20:29:12\n"
				+ "      09/01/2023 20:00:00\n"
				+ "   ]\n"
				+ "   Entryway was cleaned on [\n"
				+ "      (never cleaned)\n"
				+ "   ]\n"
				+ "   Kitchen was cleaned on [\n"
				+ "      09/13/2023 19:02:55\n"
				+ "      09/11/2023 19:09:10\n"
				+ "      09/08/2023 19:12:16\n"
				+ "      09/06/2023 19:23:09\n"
				+ "      09/04/2023 19:31:31\n"
				+ "      09/01/2023 19:00:00\n"
				+ "   ]\n"
				+ "   Living Room was cleaned on [\n"
				+ "      09/09/2023 13:59:58\n"
				+ "      09/02/2023 13:06:45\n"
				+ "   ]\n"
				+ "   Main Bathroom was cleaned on [\n"
				+ "      09/09/2023 12:46:42\n"
				+ "      09/02/2023 10:45:21\n"
				+ "   ]\n"
				+ "   Main Bedroom was cleaned on [\n"
				+ "      09/09/2023 10:20:52\n"
				+ "      09/02/2023 08:18:39\n"
				+ "   ]\n"
				+ "]", manager.getRoomReport());
		
		try {
			manager = new ReportManager("input/rooms2.txt", "input/logs2.txt");
		} catch (FileNotFoundException e) {
			fail();
		}
		
		assertEquals("No rooms have been cleaned.", manager.getRoomReport());
		
		
		try {
			manager = new ReportManager("input/rooms4.txt", "input/logs4.txt");
		} catch (FileNotFoundException e) {
			fail();
		}
		
		assertEquals(
				"Room Report [\n"
				+ "   Attic was cleaned on [\n"
				+ "      09/13/2023 20:08:05\n"
				+ "   ]\n"
				+ "   Loft was cleaned on [\n"
				+ "      09/13/2023 19:02:55\n"
				+ "   ]\n"
				+ "]", manager.getRoomReport());
		
	}

}
