package g8.dbms.simulator.event.enumerator;

/**
 * QueryType enum. Enumerates the possible queries that can be requested in the simulation.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 * 
 */
public enum QueryType {

	DDL(false),
	UPDATE(false),
	JOIN(true),
	SELECT(true);
	
	public boolean readOnly;	// Determines if the current query has a Read Only condition depending on it's type.
	
	/**
	 * Enum constructor. Sets the Read Only state of a query depending on its type.
	 * @param state Sets the Read Only state.
	 */
	private QueryType(boolean state){
		readOnly = state;
	}
}
