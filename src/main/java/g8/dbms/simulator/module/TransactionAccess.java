package g8.dbms.simulator.module;


import java.util.PriorityQueue;

import g8.dbms.simulator.Simulation;
import g8.dbms.simulator.event.Event;
import g8.dbms.simulator.event.Query;
import g8.dbms.simulator.event.enumerator.EventType;
import g8.dbms.simulator.event.enumerator.ModuleName;
import g8.dbms.simulator.event.enumerator.QueryType;

/**
 * TransactionAccess class. Represents the DBMS' Transaction Access module
 * and processes queries as the fourth module.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 * 
 */
public class TransactionAccess extends Module{
	
	private boolean lock;	// Lock the servers to work with a DDL query and blocks the pass to any other query
							// while the DDL is in process.
	
	/**
	 * Class constructor
	 * @param p Total servers in the module.
	 * @param next A pointer to the next module.
	 * @param s A pointer to the program's simulation.
	 */
	public TransactionAccess(int p, Module next, Simulation s) {
		this.capacity = p;
		this.queryQueue = new PriorityQueue<Query>();
		this.totalBusy = 0;
		this.lock = false;
		this.nextModule = next;
		this.simulation = s;
		this.mn = ModuleName.TRANSACTIONACCES;
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
		if(query.getType().equals(QueryType.DDL)){ // If the query is a DDL, the module only can work with it.
			if(this.totalBusy == 0){ // If every server is idle, the module is locked to work only with one server
									 // to process only the DDL query.
				lock = true;
				this.totalBusy++;
				query.setQueueExitTime(clock);
				query.setExitTimes(this.generateExitTime(query) + clock); //generates service time.
				Event newEvent = new Event(query.getExitTimes(3), EventType.EXIT_TRANSACTION_ACCESS, query); //creates new exit event.
				this.simulation.addEvent(newEvent); //return it.
			} else { //if servers are busy.
				this.queryQueue.add(query); //add query to queue.
			}
		} else { // If the current query is not a DDL.
			
			// If the servers are locked, or the first query in the queue is an upcoming DDL and the servers are unlocked,
			// the new query should wait until the servers are unlocked again or until the DDL process finishes respectively.
			if((lock) || (!this.queryQueue.isEmpty() && this.queryQueue.peek().getType().equals(QueryType.DDL))
					|| (this.totalBusy == this.capacity)){
				this.queryQueue.add(query); //add query to queue.
			} else { // If a DDL is not in the queue's peek and the servers are not locked.
				this.totalBusy++;
				query.setQueueExitTime(clock);
				query.setExitTimes(this.generateExitTime(query) + clock); //generates service time.
				Event newEvent = new Event(query.getExitTimes(3), EventType.EXIT_TRANSACTION_ACCESS, query); //creates a new exit event.
				this.simulation.addEvent(newEvent); //returns it.
			}
		}
	}
	
	/**
	 * Process a query's exit from the module.
	 * @param clock The current time in the simulation.
	 */
	public void processExit(Query query, double clock,boolean b){
		if(query.getType() == QueryType.DDL){
			this.lock = false;
		}
		if(this.queryQueue.isEmpty()){ // If there are no queries waiting in the queue.
			this.totalBusy--;
		} else if(this.capacity == 1){
			Query temporal = this.queryQueue.poll();
			temporal.setQueueExitTime(clock);
			temporal.setExitTimes(this.generateExitTime(temporal) + clock); //generates service time.
			Event newEvent = new Event(temporal.getExitTimes(3), EventType.EXIT_TRANSACTION_ACCESS, temporal); //creates a new exit event.
			this.simulation.addEvent(newEvent);
		} else if (this.totalBusy == 1){ // If there are only one query in the queue
			
			if(this.queryQueue.peek().getType().equals(QueryType.DDL)){ // If the waiting query is a DDL.
				Query temporal = this.queryQueue.poll();
				temporal.setQueueExitTime(clock);
				temporal.setExitTimes(this.generateExitTime(temporal) + clock); //generates service time.
				Event newEvent = new Event(temporal.getExitTimes(3), EventType.EXIT_TRANSACTION_ACCESS, temporal); //creates a new exit event.
				this.simulation.addEvent(newEvent); //returns it.
				this.lock = true;
			} else {
				this.totalBusy--;
				this.fillEventList(clock);
			}
		} else{ // If the queue has more than one query waiting.
			
			if(!this.queryQueue.peek().getType().equals(QueryType.DDL)){ // If the first waiting query is a DDL, it's exit
																		 // is generated.
				Query temporal = this.queryQueue.poll();
				temporal.setQueueExitTime(clock);
				temporal.setExitTimes(this.generateExitTime(temporal) + clock); //generates service time.
				Event newEvent = new Event(temporal.getExitTimes(3), EventType.EXIT_TRANSACTION_ACCESS, temporal); //creates a new exit event.
				this.simulation.addEvent(newEvent);
			} else { // If the first waiting query is not a DDL.
				this.totalBusy--;
			}
			
		}
		if(b){
			this.nextModule.processArrival(query, clock);
		}
	}
	
	/**
	 * Generates exit times to every query in service in order to liberate the servers to let the module
	 * work with only an upcoming DDL query.
	 * @param list Will contains the exit events created.
	 * @param clock The current time in the simulation.
	 */
	public void fillEventList(double clock){
		for(int i = 0; (i < this.capacity) && (!this.queryQueue.isEmpty()); i++){
			Query temporal = this.queryQueue.poll();
			temporal.setExitTimes(this.generateExitTime(temporal)+ clock);
			temporal.setQueueExitTime(clock);
			Event newEvent = new Event(temporal.getExitTimes(3), EventType.EXIT_TRANSACTION_ACCESS, temporal);
			simulation.addEvent(newEvent);
			this.totalBusy++;
		}
	}
	
	/**
	 * Generate an event's exit time based on the amount of time that the total blocks from disk needs to
	 * being loaded. The total blocks loaded depends on the query type.
	 * @param Query The event's query to generate it's exit time. Is necessary to update it's total disk blocks used.
	 * @return The new event's service time.
	 */
	public double generateExitTime(Query query){
		double time = this.capacity * 0.03;
		int blocks = 0;
		switch(query.getType()){
		case DDL:
			break;
		case UPDATE:
			break;
		case JOIN:
			blocks += random.nextInt(16)+1;
			blocks += random.nextInt(12)+1;
			break;
		case SELECT:
			blocks += random.nextInt(64) + 1;
			break;
		}
		query.setTotalBlocks(blocks);
		return time += (blocks/10);
	}
	
	/**
	 * Resets the module data.
	 */
	public void reset(){
		this.totalBusy = 0;			
		this.totalArrived = 0;				
		this.isBusy = false;				
		this.queryQueue.clear();	
		this.lock = false;
	}
}
