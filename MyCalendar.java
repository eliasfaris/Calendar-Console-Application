import java.time.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.time.format.DateTimeFormatter;

/**
 * @author Elias Faris
 * version 1.0 9/30/2020
 */

/**
 * 
 * The MyCalendar class is the engine of the entire code. It has all the functions that 
 * are required to print the events, delete events, create event, import text file,
 * export text file, portray month or day views, and all other helper functions to 
 * organize code.
 *
 */
public class MyCalendar {

	//HashMap to store LocalDate as key, and ArrayList of events as value
	static TreeMap<LocalDate, ArrayList<Event>> events = new TreeMap<LocalDate, ArrayList<Event>>();
	Scanner sc = new Scanner(System.in);
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
	LocalDate now = LocalDate.now();


	/**
	 *  Creates event
	 */
	public void create() {
		LocalDate date;
		String str;

		// TODO Auto-generated method stub 
		System.out.println("Event name: ");
		String name = sc.nextLine();

		try {
			// Get date of event want to create
			System.out.println("Date of Event in MM/DD/YYYY: ");
			str = sc.nextLine();
			formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
			date = LocalDate.parse(str, formatter);
			formatter = DateTimeFormatter.ofPattern("H:mm");
			//get start and end time 
			System.out.println("Start time in 24-hour military time: ");
			String st = sc.nextLine();
			System.out.println("End time in 24-hour military time: ");
			String et = sc.nextLine();
			// Add one time event to the calendar
			addOneTimeEvent(new Event(name, date, date, LocalTime.parse(st, formatter),
					LocalTime.parse(et, formatter), "One Time", true));
			// Catch any DateTimeFormatter exceptions
		} catch (Exception e) {
			System.out.println(e.getMessage() + " - Invalid date");
		}

	}

	/**
	 * Move to specific day entered.
	 */
	public void goTo() {
		String str;
		LocalDate date;
		System.out.println("Enter a date in the form of MM/DD/YYYY to see events occuring on that day:");
		str = sc.nextLine();
		formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
		try {
			date = LocalDate.parse(str, formatter);
			setCal(date);
			printDayCal();
		} catch (Exception e) {
			System.out.println(e.getMessage() + " - Invalid date");
		}
	}

	/**
	 * Choose how user wants to view either day or month view.
	 */
	public void View() {
		setCal(LocalDate.now());
		System.out.println("[D]ay view or [M]view ?"); 

		String view = sc.nextLine().toUpperCase(); // Read user input
		System.out.println();
		String input = "";
		int val = 0; // Amount to advance calendar by
		while (!input.equals("G")) {
			if (view.equals("D")) { // Day view
				shiftDay(val);
				printDayCal();
			} else if (view.equals("M")) { //month view
				shiftMonth(val); // shift calendar by month
				printMonthCal(); 

			} else {
				try {
					throw new Exception("Input not recognized. Try again");
				} catch (Exception e) {
					System.out.println(e.getMessage() + " - Invalid date");
				}
			}
			System.out.println("\n[P]revious or [N]ext or [G]o back to main menu ?");
			input = sc.nextLine().toUpperCase(); 
			if (input.equals("P")) { 
				val = -1; // Go backwards one day or one month 
			} else if (input.equals("N")) { 
				val = 1;  //go forward one day or month
			} else if (!input.equals("G")) { 
				try {
					throw new Exception("Input not recognized. Try again");
				} catch (Exception e) {
					System.out.println(e.getMessage() + " - Invalid date");
				}
			}

		}
	}

	/**
	 * Delete a one time on a specific day, 
	 * delete all events on that day, or delete a recurring event.
	 */
	public void Delete() {
		String str;
		String input;
		LocalDate date;

		System.out.println("\n[S]elected [A]ll [DR]");
		input = sc.nextLine().toUpperCase(); // Read user input


		System.out.println("Date of Event in MM/DD/YYYY: ");
		str = sc.nextLine();

		formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
		try {
			date = LocalDate.parse(str, formatter);
			setCal(date);
			if (input.equals("S")) { 
				printDayCal();
				System.out.print("Enter name of Event to delete: ");
				String eventName = sc.nextLine();

				try {
					// deletes event if its in the calendar
					remove(date, eventName);
					System.out.println("Event Deleted.");

				} catch (Exception e) {
					System.out.println("Unable to delete event.");
				}
			} else if (input.equals("A")) {
				// Remove all events on given date
				removeAllEvents(date);
				System.out.println("All events deleted on this day.");
			}
			else if(input.equals("DR")) {
				//remove recurring events 

			}
			// Catch any deletion or formatting exceptions
		} catch (Exception e) {
			System.out.println(e.getMessage() + " - Invalid date entered.");
		}



	}

	/**
	 * Prints all events in the calendar
	 */
	public static void eventsList() {

		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("EEEE, MMMM d ");
		// Print year of first event in calendar
		int year = events.firstKey().getYear();
		System.out.println("ONE TIME EVENTS\n");
		System.out.println(year);
		// For every entry in the TreeMap
		for (Entry<LocalDate, ArrayList<Event>> map : events.entrySet()) {
			// Sort the Events ArrayList
			Collections.sort(map.getValue());

			if (map.getKey().getYear() != year) {
				year = map.getKey().getYear();
				System.out.println(year);
			}
			// Print event data for all in the Events ArrayList

			for (Event e : map.getValue()) {

				if(e.getEventType() != "Recurring") {
					System.out.print(" " + dateformatter.format(map.getKey()) + " ");
					e.printEvent();
				}
			}


		}
		System.out.println("\nRECURRING EVENTS\n");

		for (Entry<LocalDate, ArrayList<Event>> map : events.entrySet()) {
			// Sort the Events ArrayList
			Collections.sort(map.getValue());

			// Print event data for all in the Events ArrayList
			for (Event e : map.getValue()) {
				if(e.getEventType() != "One Time") {	
					System.out.print(" " + dateformatter.format(map.getKey()) + " ");
					e.printEvent();
				}
			}


		}

	}



	/**
	 * Retrieves the date at which the calendar is currently at
	 *
	 * @return current date
	 */
	public LocalDate getCal() {return now;}

	/**
	 * Sets the current calendar to a specified date
	 *
	 * @param c - date want Calendar to be moved to
	 */
	public void setCal(LocalDate c) {this.now = c;}
	/**
	 * Retrieve recurring event specific dates for their given date range
	 *
	 * @param days - string of days of event
	 * @param startDate - start date 
	 * @param endDate - end date
	 * @return ArrayList of LocalDate objects in which the one time event occurs
	 */
	public ArrayList<LocalDate> getDays(String days, LocalDate startDate, LocalDate endDate) {
		ArrayList<DayOfWeek> day = new ArrayList<DayOfWeek>();

		// Add proper day to array list
		for (char d : days.toCharArray()) {
			if (d == 'S') {
				day.add(DayOfWeek.SUNDAY);
			}
			else if(d == 'M') {
				day.add(DayOfWeek.MONDAY);
			} 
			else if (d == 'T') {

				day.add(DayOfWeek.TUESDAY);
			} 
			else if (d == 'W') {

				day.add(DayOfWeek.WEDNESDAY);
			} 
			else if (d == 'R') {
				day.add(DayOfWeek.THURSDAY);
			}
			else if (d == 'F'){
				day.add(DayOfWeek.FRIDAY);
			} 
			else if (d == 'A') {
				day.add(DayOfWeek.SATURDAY);
			} 
		}

		// Find the dates corresponding to the days needed and add to ArrayList
		ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
		// Iterate through date range to find days given
		for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
			if (day.contains(d.getDayOfWeek())) {
				dates.add(d);
			}
		}
		return dates;
	}


	/**
	 * imports events from text file into calendar 
	 * 
	 *
	 */
	public void importTextFile() {
		String file = "events.txt";
		// Read in file
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			// Read file line by line
			while ((line = br.readLine()) != null) {
				// First line contains name
				String name = line;
				line = br.readLine();
				char firstChar = line.charAt(0);
				String arr[] = line.split("\\s+");
				// If line starts with letters, indicates recurring event
				if (firstChar >= 'A' && firstChar <= 'Z') {

					formatter = DateTimeFormatter.ofPattern("M/d/yy");
					LocalDate startDate = LocalDate.parse(arr[3], formatter);
					LocalDate endDate = LocalDate.parse(arr[4], formatter);
					formatter = DateTimeFormatter.ofPattern("H:mm");
					// Add event to the calendar
					Reccuring(new Event(name, startDate, endDate, LocalTime.parse(arr[1], formatter),
							LocalTime.parse(arr[2], formatter), "Recurring", true),
							arr[0]);

				} else {

					formatter = DateTimeFormatter.ofPattern("M/d/yy");
					LocalDate startDate, endDate;
					startDate = endDate = LocalDate.parse(arr[0], formatter);
					formatter = DateTimeFormatter.ofPattern("H:mm");
					// Add event to the calendar
					saveImports(new Event(name, startDate, endDate, LocalTime.parse(arr[1], formatter), 
							LocalTime.parse(arr[2], formatter),"One Time", true));
				}
			}
			// If file not found, throw exception
		} catch (FileNotFoundException e) {
			System.out.println("File not found " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
		}
	}


	/**
	 * export events from calendar to output.txt file
	 * 
	 *
	 */
	public static void saveToTextFile() {
		// Creating a File object that represents the disk file.
		PrintStream printer = null;
		try {
			printer = new PrintStream(new File("output.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Error " + e.getMessage());
		}

		// Assign printer to output stream
		System.setOut(printer);
		eventsList();
	}

	/**
	 * Adds a one time event to the calendar
	 *
	 * @param e - Event needed to be added
	 */
	public void addOneTimeEvent(Event e) {
		LocalDate start = e.getTime().getStartDate();
		// Check if the event overlaps with any other events
		if (!Overlap(start, e)) {
			events.putIfAbsent(start, new ArrayList<Event>());
			events.get(start).add(e);
			System.out.println("Event " + e.getName() + " has been added!");
		} else {
			System.out.println("Time overlaps without another event. Could not add " + e.getName());

		}
	}

	/**
	 * Used within imporTtextFile function to check if time overlaps from importing 
	 * from text file.
	 * 
	 * @param e - event wanting to check overlap
	 */

	public void saveImports(Event e) {
		LocalDate start = e.getTime().getStartDate();
		// Check if the event overlaps with any other events
		if (!Overlap(start, e)) {
			events.putIfAbsent(start, new ArrayList<Event>());
			events.get(start).add(e);

		} else {
			System.out.println("Time overlaps. Could not add " + e.getName());

		}
	}

	/**
	 * Adds a recurring event to the calendar
	 *
	 * @param e - Event object want to add
	 * @param days - String of which days want to be added
	 */
	public void Reccuring(Event e, String days) {
		// Get the specific dates between the date range
		ArrayList<LocalDate> dates = getDays(days, e.getTime().getStartDate(), e.getTime().getEndDate());
		for (LocalDate date : dates) {
			//check overlap
			if (!Overlap(date, e)) {
				events.putIfAbsent(date, new ArrayList<Event>());
				events.get(date).add(e);
			}
		}
	}

	/**
	 * Checks if event overlaps 
	 *
	 * @param date - date want to add event
	 * @param eventCheck - Event object want to add
	 * @return boolean 
	 */
	public boolean Overlap(LocalDate date, Event eventCheck) {
		ArrayList<Event> dayEvents = events.get(date);
		if (dayEvents != null) { // There are events on that day
			for (Event event : dayEvents) {
				// Call event doesOverlap method
				if (event.doesOverLap(eventCheck) == true) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Removes an event on a specific date given the name of the Event
	 *
	 * @param date - date want to remove event
	 * @param name - Event name
	 */
	public void remove(LocalDate date, String name) {
		Event e;
		int size = events.get(date).size();
		// Iterates through ArrayList of events on that day and finds the name
		// of wanted event
		for (int i = 0; i < size; i++) {
			e = events.get(date).get(i);
			// If find the event with name given, remove from ArrayList
			if (e.compareEventNames(name) == true) {
				events.get(date).remove(i);
				break;
			}
		}

		if (events.get(date).size() == size) {
			System.out.println("Event not in calendar");
		}
	}

	/**
	 * Removes all events on a given date
	 *
	 * @param date - date want to remove events (LocalDate)
	 */
	public void removeAllEvents(LocalDate date) {
		// Clear all events on given date
		if (events.get(date) != null) {
			events.get(date).clear();
		}
	}



	/**
	 * Advances Calendar by amount of given months
	 *
	 * @param numberOfMonths - number of months to move calendar
	 */
	public void shiftMonth(int numberOfMonths) {
		now = now.plusMonths(numberOfMonths);
	}

	/**
	 * Advances Calendar by days
	 *
	 * @param numberOfDays - number of days want to move Calendar by
	 */
	public void shiftDay(int numberOfDays) {
		now = now.plusDays(numberOfDays);
	}

	/**
	 * Prints beginning monthly calendar
	 */
	public void printMonthCal() {
		// Print Month and Year
		Event e = null;
		String month = now.getMonth().toString();
		month = month.charAt(0) + month.toLowerCase().substring(1, month.length());
		System.out.println(" " + month + " " + now.getYear());
		// Print Days of Week
		System.out.println("Su Mo Tu We Th Fr Sa");
		LocalDate x = LocalDate.of(now.getYear(), now.getMonth(), 1);
		String space = "";
		// Add space depending on what day the 1st falls on
		for (int i = 0; i < x.getDayOfWeek().getValue(); i++) {
			space += " ";
		}
		System.out.print(space);

		// Print every date in the month, and portray today with brackets



		for (int i = 0; i < now.lengthOfMonth(); i++) {


			if (x.equals(LocalDate.now())) {
				System.out.printf("[%2s] ", x.getDayOfMonth());
			}
			else{
				System.out.printf("{%2s} ", x.getDayOfMonth());
			}
			// Go to next day
			x = x.plusDays(1);
			if (x.getDayOfWeek() == DayOfWeek.SUNDAY) {
				System.out.print("\n");
			}
		}

		System.out.println();
	}

	/**
	 * This function prints out beginning calendar.
	 */
	public void printCal() {
		// Print Month and Year
		String month = now.getMonth().toString();
		month = month.charAt(0) + month.toLowerCase().substring(1, month.length());
		System.out.println(" " + month + " " + now.getYear());
		// Print Days of Week
		System.out.println("Su Mo Tu We Th Fr Sa");
		LocalDate x = LocalDate.of(now.getYear(), now.getMonth(), 1);
		String space = "";
		// Add space depending on what day the 1st falls on
		for (int i = 0; i < x.getDayOfWeek().getValue(); i++) {
			space += " ";
		}
		System.out.print(space);

		// Print every date in the month, and portray today with brackets



		for (int i = 0; i < now.lengthOfMonth(); i++) {


			if (x.equals(LocalDate.now())) {
				System.out.printf("[%2s] ", x.getDayOfMonth());
			}
			else{
				System.out.printf("%2s ", x.getDayOfMonth());
			}
			// Go to next day
			x = x.plusDays(1);
			if (x.getDayOfWeek() == DayOfWeek.SUNDAY) {
				System.out.print("\n");
			}
		}

		System.out.println();
	}




	/**
	 * Prints events on day
	 *
	 */
	public void printDayCal() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM d, yyyy");
		System.out.println(" " + formatter.format(now));
		// events on day
		ArrayList<Event> eventsOnDay = events.get(now);
		if (eventsOnDay != null) {

			Collections.sort(eventsOnDay);

			for (Event event : eventsOnDay) {
				event.printEvent();
			}
		}
		System.out.println();
	}


}

