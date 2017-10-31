package g8.dbms.simulator.event;

import g8.dbms.simulator.event.enumerator.EventType;

/**
 * Event class. Represents each event in the simulation process.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 * 
 */
public class Event implements Comparable{
	private Query query;		// Query to being processed in the event.
	private EventType type;		// The event's type.
	private Double time;		// The clock time in which the event will happen.

	/**
	 * Class constructor.
	 * @param q Query to being processed in the event.
	 * @param n The event's type.
	 * @param t The clock time in which the event will happen.
	 */
	public Event(double t, EventType n, Query q) {
		this.query = q;
		this.type = n;
		this.time = t;
	}

	/** 
	 * Compares the events by it's clock time.
	 * @param o The event to compare the current event.
	 * @return The comparison's result.
	 */
	public int compareTo(Object o) {
		return this.time.compareTo(((Event) o).time); 
	}
	
	/**
	 * Gets the event's type.
	 * @return The event's type.
	 */
	public EventType getType(){
		return this.type;
	}
	
	/**
	 * Gets the event's query.
	 * @return The event's query.
	 */
	public Query getQuery(){
		return this.query;
	}
	
	/**
	 * Associates a query with the current event.
	 * @param q The query to associate.
	 */
	public void setQuery(Query q){
		query = q;
	}
	
	/**
	 * Gets the event's occur time.
	 * @return The event's occur time.
	 */
	public Double getTime(){
		return time;
	}
}
