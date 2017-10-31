package g8.dbms.simulator.event.enumerator;

/**
 * EventType enum. Enumerates the possible events that can happen in the simulation.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 * 
 */
public enum EventType {
	ARRIVAL,
	EXIT_CLIENT_ADMINISTRATOR,
	EXIT_PROCESS_ADMINISTRATOR,
	EXIT_QUERY_PROCESSOR,
	EXIT_TRANSACTION_ACCESS,
	FILL_TRANSACTION_SERVERS,
	EXIT_QUERY_EXECUTOR,
	TIMEOUT;
}
