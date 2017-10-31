package g8.dbms.simulator.module;

import java.util.Queue;
import java.util.Random;

import g8.dbms.simulator.Simulation;
import g8.dbms.simulator.event.Query;
import g8.dbms.simulator.event.enumerator.ModuleName;

/**
 * Module abstract class. Represents the DBMS' modules and is used to implements it's method in a different way
 * depending on the module. It processes queries as the DBMS's modules.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 * 
 */
public abstract class Module {
	protected int capacity;					// Total of servers specified by the user for each module.
	protected int totalBusy;				// Total of current busy servers (for each module with multiple servers).
	protected boolean isBusy;				// Defines if the server is busy or not (for each module with only one served by default).
	protected Queue<Query> queryQueue;		// The queue of waiting queries in each module.
	protected Random random;				// Random number generator to work with the random values generator for each random
											// variable's time distribution calculation in the modules.
	protected Module nextModule;			// A pointer to the current module's next module.
	protected Simulation simulation;		// A pointer to the program's simulation.
	protected ModuleName mn;				// The current module's name.
	protected int totalArrived;				// The total arrived queries to the current module.
	protected double[] timesByQuery;		// The total permanence in the module time by query type.
	protected int[] arrivesByQuery;			// The total arrivals to the module by query type.
	
	/**
	 * Class constructor. Initializes some variables and arrays.
	 */
	public Module(){
		random = new Random();
		timesByQuery = new double[4];
		arrivesByQuery = new int[4];
	}
	
	/**
	 * Process a new query arrival to the module to generate it's service (exit) time and
	 * creates a new event with it.
	 * @param query The query to process it's arrival.
	 * @param clock The current time in the simulation.
	 * @return null If there are no available servers.
	 * @return newEvent Exit event if the query was served.
	 */
	public void processArrival(Query query,double clock){}
	
	/**
	 * Process a query's exit from the module.
	 * @param clock The current time in the simulation.
	 * @return newEvent If there are a query waiting in the queue to be served and it's exit time is generated.
	 * @return null If there are no queries waiting in the module's queue.
	 */
	public void processExit(Query query, double clock,boolean b){}
	
	/**
	 * Generate an event's exit time based on a discrete or continuous distribution.
	 * @param onlyRead Determines if the current query has the Read Only condition.
	 * @return The new event's service time.
	 */
	public double generateServiceTime(boolean onlyRead){
		return 0.0;
	}
	
	
	/**
	 * Gets the current module's busy servers.
	 * @return The servers busy.
	 */
	public int getTotalBusy(){
		return this.totalBusy;
	}
	
	/**
	 * Gets the current module's server state (if this have just one server)
	 * @return True if the server if busy, false if not.
	 */
	public boolean isBusy(){
		return isBusy;
	}
	
	/**
	 * Gets the total servers in the current module.
	 * @return The total servers.
	 */
	public int getCapacity(){
		return this.capacity;
	} 
	
	/**
	 * Gets the current module's queue size.
	 * @return The queue size.
	 */
	public int getQueueSize(){
		return queryQueue.size();
	}
	
	/**
	 * Gets the total queries arrived to the current module.
	 * @return The total queries arrived.
	 */
	public int getTotalArrived(){
		return this.totalArrived;
	}
	
	/**
	 * Deletes an event with a query if it exceeds the timeout.
	 * @param q The query to kill.
	 */
	public void kill(Query q){
		boolean killed = false;
		for(Query qu : this.queryQueue){
			if(q.getID() == qu.getID()){
				killed = true;
				this.queryQueue.remove(qu);
				break;
			}
		}
		if (!killed){
			simulation.removeEvent(q);
			if(this.queryQueue.isEmpty()){
				this.totalBusy--;
				this.isBusy = false;
			} else {
				q.getCurrentModule().processExit(q, q.getExitTimes(q.getCurrentModule().getModuleName().ordinal()),false);
			}
		}
		this.simulation.addKilledQueryToStatistics(q);
	}
	
	/**
	 * Deletes the timeout event with a query if that query has finished the process in the
	 * simulation to avoid and accidental deletion of that query.
	 * @param q The query that reference the timeout event to kill.
	 */
	public void killTimeout(Query q){
		boolean killed = false;
		for(Query qu : this.queryQueue){
			if(q.getID() == qu.getID()){
				killed = true;
				this.queryQueue.remove(qu);
				break;
			}
		}
		if (!killed) simulation.removeEvent(q);
	}
	
	/**
	 * Gets the current module's name.
	 * @return The module's name.
	 */
	public ModuleName getModuleName(){
		return mn;
	}
	
	/**
	 * Resets the module data.
	 */
	public void reset(){
		this.totalBusy = 0;			
		this.totalArrived = 0;			
		this.isBusy = false;				
		this.queryQueue.clear();	
	}
}
