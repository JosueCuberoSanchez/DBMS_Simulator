package g8.dbms.simulator;
import javax.swing.JOptionPane;

import g8.dbms.simulator.event.Event;
import g8.dbms.simulator.event.Query;
import g8.dbms.simulator.module.*;
import g8.dbms.userinterface.RunningWindow;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;


/**
 * Class Simulation. It starts and manages the whole simulation. Connects every
 * module in the DBMS and makes the events pass through them.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam
 *
 */
public class Simulation extends Thread {
	
	private double clock; 						// The system clock.
	private PriorityQueue<Event> eventsQueue;	// The events priority queue ordered
												// by event time.
	private LinkedList<Statistics> stats;		// Contains the simulation statistics for each execution.
	private ClientAdministrator cam;			// DBMS' Client Administrator Module.
	private ProcessAdministrator pam;			// DBMS' Process Administrator Module.
	private QueryProcesor qpm;					// DBMS' Query Processor Module.
	private TransactionAccess tam;				// DBMS' Transaction Access Module.
	private QueryExecutor qem;					// DBMS' Query Executor Module.
	private int numberOfRuns;					// Total times to run the simulation.
	private RunningWindow rw;					// The window to refresh with the simulation data.
	private double delay;						// Number of second to stop the simulation to let the user
												// view the simulation progress in the running window.
	private double stopTime;					// Time in which the simulation will end.
	private Statistics finalStatistics;			// Contains the total statistic, weighted with the
												// total simulations executed.
	private int[] parameters;					// The modules parameters (total servers).
	
	/**
	 * Simulation class constructor
	 * 
	 * @param k Total servers in the Client Administrator Module.
	 * @param n Total servers in the Query Processor Module.
	 * @param p Total servers in the Transaction Access Module.
	 * @param m Total servers in the Query Executor Module.
	 * @param t Timeout for each connection.
	 */
	public Simulation(int k, int n, int p, int m, double t,int numberOfRuns,double stopTime) {
		this.qem = new QueryExecutor(m, this);
		this.tam = new TransactionAccess(p, qem , this);
		this.qpm = new QueryProcesor(n, tam, this);
		this.pam = new ProcessAdministrator(qpm, this);
		this.cam = new ClientAdministrator(k,pam, this, t);
		this.eventsQueue = new PriorityQueue<Event>();
		this.stats = new LinkedList<Statistics>();
		this.clock = 0.0;
		this.numberOfRuns = numberOfRuns;
		this.delay = 0;
		this.stopTime = stopTime;
		this.finalStatistics = new Statistics();
		this.parameters = new int[5];
			parameters[0] = k;
			parameters[1] = 1;
			parameters[2] = n;
			parameters[3] = p;
			parameters[4] = m;
	}
	
	/**
	 * Run the whole simulation in a cycle that manages the events.
	 */
	public void startSimulation(){
		cam.createEvent(clock);// Creates the first event and the timeout event.
		stats.add(new Statistics());
		String dataToSend;
		while((clock < stopTime)){
			Event aux = eventsQueue.poll();
			clock = aux.getTime();	// Updates the system clock to the current event time.
			switch(aux.getType()){
			case ARRIVAL: // New event (the query is not in the DBMS at this point).
				dataToSend = "Query ID " + aux.getQuery().getID() + " arriving to Client Administrator Module.";
				rw.update(dataToSend, aux); // Write the line in the window console output and updates the current event data.
				cam.processArrival(aux.getQuery(), clock);
				break;
			case EXIT_CLIENT_ADMINISTRATOR:	// An event leaving the Client Administrator Module.
				dataToSend = "Query ID " + aux.getQuery().getID() + " leaving the Client Administrator Module.";
				rw.update(dataToSend, aux); // Write the line in the window console output and updates the current event data..
				cam.processExit(aux.getQuery(),clock,true);	
				break;
			case EXIT_PROCESS_ADMINISTRATOR: // An event leaving the Process Administrator Module.
				dataToSend = "Query ID " + aux.getQuery().getID() + " leaving the Process Administrator Module.";
				rw.update(dataToSend, aux); // Write the line in the window console output and updates the current event data..
				pam.processExit(aux.getQuery(),clock,true);
				break;
			case EXIT_QUERY_PROCESSOR: // An event leaving the Query Processor Module.
				dataToSend = "Query ID " + aux.getQuery().getID() + " leaving the Query Processor Module.";
				rw.update(dataToSend, aux); // Write the line in the window console output and updates the current event data..
				qpm.processExit(aux.getQuery(),clock,true);
				break;
			case EXIT_TRANSACTION_ACCESS: // An event leaving the Transaction Access Module.
				dataToSend = "Query ID " + aux.getQuery().getID() + " leaving the Transaction Access Module.";
				rw.update(dataToSend, aux); // Write the line in the window console output and updates the current event data..
				tam.processExit(aux.getQuery(),clock,true);
				break;
			case EXIT_QUERY_EXECUTOR: // An event leaving the Query Executor Module.
				dataToSend = "Query ID " + aux.getQuery().getID() + " leaving the Query Executor.";
				rw.update(dataToSend, aux); // Write the line in the window console output and updates the current event data..
				stats.getLast().addQuery(aux.getQuery()); // The query is stored as finished to obtain the simulation statistics later.
				qem.processExit(aux.getQuery(),clock,true);
				
				dataToSend = "Query ID " + aux.getQuery().getID() + " has finished.";
				rw.update(dataToSend, aux); // Write the line in the window console output.
				rw.updateServed(); // Write the total served queries in the window data and updates the current event data..
				break;
			case TIMEOUT: // The timeout event arrived before it's query ends. This will kill the it's query.
				Query q =aux.getQuery();
				dataToSend = "Query ID " + aux.getQuery().getID() + " exceeded the limit timeout.";
				rw.update(dataToSend, aux); // Write the line in the window console output and updates the current event data..
				q.getCurrentModule().kill(q); // Kills the query that exceeded the timeout.
				break;
			default:
				// Do nothing.
				break;
			}
			
			// This will happen for each event processed:
			
			rw.updateBar(aux);	// Updates the current simulation progress bar in the window.
			rw.updateModules(cam, pam, qpm, tam, qem);	// Updates the modules data table in the window.
			try {
				Simulation.sleep((long) this.delay);
			} catch (InterruptedException e) {
				JOptionPane.showMessageDialog(null, "Failed to do a delay to slowly update the simulation data",
						"Delay error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		//This will happen for each simulation finished.
		
		rw.setBarFinished(); // Sets the current simulation progress bar in 100% and updates the general
							 // program process bar.
	}
	
	/**
	 * Adds a new event to the simulation's events queue.
	 * @param e The event to add in the queue.
	 */
	public void addEvent(Event e){
		this.eventsQueue.add(e);
	}
	
	/**
	 * Removes an event form the simulation's events queue.
	 * @param q The event to remove from the queue
	 */
	public void removeEvent(Query q){
		int t = q.getID();
		for( Event e : this.eventsQueue){
			if(e.getQuery().getID() == t){
				this.eventsQueue.remove(e);
				break;
			}
		}
	}
	
	/**
	 * Creates a new event to make a new query that will pass through the Modules and be processes if
	 * the timeout is not reached.
	 * The event is created with an exponential time distribution.
	 */
	public void Simulate(){
		for(int i=0;i<this.numberOfRuns;i++){
			rw.updateSimulationNumber("Starting simulation number " + (i+1) + ".");
			this.startSimulation();
			this.addDataToStatistics(i+1);
			this.reset();
			rw.updateModules(cam, pam, qpm, tam, qem);
		}
		rw.setFinishAvailable();
		this.setFinalStatistics();
	}
	
	/**
	 * Resets the modules data, the system clock and the events queue to run the simulation again
	 */
	public void reset(){
		this.cam.reset();
		this.pam.reset();
		this.qem.reset();
		this.qpm.reset();
		this.tam.reset();
		this.clock = 0.0;
		this.eventsQueue.clear();
	}
		
	/**
	 * Adds a finished query to the current statistics instance generated for the current simulation.
	 * @param query
	 */
	public void addQueryToStatistics(Query query){
		this.stats.getLast().addQuery(query);
	}
	
	/**
	 * Adds a non-finished and killed query to the current statistics instance generated for the
	 * current simulation.
	 * @param query
	 */
	public void addKilledQueryToStatistics(Query query){
		this.stats.getLast().addKilledQuery(query);
	}
	
	/**
	 * Adds the simulation data to the current statistics instance generated for an executed simulation.
	 * @param number The simulation number in the general execution.
	 */
	public void addDataToStatistics(int number){
		this.stats.getLast().setDiscarted(cam.getTotalDiscarded());
		this.stats.getLast().setLambdas(stopTime, cam.getTotalArrived(), pam.getTotalArrived(), qpm.getTotalArrived(), tam.getTotalArrived(), qem.getTotalArrived());
		this.stats.getLast().setAverageServiceTimeInModules();
		this.stats.getLast().setAverageServiceTimeInSystem();
		this.stats.getLast().setAverageQueueTimeInModules();
		this.stats.getLast().setAverageQueueTimeInSystem();
		this.stats.getLast().setAverageTimeInModules();
		this.stats.getLast().setAverageTimeInSystem();
		this.stats.getLast().setMus();
		this.sendParametersToStatistics();
		this.stats.getLast().setRhos();
		this.stats.getLast().setLs();
		this.stats.getLast().setLq();
		this.stats.getLast().setL();
		this.stats.getLast().setIdleTimes(this.stopTime);
		this.stats.getLast().setQueryTimesByType();
		this.stats.getLast().setAttemptedConnections(this.cam.getAttemptedConnections());
		String path = "./html/run-" + number + ".html";
		HTMLGenerator html = new HTMLGenerator(stats.getLast(), path, this.numberOfRuns, number);
		html.generateHTML();
	}
	
	/**
	 * Adds the total statistics, weighting the total with every simulation processed in the current execution,
	 * that are in the statistics list.
	 */
	public void setFinalStatistics(){
		Iterator<Statistics> it = this.stats.iterator();
		Statistics temp;
		while(it.hasNext()){
			temp = it.next();
			this.finalStatistics.setTotalLambda(temp);
			this.finalStatistics.setTotalAverageQueueTimeInModule(temp,0);
			this.finalStatistics.setTotalAverageQueueTimeInModule(temp,1);
			this.finalStatistics.setTotalAverageQueueTimeInModule(temp,2);
			this.finalStatistics.setTotalAverageQueueTimeInModule(temp,3);
			this.finalStatistics.setTotalAverageQueueTimeInModule(temp,4);
			this.finalStatistics.setTotalAverageQueueTimeInModule(temp,5);
			this.finalStatistics.setTotalL(temp);
			this.finalStatistics.setTotalLq(temp);
			this.finalStatistics.setTotalLs(temp);
			this.finalStatistics.setTotalMius(temp);
			this.finalStatistics.setTotalIdleTimes(temp);
			this.finalStatistics.setTotalQueryTimesByType(temp);
			this.finalStatistics.setTotalAverageTimeInSystem(temp);
			this.finalStatistics.setTotalAverageServiceTimeInSystem(temp);
			this.finalStatistics.setTotalDiscarded(temp);
			this.finalStatistics.setTotalAttemptedConnections(temp);
			this.finalStatistics.setTotalKilledQueries(temp);
		}
		
		this.finalStatistics.generateFinalAverage(this.stats.size());
		HTMLGenerator index = new HTMLGenerator(finalStatistics,this.numberOfRuns, cam.getCapacity(), qpm.getCapacity(), tam.getCapacity(), qem.getCapacity());
		index.generateHTML();
	}
	
	/**
	 * Sets a pointer to a Running Window to call it to update the progress and show it to the user
	 * through that window.
	 * @param window The referenced window.
	 */
	public void setRunningWindow(RunningWindow window){
		this.rw = window;
	}
	
	/**
	 * Gets a statistics instance in the statistics list.
	 * @param index The simulation's number to obtains it's statistics instance.
	 * @return The statistics instance associated to the indexed simulation.
	 */
	public Statistics getStatistics(int index){
		return this.stats.get(index);
	}
	
	/**
	 * Gets the weighted total statistics in the current execution.
	 * @return The statistics weighted in the execution.
	 */
	public Statistics getFinalStatistics(){
		return this.finalStatistics;
	}
	
	/**
	 * Gets the pointer to the Running Window associated to the simulation thread.
	 * @return The thread's Running Window.
	 */
	public RunningWindow getRunningWindow(){
		return this.rw;
	}
	
	/**
	 * Set's the delay time to stop the simulation thread in that amount of time periodically to
	 * let the user view the process in the Running Window.
	 * @param time The time to delay the thread.
	 */
	public void setDelay(Double time){
		this.delay = time;
	}
	
	/**
	 * Sends the modules parameters (number of servers) to the current statistics instance.
	 */
	public void sendParametersToStatistics(){
		this.stats.getLast().addParameters(this.parameters);
	}
	
	/**
	 * Run method to extends the Thread class. It starts the simulation progress in a new Thread.
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		this.Simulate();
	}
	
}
