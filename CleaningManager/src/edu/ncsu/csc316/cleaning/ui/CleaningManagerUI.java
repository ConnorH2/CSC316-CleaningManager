package edu.ncsu.csc316.cleaning.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc316.cleaning.manager.ReportManager;

public class CleaningManagerUI {

	private static ReportManager manager;
	
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
				}
			}
			
			
			// Prompt the user for a command
			System.out.print("\n\nFiles loaded successfully.\nPlease enter one of the following reports: \n");
			System.out.print("\"frequency\" - Creates a report showing a specified number of the most frequently cleaned rooms\n");
			System.out.print("\"cleanings\" - Creates a report showing each time every room was cleaned\n");
			System.out.print("\"baglife\" - Creates a report showing the estimated remaing vacuum bag life\n");
			System.out.print("\nPlease enter a command(frequency/cleanings/baglife): ");
			
		} catch(Exception e) {
			System.out.println("F*CK");
			input.close();
			e.printStackTrace();
		}
	}

}
