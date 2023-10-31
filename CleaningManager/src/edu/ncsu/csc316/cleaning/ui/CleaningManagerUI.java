package edu.ncsu.csc316.cleaning.ui;

import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import edu.ncsu.csc316.cleaning.manager.ReportManager;

/**
 * CleaningManagerUI contains the main function which executes the CleaningManager program, 
 * reading in files and executing commands read from the command line.
 * @author Connor Hekking
 */
public class CleaningManagerUI {

	/** ReportManager object for use in generating reports. */
	private static ReportManager manager;
	
	/**
	 * Main function of CleaningManager which first accepts input room and log files, 
	 * then executes one of the following four commands: 
	 * "frequency" - Creates a report showing a specified number of the most frequently cleaned rooms. 
	 * "cleanings" - Creates a report showing each time every room was cleaned. 
	 * "baglife" - Creates a report showing the estimated remaing vacuum bag life. 
	 * "quit" - Exits the program.
	 * @param args The command line arguments given with the execution of the program, 
	 * none are needed for this program.
	 */
	public static void main(String[] args) {
		
		// Create a scanner for console input
		Scanner input = new Scanner(System.in);
		
		// Nest everything else in a try/catch block to ensure we always close the scanner
		try {
		
			// Create a boolean flag for correct file input
			boolean managerCreated = false;
			
			// Continue prompting the user until they give correct files
			while(!managerCreated) {
				try {
					// Prompt the user for the input files
					System.out.print("Please enter the room information file: ");
					String roomStr = input.next();
					
					System.out.print("Please enter the cleaning event information file: ");
					String cleaningStr = input.next();
					
					// Attempt to create a ReportManager using the given files
					manager = new ReportManager(roomStr, cleaningStr);
					// No exception was thrown, files read successfully
					managerCreated = true;
				} catch (FileNotFoundException e) {
					System.out.println("One or both of the file names was incorrect, please re-enter the files.");
				} catch (DateTimeParseException e) {
					System.out.println("One or both of the file names was incorrect, please re-enter the files.");
				}
			}
			
			
			// Prompt the user for a command
			System.out.print("\n\nFiles loaded successfully.\nPlease enter one of the following commands: \n");
			System.out.print("\"frequency\" - Creates a report showing a specified number of the most frequently cleaned rooms\n");
			System.out.print("\"cleanings\" - Creates a report showing each time every room was cleaned\n");
			System.out.print("\"baglife\" - Creates a report showing the estimated remaing vacuum bag life\n");
			System.out.print("\"quit\" - Exits the program\n");
			System.out.print("\nPlease enter a command(frequency/cleanings/baglife/quit): ");
			
			String command1 = input.next();
			
			while(command1.compareTo("quit") != 0) {
				
				
				if(command1.compareTo("frequency") == 0) {
					// Get the number of rooms to include in the report
					System.out.print("\nPlease enter the number of rooms you wish to include in the frequency report: ");
					String command2 = input.next();
					
					// Parse the number into an int
					int num = Integer.parseInt(command2); // TODO what happens if this throws an exception?
					
					// Print the string returned by getFrequencyReport
					System.out.print("\n" + manager.getFrequencyReport(num) + "\n");
					
				} else if(command1.compareTo("cleanings") == 0) {
					// Print the string returned by getRoomReport
					System.out.print("\n" + manager.getRoomReport() + "\n");
					
				} else if(command1.compareTo("baglife") == 0) {
					// Get the time since the last bag replacement
					System.out.print("\nPlease enter the date and time that the vacuum bag was last replaced in format \"MM/DD/YYYY HH:MM:SS\": ");
					String command2 = input.next() + " " + input.next();
					
					// Print the string returned by getVaccumBagReport
					System.out.print("\n" + manager.getVacuumBagReport(command2) + "\n");
					
				} else {
					// Command not recognized
					System.out.print("\nInvalid command.\n");
				}
				
				// Prompt the user for the next command
				System.out.print("\nPlease enter a command(frequency/cleanings/baglife/quit): ");
				
				command1 = input.next();
			}
			
			
		} catch(Exception e) {
			input.close();
			e.printStackTrace();
		}
	}

}
