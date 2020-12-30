import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;



/**
 * @author Elias Faris
 * version 1.0 9/30/2020
 */


/**
 * 
 * This class sets up the event objects within the hash map. Sets up the name,
 * the time interval, and if the event is a one time or recurring event.
 * This class implements Comparable in order to sort the events throughout
 * the code.
 *
 */
public class Event implements Comparable<Event> {
	String name;
	TimeInterval time;
	String eventType;
	boolean flag = false;

	/**
	 * Event constructor 
	 *
	 * @param name - string name of event object 
	 * @param startDate - start date of event
	 * @param endDate - end date of event
	 * @param startTime - start time of event 
	 * @param endTime - end time
	 * @param eventType - event type
	 * @param flag - flag to check if event is on that day
	 */
	public Event(String name, LocalDate startDate, LocalDate endDate, LocalTime startTime,
			LocalTime endTime, String eventType, boolean flag) {
		this.name = name;
		this.time = new TimeInterval(startDate, endDate, startTime, endTime);
		this.eventType = eventType;
		this.flag = flag;
	}

	/**
	 * 
	 * @return returns flag
	 */
	public boolean getFlag() {return flag;}

	/**
	 *
	 * @return name of event
	 */
	public String getName() {return name;}

	/**
	 * 
	 *
	 * @return time interval
	 *    
	 */
	public TimeInterval getTime() {return time;}

	/**
	 *
	 * @return event type
	 */

	public String getEventType() {return eventType;}

	/**
	 * Checks if two events overlap
	 * 
	 * @param e - Event object being compared
	 * @return boolean true if overlaps, false otherwise
	 */
	public boolean doesOverLap(Event e) {
		return this.getTime().timeOverlap(e.getTime());
	}



	/**
	 * compares time intervals of two events
	 * 
	 * @param e - Event to compare
	 * @return int defining if Event before or after this event
	 */
	public int compareTo(Event e) {
		// Call time interval compares method
		return this.getTime().compareTo(e.getTime());
	}


	/**
	 * Prints start time, end time, and name of event
	 */
	public void printEvent() {
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("H:mm");
		System.out.println(timeformatter.format(getTime().getStartTime()) +
				" - " + timeformatter.format(getTime().getEndTime()) + " " + getName());

	}

	/**
	 * compares two events by name
	 * 
	 * @param n - name of event 
	 * @return boolean
	 */
	public boolean compareEventNames(String n) {
		// Check if the two names are the same
		if (this.getName().equals(n)) {
			return true;
		}
		return false;
	}





}