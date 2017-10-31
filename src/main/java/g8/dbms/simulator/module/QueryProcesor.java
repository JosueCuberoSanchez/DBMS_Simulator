package g8.dbms.simulator.module;

import java.util.LinkedList;
import java.util.Random;

import g8.dbms.simulator.Simulation;
import g8.dbms.simulator.event.Event;
import g8.dbms.simulator.event.Query;
import g8.dbms.simulator.event.enumerator.EventType;
import g8.dbms.simulator.event.enumerator.ModuleName;

/**
 * QueryProcessor class. Represents the DBMS' Query Processor module
 * and processes queries as the third DBMS module.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 * 
 */
public class QueryProcesor extends Module {

	/**
	 * Class constructor
	 * @param n Total servers in the module.
	 * @param next A pointer to the next module.
	 * @param s A pointer to the program's simulation.
	 */
	public QueryProcesor(int n, Module next, Simulation s) {
		this.capacity = n;
		this.queryQueue = new LinkedList<Query>();
		this.nextModule = next; 
		this.simulation = s;
		this.mn = ModuleName.QUERYPROCESSOR;
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
		if(this.capacity == this.totalBusy){ //if servers are busy
			this.queryQueue.add(query); //add query to queue
		} else { //if servers are not busy
			this.totalBusy++;
			query.setQueueExitTime(clock);
			query.setExitTimes(this.generateExitTime(query.isOnlyRead())+clock); //generate service time
			Event newEvent = new Event(query.getExitTimes(2),EventType.EXIT_QUERY_PROCESSOR,query); //create new exit event
			this.simulation.addEvent(newEvent); //returns it
		}
	}
	
	/**
	 * Process a query's exit from the module.
	 * @param clock The current time in the simulation.
	 */
	public void processExit(Query query ,double clock,boolean b){
		if(this.queryQueue.size() > 0){ //if there is a queue
			Query temporal = this.queryQueue.poll(); //get the first query (FIFO discipline)
			temporal.setQueueExitTime(clock);
			temporal.setExitTimes(this.generateExitTime(temporal.isOnlyRead())+clock); //generate its exit time
			Event newEvent = new Event(temporal.getExitTimes(2),EventType.EXIT_QUERY_PROCESSOR,temporal); //create its exit event
			this.simulation.addEvent(newEvent); //return it's event
		} else { //if there was no queue
			this.totalBusy--; 			
		}
		if(b){
			this.nextModule.processArrival(query, clock);
		}
	}
	
	/**
	 * Generate an event's exit time based on a normal distribution.
	 * @param onlyRead Determines if the current query has the Read Only condition.
	 * @return The new event's service time created by the validation times.
	 */
	public double generateExitTime(boolean readOnly){ //generates the summation of all phases
		return generateLexicValidation()+generateSintacticValidation()+generateSemanticValidation()+
				generatePermissionVerification()+queryOptimization(readOnly);
	}
	
	/**
	 * Generates the lexical validation duration based on a Monte Carlo discrete distribution.
	 * @return The validation time.
	 */
	public double generateLexicValidation(){ //generates time using Monte Carlo
		super.random = new Random();
		double result = random.nextDouble();
		if(result < 0.7){
			result = 0.1;
		} else {
			result = 0.4;
		}
		return result;
	}
	
	/**
	 * Generates the syntactic validation duration based on an uniform discrete distribution.
	 * @return The validation time.
	 */
	public double generateSintacticValidation(){ //generates time using uniform distribution
		super.random = new Random();
		return 0.8*random.nextDouble();
	}
	
	/**
	 * Generates the semantic validation duration based on a normal continuous distribution.
	 * @return The validation time.
	 */
	public double generateSemanticValidation(){ //generates time using normal distribution(convolution method)
		super.random = new Random();
		double result = 0.0;
		for(int i=0;i<12;i++){
			result += random.nextDouble();
		}
		result -= 6;
		result = 1 + result*0.7071067812;
		return result;
	}
	
	/**
	 * Generates the permission verification duration based on an exponential continous distribution.
	 * @return The validation time.
	 */
	public double generatePermissionVerification(){ //generates time using exponential distribution
		super.random = new Random();
		return (-1*Math.log(random.nextDouble()))/(0.7); 
	}
	
	/**
	 * Generates the query optimization time duration based on the query type.
	 * @param readOnly Used to calculate the optimization time based on the query's Read Only condition.
	 * @return The optimization time.
	 */
	public double queryOptimization(boolean readOnly){ //generates time depending if sentence is read-only or not
		if(readOnly){
			return 0.1;
		} else {
			return 0.5;
		}
	}

}