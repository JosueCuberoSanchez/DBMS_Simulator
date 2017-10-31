package g8.dbms.simulator.event;

import g8.dbms.simulator.event.enumerator.QueryType;
import g8.dbms.simulator.module.Module;

/**
 * ProcessAdministrator class. Represents the DBMS' Process Administrator module
 * and processes queries as the second DBMS module.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 * 
 */
public class Query implements Comparable {
	
	double[] arrivalTime;	// The query's arrival time in each module.
	double[] exitTime;		// The query's exit time in each module.
	double[] queueExitTime; // The query's exit time from queue.
	Module currentModule;	// The current module where the query is located.
	QueryType queryType;	// The query's type.
	boolean isKilled;		// Defines if the query was taken out from the DBMS due to it's passed the timeout.
	int blocks;				// Total of disk's blocks needed to being loaded to complete the query execution.
	int ID;					// Query ID.

	/**
	 * Class constructor. It creates the query whose type depends on a Monte Carlo discrete distribution.
	 * @param random A random number to choice the query's type with the distribution.
	 * @param id An unique identifier to give to the query.
	 */
	public Query(double random, int id) {
		this.arrivalTime = new double[5];
		this.exitTime = new double[5];
		this.queueExitTime = new double[5];
		if(random < 0.07){
			this.queryType = QueryType.DDL;
		} else if(random < 0.35){
			this.queryType = QueryType.UPDATE;
		} else if(random < 0.67){
			this.queryType = QueryType.SELECT;
		} else if(random <= 1){
			this.queryType = QueryType.JOIN;
		}
		this.ID = id;
	}
	
	/**
	 * Sets the query as killed due to a time excess.
	 */
	public void kill(){
		this.isKilled= true;
	}
	
	/**
	 * Sets the query's arrival time to the current module.
	 * @param time The clock time when the query arrives.
	 */
	public void setArrivalTimes(double time){
		this.arrivalTime[this.currentModule.getModuleName().ordinal()] = time;
	}
	
	/**
	 * Sets the query's exit time from the current module.
	 * @param time The clock time when the query leaves.
	 */
	public void setExitTimes(double time){
		this.exitTime[this.currentModule.getModuleName().ordinal()] = time;
	}
	
	/**
	 * Sets the query's exit time from the queue of the current module.
	 * @param time The clock time when the query leaves the queue.
	 */
	public void setQueueExitTime(double time){
		this.queueExitTime[this.currentModule.getModuleName().ordinal()] = time;
	}
	
	/**
	 * Sets the module where the query currently stays.
	 * @param mod The current module's name.
	 */
	public void setCurrentModule(Module mod){
		this.currentModule = mod;
	}
	
	/**
	 * Gets the query's killed status.
	 * @return true if it's killed, false if not.
	 */
	public boolean isKilled(){
		return this.isKilled;
	}
	
    /**
     * Gets the query's arrival time to a module.
     * @param module The module to obtains the query's time.
     * @return The query's arrival time.
     */
    public double getArrivalTimes(int module){
		return this.arrivalTime[module];
	}
	
	/**
	 * Gets the query's exit time from a module.
     * @param module The module to obtains the query's time.
     * @return The query's exit time.
	 */
	public double getExitTimes(int module){
		return this.exitTime[module];
	}
	
	/**
	 * Gets the query's exit queue time in a module.
     * @param module The module to obtains the query's time.
     * @return The query's exit queue arrival time.
	 */
	public double getQueueExitTime(int module){
		return this.queueExitTime[module];
	}
	
	/**
	 * Gets the module's name where the query is currently located.
	 * @return The module's name.
	 */
	public Module getCurrentModule(){
		return this.currentModule;
	}
	
	/** 
	 * Compares the queries by it's type.
	 * @param o The query to compare the current query.
	 * @return The comparison's result.
	 */
	public int compareTo(Object o) {
		if(this.queryType.ordinal() == ((Query) o).getType().ordinal()){
			return 0;
		}else{
			return -1;
		}
	}
	
	/**
	 * Gets the query's read only state.
	 * @return true if it's read only, false if not.
	 */
	public boolean isOnlyRead(){
		return queryType.readOnly;
	}
	
	/**
	 * Get the query type.
	 * @return The query type.
	 */
	public QueryType getType(){
		return this.queryType;
	}
	
	/**
	 * Sets the total amount of blocks that the query needs to be processed.
	 * @param b The total of blocks needed.
	 */
	public void setTotalBlocks(int b){
		this.blocks = b;
	}
	
	/**
	 * Gets the total amount of blocks that the query needs to be processed.
	 * @return The total of blocks needed.
	 */
	public int getTotalBlocks(){
		return this.blocks;
	}
	
	/**
	 * Gets the query unique identifier.
	 * @return The query's ID
	 */
	public int getID(){
		return ID;
	}
}