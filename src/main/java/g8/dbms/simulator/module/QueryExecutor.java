package g8.dbms.simulator.module;

import g8.dbms.simulator.Simulation;
import g8.dbms.simulator.event.Event;
import g8.dbms.simulator.event.Query;
import g8.dbms.simulator.event.enumerator.EventType;
import g8.dbms.simulator.event.enumerator.ModuleName;

import java.util.LinkedList;

/**
 * QueryExecutor class. Represents the DBMS' Query Executor
 * and processes queries as the fifth module.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 * 
 */
public class QueryExecutor extends Module {

	/**
	 * Class constructor.
	 * @param m Total servers in the module.
	 * @param s A pointer to the program's simulation.
	 */
	public QueryExecutor(int m, Simulation s) {
		this.capacity = m;
		this.queryQueue = new LinkedList<Query>();
		this.totalBusy = 0;
		this.nextModule = null;
		this.simulation = s;
		this.mn = ModuleName.QUERYEXECUTOR;
		this.totalArrived = 0;
	}

	/**
	 * Process a new query arrival to the module to generate it's service (exit) time and
	 * creates a new event with it.
	 * @param query The query to process it's arrival.
	 * @param clock The current time in the simulation.
	 */
	public void processArrival(Query query, double clock){
		this.totalArrived++;
		query.setCurrentModule(this); //assign current module to the query.
		query.setArrivalTimes(clock);
		if(this.capacity == this.totalBusy){ //if servers are busy
			this.queryQueue.add(query); //add query to queue
		} else { //if servers are not busy
			this.totalBusy++;
			query.setQueueExitTime(clock);
			query.setExitTimes(this.generateExitTime(query) + clock); //generate service time
			Event newEvent = new Event(query.getExitTimes(4),EventType.EXIT_QUERY_EXECUTOR,query); //create new exit event
			this.simulation.addEvent(newEvent); //returns it
		}
	}
	
	/**
	 * Process a query's exit from the module.
	 * @param clock The current time in the simulation.
	 */
	public void processExit(Query query, double clock,boolean b){
		if(this.queryQueue.size() > 0){ //if there is a queue
			Query temporal = this.queryQueue.poll(); //get the first query (FIFO discipline)
			temporal.setQueueExitTime(clock);
			temporal.setExitTimes(this.generateExitTime(temporal)+clock); //generate it's exit time
			Event newEvent = new Event(temporal.getExitTimes(4),EventType.EXIT_QUERY_EXECUTOR,temporal); //create it's exit event
			this.simulation.addEvent(newEvent); //return it's event
		} else { //if there was no queue
			this.totalBusy--;
		}
		query.getCurrentModule().killTimeout(query);
	}
	
	/**
	 * Generate an event's exit time based on the amount of time that the total blocks from disk needs to
	 * being loaded due to it's size. The total blocks loaded depends on the query type.
	 * @param Query The event's query to generate it's exit time.
	 * @return The new event's service time.
	 */
	public double generateExitTime(Query query){
		double time = Math.pow(query.getTotalBlocks(), 2);
		time /= 1000;
		switch(query.getType()){
		case DDL:
			time += 0.5;
			break;
		case UPDATE:
			time++;
			break;
		default:
			break;
		}
		return time;
	}
}
