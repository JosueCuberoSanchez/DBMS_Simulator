package g8.dbms.simulator.module;

import java.util.LinkedList;
import g8.dbms.simulator.Simulation;
import g8.dbms.simulator.event.Event;
import g8.dbms.simulator.event.Query;
import g8.dbms.simulator.event.enumerator.EventType;
import g8.dbms.simulator.event.enumerator.ModuleName;

/**
 * ProcessAdministrator class. Represents the DBMS' Process Administrator module
 * and processes queries as the second DBMS module.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 * 
 */
public class ProcessAdministrator extends Module {

	/**
	 * Class constructor.
	 * @param next A pointer to the next module.
	 * @param s A pointer to the program's simulation.
	 */
	public ProcessAdministrator(Module next, Simulation s) {
		this.isBusy = false;
		this.queryQueue = new LinkedList<Query>();
		this.nextModule = next;
		this.simulation = s;
		this.mn = ModuleName.PROCESSADMINISTRATOR;
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
		query.setCurrentModule(this); //assign current module to the query
		query.setArrivalTimes(clock);
		if(this.isBusy){ //if server is busy(c=1)
			this.queryQueue.add(query);//add to queue//return null event
		} else{ //if server is available
			this.isBusy = true; //set server as busy
			query.setQueueExitTime(clock);
			query.setExitTimes(this.generateExitTime() + clock); //generate service time
			Event newEvent = new Event(query.getExitTimes(1), EventType.EXIT_PROCESS_ADMINISTRATOR, query); //create new Exit event
			simulation.addEvent(newEvent);  //return that event
		}
	}
	
	/**
	 * Process a query's exit from the module.
	 * @param clock The current time in the simulation.
	 */
	public void processExit(Query query, double clock,boolean b){
		if(this.queryQueue.size() > 0){ //if queue is not empty
			Query temporal = this.queryQueue.poll(); //get the first query out(FIFO discipline)
			temporal.setQueueExitTime(clock);
			temporal.setExitTimes(this.generateExitTime() + clock); //set its exit time
			Event newEvent = new Event(temporal.getExitTimes(1), EventType.EXIT_PROCESS_ADMINISTRATOR, temporal); //create new Exit event for it
			this.simulation.addEvent(newEvent); //return it
		} else { //if there is no queue
			this.isBusy = false; //free server
		}
		if(b){
			this.nextModule.processArrival(query, clock);
		}
	}
	
	/**
	 * Generate an event's exit time based on a normal distribution.
	 * @param onlyRead Determines if the current query has the Read Only condition.
	 * @return The new event's service time.
	 */
	public double generateExitTime(){ //generates service time bases on normal distribution(convolution method)
		double result = 0.0;
		for(int i=0;i<12;i++){
			result += super.random.nextDouble();
		}
		result -= 6;
		result = 1.5 + result*0.316227766;
		return result;
	}
	
}