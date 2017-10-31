package g8.dbms.simulator;

import java.util.LinkedList;
import java.util.List;

import g8.dbms.simulator.event.Query;
import g8.dbms.simulator.event.enumerator.QueryType;

/**
 * Statistics class. Store each finished query to calculates the simulation
 * final statistics.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 * 
 */
public class Statistics {
	private int totalDiscarded;				// Total amount of connections that couldn't enter to the DBMS
    private List<Query> finishedQueries;	// Stores the queries that finished the process before the timeout.
	private List<Query> killedQueries;		// Stores the queries that don't finish the process because exceeded the timeout.
	private double[] idleTimes;				// The average servers idle time for each module and the system.
	private double[] Wq;					// The average queries waiting queue time for each module and the system.
	private double[] Ws;					// The average queries service time for each module and the system.
	private double[] W;						// The average queries permanence time for each module and the system.
	private double[] Lq;					// The average queries in queue for each module and the system.
	private double[] Ls;					// The average queries in service for each module and the system.
	private double[] L;						// The average queries that are in each module and the system.
	private double[] mu;					// The average service rate for each module and the system.
	private double[] lambda;				// The average arrival for each module and the system.
	private double[] rho;					// The average occupation time for each module and the system.
	private double[] queryTimeDLL;			// The average DDL queries permanence time for each module and the system.
	private double[] queryTimeSELECT;		// The average SELECT queries permanence time for each module and the system.
	private double[] queryTimeJOIN;			// The average JOIN queries permanence time for each module and the system.
	private double[] queryTimeUPDATE;		// The average UPDATE queries permanence time for each module and the system.
	private int totalDLL;					// The total DDL queries that finished the process before the timeout.
	private int totalSELECT;				// The total SELECT queries that finished the process before the timeout.
	private int totalJOIN;					// The total JOIN queries that finished the process before the timeout.
	private int totalUPDATE;				// The total UPDATE queries that finished the process before the timeout.
	private int[] parameters;				// The modules parameters (serves amount) in the execution.
	private int attemptedConnections;		// The addition of the connections that arrives to the system and the
											// connections discarded due to the lack of servers in the CAM module.
	private int totalKilledQueries;			// The total of queries that don't finish the process because exceeded
											// the timeout. This is necessary to obtain the addition of killed queries
											// in the weighted total simulation data by adds the killedQueries.size() for
											// each simulation executed.
	
	/**
	 * Class constructor. Initializes every variable and array. Note that some arrays have 6 positions.
	 * This is necessary to stores the average in each module (from 0 to 4) and in the general system (in 5).
	 */
	public Statistics() {
		this.finishedQueries = new LinkedList<Query>();
		this.killedQueries = new LinkedList<Query>();
		this.idleTimes = new double[6];
		this.Wq = new double[6];
		this.Ws = new double[6];
		this.W = new double[6];
		this.Lq = new double[6];
		this.Ls = new double[6];
		this.L = new double[6];
		this.mu = new double[6];
		this.lambda = new double[6];
		this.rho = new double[6];
		this.queryTimeDLL = new double[5];
		this.queryTimeSELECT = new double[5];
		this.queryTimeUPDATE = new double[5];
		this.queryTimeJOIN = new double[5];
		this.totalDLL = 0;
		this.totalJOIN = 0;
		this.totalSELECT = 0;
		this.totalUPDATE = 0;
		this.attemptedConnections = 0;
	}
	
	/**
	 * Gets the total amount of served connections.
	 * @return The finished (served) queries queue size.
	 */
	public int getServed(){
		return this.finishedQueries.size();
	}
	
	/**
	 * Gets the total amount of discarded connections.
	 * @return The total of discarded connections.
	 */
	public int getDiscarted () {
		return this.totalDiscarded;
	}
	
	/**
	 * Sets the total amount of discarded connections.
	 */
	public void setDiscarted(int d) {
		this.totalDiscarded = d;
	}
	
	//starting lambda methods and functions
	
	/**
	 * Sets the lambdas for all modules and system.
	 * @param stopTime the stop time for simulation.
	 * @param camArrived the queries arrived to client administrator module.
	 * @param pamArrived the queries arrived to process administrator module.
	 * @param qemArrived the queries arrived to query executor module.
	 * @param qpmArrived the queries arrived to query processor module.
	 * @param tamArrived the queries arrived to transaction access module.
	 */
	public void setLambdas(double stopTime,int camArrived, int pamArrived, int qmpArrived, int tamArrived, int qemArrived){
		this.lambda[0] = camArrived/stopTime;
		this.lambda[1] = pamArrived/stopTime;
		this.lambda[2] = qmpArrived/stopTime;
		this.lambda[3] = tamArrived/stopTime;
		this.lambda[4] = qemArrived/stopTime;
		this.lambda[5] = camArrived/stopTime;
	}
	
	/**
	 * Gets the lambda from a module or the system.
	 * @param module The module (or the system if module == 5) to obtain its lambda.
	 * @return The lambda from a module or the system.
	 */
	public double getLambda(int module){
		int m;
		if(module == 5){
			m = 0;
		} else {
			m = module;
		}
		return this.lambda[m];
	}
	
	//ending lambda methods and functions
	
	//starting W methods and functions
	
	/**
	 * Sets the average life time of a query in all modules.
	 */
	public void setAverageTimeInModules(){ 
		int totalQueries = this.finishedQueries.size();
		double totalLifeTime;
		for(int i=0;i<5;i++){
			if(totalQueries == 0){
				this.W[i] = 0;
			}else{
				totalLifeTime = 0.0;
				for(Query q : this.finishedQueries){
					totalLifeTime += (q.getExitTimes(i)-q.getArrivalTimes(i));
				}
				this.W[i] = (totalLifeTime/totalQueries);
			}
		}
	}
	
	/**
	 * Sets the average time in the system.
	 */
	public void setAverageTimeInSystem(){ //W in system
		this.W[5] = ((this.W[0]+this.W[1]+this.W[2]+this.W[3]+this.W[4]));
	}
	
	/**
	 * Gets the average time in a module or system.
	 * @param module The module (or the system if module == 5) to obtain its average time.
	 * @return average time in a module or system.
	 */
	public double getAverageTime(int module){
		return this.W[module];
	}
	
	//ending W methods and functions
	
	//starting Ws methods and functions
	
	/**
	 * Sets the average service time of a query in the modules.
	 */
	public void setAverageServiceTimeInModules(){ 
		int totalQueries = this.finishedQueries.size();
		for(int i=0;i<5;i++){
			if(totalQueries == 0){
				Ws[i] = 0;
			}else{
				double totalLifeTime = 0.0;
				for(Query q : this.finishedQueries){
					totalLifeTime += (q.getExitTimes(i)-q.getQueueExitTime(i));
				}
				this.Ws[i] = (totalLifeTime/totalQueries);
			}
		}
	}
	
	/**
	 * Sets the average service time in the system.
	 */
	public void setAverageServiceTimeInSystem(){ //Ws in system
		this.Ws[5] = ((this.Ws[0]+this.Ws[1]+this.Ws[2]+this.Ws[3]+this.Ws[4]));
	}
	
	/**
	 * Gets the average service time in a module.
	 * @param module The module (or the system if module == 5) to obtain its average service time.
	 * @return average service time in a module.
	 */
	public double getAverageServiceTime(int module){
		return this.Ws[module];
	}
	
	//ending Ws methods and functions
	
	//starting Wq methods and functions
	
	/**
	 * Sets the average queue time of a query in the modules.
	 */
	public void setAverageQueueTimeInModules(){ 
		int totalQueries = this.finishedQueries.size();
		for(int i=0;i<5;i++){
			if(totalQueries == 0){
				Wq[i] = 0;
			}else{
				double totalLifeTime = 0.0;
				for(Query q : this.finishedQueries){
					totalLifeTime += (q.getQueueExitTime(i)-q.getArrivalTimes(i));
				}
				this.Wq[i] = (totalLifeTime/totalQueries);
			}
		}
	}
	
	/**
	 * Sets the average queue time in the system.
	 */
	public void setAverageQueueTimeInSystem(){ //Wq in system
		this.Wq[5] = ((this.Wq[0]+this.Wq[1]+this.Wq[2]+this.Wq[3]+this.Wq[4]));
	}
	
	/**
	 * Gets the average queue time in a module.
	 * @param module The module (or the system if module == 5) to obtain its average queue time.
	 * @return average queue time in a module.
	 */
	public double getAverageQueueTime(int module){
		return this.Wq[module];
	}
	
	//ending Wq methods and functions
	
	//starting mu methods and functions
	
	/**
 	 * Sets the mu's for all modules and system.
 	 */
	public void setMus(){
		this.mu[0] = (1/this.Ws[0]);
		this.mu[1] = (1/this.Ws[1]);
		this.mu[2] = (1/this.Ws[2]);
		this.mu[3] = (1/this.Ws[3]);
		this.mu[4] = (1/this.Ws[4]);
		this.mu[5] = (1/this.Ws[5]);
	}
	
	/**
	 * Gets the mu from a module.
	 * @param module The module (or the system if module == 5) to obtain its mu.
	 * @return The mu from a module.
	 */
	public double getMu(int module){
		return this.mu[module];
	}
	
	//ending mu methods and functions
	
	//starting rho methods and functions
	
	/**
	 * Sets the rhos for all modules and system.
	 */
	public void setRhos(){
		for(int i=0;i<5;i++){
			this.rho[i] = this.lambda[i]/(this.mu[i]*this.parameters[i]);
		}
		this.rho[5] = this.lambda[0]/this.mu[5];
	}
	
	/**
	 * Gets the rho from a module.
	 * @param module The module (or the system if module == 5) to obtain its rho.
	 * @return The rho from a module.
	 */
	public double getRho(int module){
		return this.rho[module];
	}
	
	//ending rho methods and functions
	
	//starting Ls methods and functions
	
	/**
	 * Sets the Ls for all modules and system.
	 */
	public void setLs(){
		for(int i=0;i<=5;i++){
			this.Ls[i] = this.lambda[i]*this.Ws[i];
		}
	}
	
	/**
	 * Gets the Ls from a module or system.
	 * @param module The module (or the system if module == 5) to obtain its Ls.
	 * @return The Ls from a module or system.
	 */
	public double getLs(int module){
		return this.Ls[module];
	}
	
	//ending Ls methods and functions
	
	//starting Lq methods and functions
	
	/**
	 * Sets the Lq for all modules and system.
	 */
	public void setLq(){
		for(int i=0;i<=5;i++){
			this.Lq[i] = this.lambda[i]*this.Wq[i];
		}
	}
	
	/**
	 * Gets the Lq from client administrator module.
	 * @param module The module (or the system if module == 5) to obtain its Lq.
	 * @return The Lq from client administrator module.
	 */
	public double getLq(int module){
		return this.Lq[module];
	}
	
	//ending Lq methods and functions
	
	//starting L methods and functions
	
	/**
	 * Sets the L for all modules and system.
	 */
	public void setL(){
		for(int i=0;i<=5;i++){
			this.L[i] = this.lambda[i]*this.W[i];
		}
	}
	
	/**
	 * Gets the L from client administrator module.
	 * @param module The module (or the system if module == 5) to obtain its L.
	 * @return The L from client administrator module.
	 */
	public double getL(int module){
		return this.L[module];
	}
	
	//ending L methods and functions
	
	/**
	 * Gets the total amount of connections attempted in the DBMS.
	 * @return The sum of total served and total discarded connections.
	 */
	public int getConnections(){
		return (this.finishedQueries.size() + this.totalDiscarded);
	}
	
	/**
	 * Adds a new query to the finished queries list.
	 * @param query The query to be added to the list.
	 */
	public void addQuery(Query query){
		this.finishedQueries.add(query);
	}
	
	/**
	 * Adds a new killed query to the killed queries list.
	 * @param query The query to be added to the list.
	 */
	public void addKilledQuery(Query query){
		this.killedQueries.add(query);
	}
	
	/**
	 * Sets the idle times vector.
	 * @param stopTime, the time to stop simulation given by the user.
	 */
	public void setIdleTimes(double stopTime){
		for(int i=0;i<=5;i++){
			if(this.rho[i] >= 1){
				this.idleTimes[i] = 0.0;
			} else {
				this.idleTimes[i] = (stopTime*((1-this.rho[i])*100))/100;
			}
		}
	}
	
	/**
	 * Gets the idle time from client administrator module.
	 * @param module The module (or the system if module == 5) to obtain its idleTime.
	 * @return the idle time from client administrator module.
	 */
	public double getIdleTime(int module){
		return this.idleTimes[module];
	}
	
	/**
	 * Asks for stability of the given module.
	 * @param module The module (or the system if module == 5) to obtain its stability.
	 * @return true if stable, false if not.
	 */
	public boolean isModuleStable(int module){
		if(this.rho[module]<1){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Sets the average queries permanence time by query type for each module and the system.
	 */
	public void setQueryTimesByType(){
		for(Query q : this.finishedQueries){
			switch(q.getType()){
			case DDL:
				this.totalDLL++;
				break;
			case SELECT:
				this.totalSELECT++;
				break;
			case JOIN:
				this.totalJOIN++;
				break;
			case UPDATE:
				this.totalUPDATE++;
				break;
			}
			for(int i=0;i<5;i++){
				switch(q.getType()){
					case DDL:
						this.queryTimeDLL[i] += (q.getExitTimes(i)-q.getArrivalTimes(i));
						break;
					case SELECT:
						this.queryTimeSELECT[i] += (q.getExitTimes(i)-q.getArrivalTimes(i));
						break;
					case JOIN:
						this.queryTimeJOIN[i] += (q.getExitTimes(i)-q.getArrivalTimes(i));
						break;
					case UPDATE:
						this.queryTimeUPDATE[i] += (q.getExitTimes(i)-q.getArrivalTimes(i));
						break;
				}
			}
		}
		
		for(int i=0;i<5;i++){
			if(this.totalDLL == 0){
				this.queryTimeDLL[i] =0;
			}else{
				this.queryTimeDLL[i] /= (this.totalDLL*1.0);
			}
			
			if(this.totalSELECT == 0){
				this.queryTimeSELECT[i] = 0;
			}else{
				this.queryTimeSELECT[i] /= (this.totalSELECT*1.0);
			}
			
			if(this.totalJOIN == 0){
				this.queryTimeJOIN[i] = 0;
			}else{
				this.queryTimeJOIN[i] /= (this.totalJOIN*1.0);
			}
			
			if(this.totalUPDATE == 0){
				this.queryTimeUPDATE[i] = 0;
			}else{
				this.queryTimeUPDATE[i] /= (this.totalUPDATE*1.0);
			}
		}
	}
	
	/**
	 * Gets the average queries permanence time by type from each module and the system.
	 * @param qt The query type to obtains it's times.
	 * @param module The module to obtains the permanence times.
	 * @return The average query permanence time by type.
	 */
	public double getQueryTimeByTypeInModule(QueryType qt,int module){
		switch(qt){
			case DDL:
				return this.queryTimeDLL[module];
			case UPDATE:
				return this.queryTimeUPDATE[module];
			case JOIN:
				return this.queryTimeJOIN[module];	
			case SELECT:
				return this.queryTimeSELECT[module];
			default:
				return 0.0;
		}
	}
	
	/**
	 * Sets the total connections that tried to enter to the system.
	 * @param connections The total attempted connections.
	 */
	public void setAttemptedConnections(int connections){
		this.attemptedConnections = connections;
	}
	
	/**
	 * Gets the total connections that tried to enter to the system.
	 * @return The total attempted connections.
	 */
	public int getAttemptedConnections(){
		return this.attemptedConnections;
	}
	
	/**
	 * Updates the addition of lambdas for all modules and system to calculates the weighted average later.
	 * @param stats The current Statistics instance to add it's data to the total.
	 */
	public void setTotalLambda(Statistics stats){
		for(int i = 0; i < lambda.length; i++){
			this.lambda[i] += stats.lambda[i];
		}
	}
	
	/**
	 * Updates the addition of the average queue time of a query in the modules and the system to calculates the
	 * weighted average later.
	 * @param stats The current Statistics instance to add it's data to the total.
	 * @param module The module from which obtains the average (5 to obtains the general system average).
	 */
	public void setTotalAverageQueueTimeInModule(Statistics stats,int module){
		this.Wq[module] += stats.Wq[module];
	}

	/**
	 * Updates the addition of the average system time of a query in the modules and the system to calculates the
	 * weighted average later.
	 * @param stats The current Statistics instance to add it's data to the total.
	 */
	public void setTotalAverageTimeInSystem(Statistics stats){
		for(int i = 0; i < W.length; i++){
			this.W[i] += stats.W[i];
		}
	}
	
	/**
	 * Updates the addition of the average system service of a query in the modules and the system to calculates the
	 * weighted average later.
	 * @param stats The current Statistics instance to add it's data to the total.
	 */
	public void setTotalAverageServiceTimeInSystem(Statistics stats){
		for(int i = 0; i < W.length; i++){
			this.Ws[i] += stats.Ws[i];
		}
	}
	
	/**
	 * Updates the addition of the L for all modules and system to calculates the weighted average later.
	 * @param stats The current Statistics instance to add it's data to the total.
	 */
	public void setTotalL(Statistics stats){
		for(int i = 0; i < L.length; i++){
			this.L[i] += stats.L[i];
		}
	}
	
	/**
	 * Updates the addition of the Lq for all modules and system to calculates the weighted average later.
	 * @param stats The current Statistics instance to add it's data to the total.
	 */
	public void setTotalLq(Statistics stats){
		for(int i = 0; i < L.length; i++){
			this.Lq[i] += stats.Lq[i];
		}
	}
	
	/**
	 * Updates the addition of the Ls for all modules and system to calculates the weighted average later.
	 * @param stats The current Statistics instance to add it's data to the total.
	 */
	public void setTotalLs(Statistics stats){
		for(int i = 0; i < L.length; i++){
			this.Ls[i] += stats.Ls[i];
		}
	}
	
	/**
	 * Updates the addition of the idle times for all modules and system to calculates the weighted average later.
	 * @param stats The current Statistics instance to add it's data to the total.
	 */
	public void setTotalIdleTimes(Statistics stats){
		for(int i = 0; i < this.idleTimes.length; i++){
			this.idleTimes[i] += stats.idleTimes[i];
		}
	}
	
	/**
	 * Updates the addition of the mius for all modules and system to calculates the weighted average later.
	 * @param stats The current Statistics instance to add it's data to the total.
	 */
	public void setTotalMius(Statistics stats){
		for(int i = 0; i < this.mu.length; i++){
			this.mu[i] += stats.mu[i];
		}
	}
	
	/**
	 * Updates the addition of the total discarded connections due to the lack of servers in the
	 * Client Administrator Module.
	 * @param stats The current Statistics instance to add it's data to the total.
	 */
	public void setTotalDiscarded(Statistics stats){
		this.totalDiscarded += stats.totalDiscarded;
	}
	
	/**
	 * Updates the addition of the average queries permanence time by query type for each module and the system
	 * to calculates the weighted average later.
	 * @param stats The current Statistics instance to add it's data to the total.
	 */
	public void setTotalQueryTimesByType(Statistics stats){
		for(int i = 0; i < 5; i++){
			this.queryTimeDLL[i] += stats.queryTimeDLL[i];
			this.queryTimeJOIN[i] += stats.queryTimeJOIN[i];
			this.queryTimeSELECT[i] += stats.queryTimeSELECT[i];
			this.queryTimeUPDATE[i] += stats.queryTimeUPDATE[i];
		}
	}
	
	/**
	 * Updates the addition of the attempted connections to the system.
	 * @param stats The current Statistics instance to add to the total.
	 */
	public void setTotalAttemptedConnections(Statistics stats){
		this.attemptedConnections += stats.attemptedConnections;
	}
	
	/**
	 * Updates the addition of the killed queries in the system.
	 * @param stats The current Statistics instance to add to the total.
	 */
	public void setTotalKilledQueries(Statistics stats){
		this.totalKilledQueries += stats.killedQueries.size();
	}
	
	/**
	 * Gets the addition of the killed queries in the system.
	 * @return The total killed queries.
	 */
	public int getTotalKilledQueries(){
		return this.totalKilledQueries;
	}
	
	/**
	 * Gets the total killed queries in the system in an individual simulation
	 * @return The total killed queries.
	 */
	public int getKilledQueries(){
		return this.killedQueries.size();
	}
	
	/**
	 * Weights the addition of the executed simulation's statistics data by the total
	 * simulations processed.
	 * @param amount The total simulations processed in the program execution.
	 */
	public void generateFinalAverage(int amount){
		for(int i = 0; i < 6; i++){
			this.lambda[i] /= amount;
			this.W[i] /= amount;
			this.Ws[i] /= amount;
			this.Wq[i] /= amount;
			this.L[i] /= amount;
			this.Ls[i] /= amount;
			this.Lq[i] /= amount;
			this.mu[i] /= amount;
			this.idleTimes[i] /= amount;
		}
		
		for(int i = 0; i < 4; i++){
			this.queryTimeDLL[i] /= amount;
			this.queryTimeJOIN[i] /= amount;
			this.queryTimeSELECT[i] /= amount;
			this.queryTimeUPDATE[i] /= amount;
		}
	}
	
	/**
	 * Sets the modules parameters.
	 * @param param The modules parameters.
	 */
	public void addParameters(int[] param){
		this.parameters = param;
	}
}
