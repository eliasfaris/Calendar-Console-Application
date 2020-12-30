import java.util.Scanner;


/**
 * @author Elias Faris
 * version 1.0 9/30/2020
 */


public class MyCalendarTester {


	public static void main(String[] args) {

		MyCalendar cal = new MyCalendar();

		cal.importTextFile(); //grab all information from text file to fill calendar

		System.out.println("Loading is done!\n");

		cal.printCal(); //prints calendar month view for user

		Scanner sc = new Scanner(System.in);
		String input = "";

		while(!input.equals("Q")) {

			//main menu
			System.out.println("\nSelect one of the following main menu options:");
			System.out.println("[V]iew by [C]reate, [G]o to [E]vent list [D]elete [Q]uit");

			input = sc.nextLine().toUpperCase(); // Read user input

			if(input.equals("V")) {cal.View(); }

			else if(input.equals("C")) {cal.create(); }

			else if(input.equals("G")) {cal.goTo(); }

			else if(input.equals("E")) {
				if(MyCalendar.events != null) {
					cal.eventsList();

				}
				else {
					System.out.println("No events on calendar.");
					break;
				}

			}
			else if(input.equals("D")) {cal.Delete(); }

			else if(input.equals("Q")) {break;}

			else {
				System.out.println("Input not recognized. Try: C, V, G, E, D, Q.");
			}

		}

		System.out.println("Good Bye");

		MyCalendar.saveToTextFile();  //save all events from calendar to output.txt

		sc.close(); //close scanner


	}


}