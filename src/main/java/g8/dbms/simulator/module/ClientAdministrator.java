package g8.dbms.simulator.module;

import java.util.LinkedList;

import g8.dbms.simulator.Simulation;
import g8.dbms.simulator.event.Event;
import g8.dbms.simulator.event.Query;
import g8.dbms.simulator.event.enumerator.EventType;
import g8.dbms.simulator.event.enumerator.ModuleName;


/**
 * ClientAdministrator class. Represents the DBMS' Client Administrator module
 * and processes queries as the first DBMS module.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 * 
 */
public class ClientAdministrator extends Module {
	private double timeout;
	private int totalDiscarted; //total amount of discarded queries due to full servers
	private int index;							// Generates the ID for each query.
	private int attemptedConnections;

	/**
	 * Class constructor
	 * @param k Total servers in the module.
	 * @param next A pointer to the next module.
	 * @param s A pointer to the program's simulation.
	 * @param to The limit timeout for each connection.
	 */
	public ClientAdministrator(int k, Module next, Simulation s, double to) {
		
		this.capacity = k;
		this.totalBusy = 0;
		this.totalDiscarted = 0;
		this.nextModule = next;
		this.simulation = s;
		this.timeout = to;
		this.mn= ModuleName.CLIENTADMINISTRATOR;
		this.index = 1;
		this.queryQueue = new LinkedList<Query>();
		this.totalArrived = 0;
		this.attemptedConnections = 0;
	}
	
	/**
	 * Process a new query arrival to the module to generate it's service (exit) time and
	 * creates a new event with it.
	 * @param query The query to process it's arrival.
	 * @param clock The current time in the simulation.
	 */
	public void processArrival(Query query, double clock){
		this.attemptedConnections++;
		query.setCurrentModule(this); //assign current module to the query
		query.setArrivalTimes(clock);
		if(this.totalBusy == this.capacity){ //if all servers are busy
			this.totalDiscarted++; //discard query
			this.simulation.getRunningWindow().updateDiscarded();
		} else { //else if there are available servers
		    this.totalArrived++;
			this.totalBusy++; 
			query.setQueueExitTime(clock);
			query.setExitTimes(this.generateServiceTime(query.isOnlyRead())+clock); //generate exit time for this module
			Event newEvent = new Event(query.getExitTimes(0), EventType.EXIT_CLIENT_ADMINISTRATOR, query); //make new event
			Event kill= new Event(clock + timeout, EventType.TIMEOUT, query); // Creates a kill event to terminate the query if it's prosecution is
			  // not finished before the timeout is reached.
			this.simulation.addEvent(kill);
			this.simulation.addEvent(newEvent); //return it
		}
		this.createEvent(clock);
	}
	
	/**
	 * Creates a new event with a query to adds in the simulation events queue.
	 * @param clock The current system time.
	 */
	public void createEvent(double clock){
		double r = random.nextDouble();
		double time = (-Math.log(r))/(35.0/60.0); // Exponential time distribution.
		double t = super.random.nextDouble();
		//System.out.println(t);
		Query q = new Query(t, this.index); // Generates a new query whose type depends on the random number sent.
		this.index++;
		q.setCurrentModule(this);
		Event temp = new Event(clock + time, EventType.ARRIVAL, q); // Creates an arrival connection event.
		this.simulation.addEvent(temp);		
	}
	
	/**
	 * Process a query's exit from the module.
	 * @param clock The current time in the simulation.
	 */
	public void processExit(Query query, double clock,boolean b){
		this.totalBusy--; 
		if(b){
			this.nextModule.processArrival(query, clock);
		}
	}
	
	/**
	 * Generate an event's exit time based on an uniform distribution.
	 * @param onlyRead Determines if the current query has the Read Only condition.
	 * @return The new event's service time.
	 */
	public double generateServiceTime(boolean onlyRead){
		return (0.01+(0.05-0.01)*super.random.nextDouble()); //generates service time based on uniform distribution
	}
	
	/**
	 * Get the total discarded connections.
	 * @return The total amount of connections that couldn't enter in the module.
	 */
	public int getTotalDiscarded(){
		return this.totalDiscarted;
	}
	
	/**
	 * Deletes an event with a query if it exceeds the timeout.
	 * @param q The query to kill.
	 */
	public void kill(Query q){
		this.simulation.removeEvent(q);
		this.simulation.addQueryToStatistics(q);
	}
	
	
	/**
	 * Resets the module data.
	 */
	public void reset(){
		super.reset();
		this.index = 1;
		this.attemptedConnections = 0;
		this.totalDiscarted = 0;
	}
	
	/**
	 * Gets the total connections that tried to enter to the system.
	 * @return The total attempted connections.
	 */
	public int getAttemptedConnections(){
		return this.attemptedConnections;
	}
}