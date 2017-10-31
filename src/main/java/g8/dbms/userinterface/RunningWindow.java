package g8.dbms.userinterface;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Cursor;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import g8.dbms.simulator.Simulation;
import g8.dbms.simulator.event.Event;
import g8.dbms.simulator.module.ClientAdministrator;
import g8.dbms.simulator.module.ProcessAdministrator;
import g8.dbms.simulator.module.QueryExecutor;
import g8.dbms.simulator.module.QueryProcesor;
import g8.dbms.simulator.module.TransactionAccess;

import javax.swing.border.BevelBorder;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.ScrollPaneConstants;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Running Window GUI. Lets the user to view the simulations and program progress.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 */
public class RunningWindow extends JFrame{

	// GUI elements needed from methods outside the JFrame constructor.
	
	private JPanel contentPane;
	private JTable eventTable;
	private JTable modulesTable;
	private JTextArea txtConsole;
	private JButton btnFinish;
	private JProgressBar generalProgressBar;
	private JProgressBar currentSimulationProgressBar;
	private JLabel lblCurrentClock;
	private JLabel lblProcessedNumber;
	private JButton btnStop;
	private JLabel lblNumberSeconds;
	private JLabel lblNumber;
	private JLabel lblNumberServed;
	private JLabel lblNumberTimes;
	private JLabel lblNumberDiscarded;

	// End of the GUI elements declaration.
	
	private Simulation sim;			// A pointer to the program simulation.
	private CurrentStatistics cs;	// A pointer to the descendant Current Statistics window.
	
	/**
	 * Class constructor. It creates the frame.
	 * @param s The program simulation.
	 * @param cw The ancestor Configuration Window.
	 */
	public RunningWindow(Simulation s, final ConfigurationWindow cw) {
		
		// Creates a Current Statistics window:
		
		cs = new CurrentStatistics(this);
		cs.setLocationRelativeTo(null);
		setResizable(false);
		
		// Sets the program's simulation pointer:
		
		sim = s;
		sim.setRunningWindow(this);
		
		// Main JFrame properties:
		
		setTitle("Running simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 802, 731);
		contentPane = new JPanel();
		contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// The JButton Stop:
		
		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			
			/**
			 * If the button is pressed, the program asks to the user if really want to stop the process.
			 * If No is selected, the program will continue normally.
			 * If Yes is selected, the program will be stoped.
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to stop the simulation?", "Aborted operation",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(option == JOptionPane.YES_OPTION){
					dispose();
					System.exit(0);	// Kill the program.
				}
			}
		});
		btnStop.setBounds(598, 669, 89, 23);
		btnStop.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		contentPane.add(btnStop);
		
		// The Simulation parameters JPanel:
		
		JPanel panelSimulationParameters = new JPanel();
		panelSimulationParameters.setBounds(10, 11, 383, 125);
		panelSimulationParameters.setLayout(null);
		panelSimulationParameters.setBorder(new TitledBorder(null, "Simulation parameters", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelSimulationParameters);
		
		// JLabel Times:
		
		JLabel lblTimes = new JLabel("Number of times to run the simulation:");
		lblTimes.setBounds(10, 21, 200, 14);
		panelSimulationParameters.add(lblTimes);
		
		// JLabel Seconds:
		
		JLabel lblSeconds = new JLabel("Total seconds to run each simulation:");
		lblSeconds.setBounds(10, 46, 220, 14);
		panelSimulationParameters.add(lblSeconds);
		
		// JLabel Timeout:
		
		JLabel lblTimeout = new JLabel("Seconds to terminates a connection (timeout):");
		lblTimeout.setBounds(10, 71, 241, 14);
		panelSimulationParameters.add(lblTimeout);
		
		// JLabel Number Timeout, shows the configured timeout in the Configuration Window:
		
		JLabel lblNumberTimeout = new JLabel(cw.getTimeout());
		lblNumberTimeout.setBounds(240, 71, 46, 14);
		panelSimulationParameters.add(lblNumberTimeout);
		
		// JLabel Number Seconds, shows the configured seconds in the Configuration Window:
		
		lblNumberSeconds = new JLabel(cw.getSeconds());
		lblNumberSeconds.setBounds(240, 46, 46, 14);
		panelSimulationParameters.add(lblNumberSeconds);
		
		// JLabel Number Times, shows the configured times in the Configuration Window:
		
		lblNumberTimes = new JLabel(cw.getTimes());
		lblNumberTimes.setBounds(240, 21, 46, 14);
		panelSimulationParameters.add(lblNumberTimes);
		this.cs.fillCombobox(Integer.parseInt(cw.getTimes()));
		
		// JLabel Delay:
		
		JLabel lblDelay = new JLabel("Delay seconds:");
		lblDelay.setBounds(10, 96, 86, 14);
		panelSimulationParameters.add(lblDelay);
		
		// JLabel Delay Time, shows the configured delay in the Configuration Window:
		
		JLabel lblDelayTime = new JLabel(cw.getDelaySeconds());
		lblDelayTime.setBounds(240, 96, 46, 14);
		panelSimulationParameters.add(lblDelayTime);
		
		// The Modules parameters JPanel:
		
		JPanel panelModulesParameters = new JPanel();
		panelModulesParameters.setBounds(403, 11, 383, 125);
		panelModulesParameters.setLayout(null);
		panelModulesParameters.setBorder(new TitledBorder(null, "Modules parameters", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelModulesParameters);
		
		// JLabel Connections:
		
		JLabel lblConnections = new JLabel("Number of posible connections:");
		lblConnections.setBounds(10, 21, 222, 14);
		panelModulesParameters.add(lblConnections);
		
		// JLabel Query Processes:
		
		JLabel lblQueryProcesses = new JLabel("Number of concurrent query processes:");
		lblQueryProcesses.setBounds(10, 46, 211, 14);
		panelModulesParameters.add(lblQueryProcesses);
		
		// JLabel Transaction Processes:
		
		JLabel lblTransactionProcesses = new JLabel("Number of processes to execute transactions:");
		lblTransactionProcesses.setBounds(10, 71, 222, 14);
		panelModulesParameters.add(lblTransactionProcesses);
		
		// JLabel Execution Processes:
		
		JLabel lblExecutionProcesses = new JLabel("Number of processes to execute queries:");
		lblExecutionProcesses.setBounds(10, 96, 211, 14);
		panelModulesParameters.add(lblExecutionProcesses);
		
		// JLabel Number Transactions, shows the configured number of processes in the Configuration Window:
		
		JLabel lblNumberTransactions = new JLabel(cw.getTotalTransactionProcesses());
		lblNumberTransactions.setBounds(239, 71, 46, 14);
		panelModulesParameters.add(lblNumberTransactions);
		
		// JLabel Number Executes, shows the configured number of processes in the Configuration Window:
		
		JLabel lblNumberExecutes = new JLabel(cw.getTotalExecutionProcesses());
		lblNumberExecutes.setBounds(239, 96, 46, 14);
		panelModulesParameters.add(lblNumberExecutes);
		
		// JLabel Number Queries, shows the configured number of processes in the Configuration Window:
		
		JLabel lblNumberQueries = new JLabel(cw.getTotalQueryProcesses());
		lblNumberQueries.setBounds(239, 46, 46, 14);
		panelModulesParameters.add(lblNumberQueries);
		
		// JLabel Number Connections, shows the configured number of posible connections in the Configuration Window:
		
		JLabel lblNumberConnections = new JLabel(cw.getTotalConnections());
		lblNumberConnections.setBounds(239, 21, 46, 14);
		panelModulesParameters.add(lblNumberConnections);
		
		// The Current system data JPanel:
		
		JPanel panelCurrentSystemData = new JPanel();
		panelCurrentSystemData.setBounds(10, 147, 383, 125);
		panelCurrentSystemData.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Current system data", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(panelCurrentSystemData);
		panelCurrentSystemData.setLayout(null);
		
		// JLabel Clock time:
		
		JLabel lblClockTime = new JLabel("Clock time:");
		lblClockTime.setBounds(10, 47, 73, 14);
		panelCurrentSystemData.add(lblClockTime);
		
		// JLabel Current event:
		
		JLabel lblCurrentEvent = new JLabel("Current event:");
		lblCurrentEvent.setBounds(10, 72, 185, 14);
		panelCurrentSystemData.add(lblCurrentEvent);
		
		// The Event JSrollPane. Stores the Event JTable:
		
		JScrollPane eventScrollPane = new JScrollPane();
		eventScrollPane.setBounds(107, 65, 266, 42);
		panelCurrentSystemData.add(eventScrollPane);
		eventScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		eventScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		// The Event JTable. Shows the current event data: type, time and it's query ID:
		
		eventTable = new JTable();
		eventTable.setEnabled(false);
		eventScrollPane.setViewportView(eventTable);
		eventTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
			},
			new String[] {
				"Event type", "Time", "Query ID"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Double.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		eventTable.getColumnModel().getColumn(0).setResizable(false);
		eventTable.getColumnModel().getColumn(0).setPreferredWidth(114);
		eventTable.getColumnModel().getColumn(1).setResizable(false);
		eventTable.getColumnModel().getColumn(1).setPreferredWidth(15);
		eventTable.getColumnModel().getColumn(2).setResizable(false);
		eventTable.getColumnModel().getColumn(2).setPreferredWidth(15);
		
		// JLabel Simulation number:
		
		JLabel lblSimulationNumber = new JLabel("Simulation number:");
		lblSimulationNumber.setBounds(10, 22, 97, 14);
		panelCurrentSystemData.add(lblSimulationNumber);
		
		// JLabel Current clock (will be updated dynamically):
		
		lblCurrentClock = new JLabel("0");
		lblCurrentClock.setBounds(107, 47, 46, 14);
		panelCurrentSystemData.add(lblCurrentClock);
		
		// JLabel Number (will be updated dynamically):
		
		lblNumber = new JLabel("0");
		lblNumber.setBounds(107, 22, 46, 14);
		panelCurrentSystemData.add(lblNumber);
		
		// The Current Statistics JPanel:
		
		JPanel panelCurrentStatistics = new JPanel();
		panelCurrentStatistics.setBorder(new TitledBorder(null, "Current statistics", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCurrentStatistics.setBounds(403, 147, 383, 125);
		contentPane.add(panelCurrentStatistics);
		panelCurrentStatistics.setLayout(null);
		
		// JLabel Total connections served:
		
		JLabel lblTotalConnectionsServed = new JLabel("Total connections served:");
		lblTotalConnectionsServed.setBounds(10, 22, 164, 14);
		panelCurrentStatistics.add(lblTotalConnectionsServed);
		
		// JLabel Total connections discarded:
		
		JLabel lblTotalConnectionsDiscarded = new JLabel("Total connections discarded:");
		lblTotalConnectionsDiscarded.setBounds(10, 47, 149, 14);
		panelCurrentStatistics.add(lblTotalConnectionsDiscarded);
		
		// JLabel Events processed:
		
		JLabel lblEventsProcessed = new JLabel("Events processed:");
		lblEventsProcessed.setBounds(10, 72, 149, 14);
		panelCurrentStatistics.add(lblEventsProcessed);
		
		// JLabel Processed number (will be updated dynamically):
		
		lblProcessedNumber = new JLabel("0");
		lblProcessedNumber.setBounds(169, 72, 46, 14);
		panelCurrentStatistics.add(lblProcessedNumber);
		
		// JLabel Number discarded (will be updated dynamically):
		
		lblNumberDiscarded = new JLabel("0");
		lblNumberDiscarded.setBounds(169, 47, 46, 14);
		panelCurrentStatistics.add(lblNumberDiscarded);
		
		// JLabel Number served (will be updated dynamically):
		
		lblNumberServed = new JLabel("0");
		lblNumberServed.setBounds(169, 22, 46, 14);
		panelCurrentStatistics.add(lblNumberServed);
		
		// The Modules data JPanel:
		
		JPanel panelModulesData = new JPanel();
		panelModulesData.setBorder(new TitledBorder(null, "Modules data", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelModulesData.setBounds(10, 283, 776, 144);
		contentPane.add(panelModulesData);
		panelModulesData.setLayout(null);
		
		// The Modules JScrollPane. Stores the Modules JTable:
		
		JScrollPane modulesScrollPane = new JScrollPane();
		modulesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		modulesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		modulesScrollPane.setBounds(37, 21, 702, 106);
		panelModulesData.add(modulesScrollPane);
		
		// Modules JTable. Shows the current real data about each module in the simulation: module name,
		// if all the server are busy, the total servers busy, the queue length and the total of clients served.
		
		modulesTable = new JTable();
		modulesTable.setEnabled(false);
		modulesTable.setModel(new DefaultTableModel(
			new Object[][] {
				{"Client Administrator", null, null, "N/A", null},
				{"Process Administrator", null, null, null, null},
				{"Query Processor", null, null, null, null},
				{"Transaction Access", null, null, null, null},
				{"Query Executor", null, null, null, null},
			},
			new String[] {
				"Module", "All servers busy", "Busy servers", "Queue length", "Clients served"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Boolean.class, Integer.class, Integer.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		modulesTable.getColumnModel().getColumn(0).setResizable(false);
		modulesTable.getColumnModel().getColumn(1).setResizable(false);
		modulesTable.getColumnModel().getColumn(2).setResizable(false);
		modulesTable.getColumnModel().getColumn(3).setResizable(false);
		modulesTable.getColumnModel().getColumn(4).setResizable(false);
		modulesScrollPane.setViewportView(modulesTable);
		
		// The Simulation progress JPanel:
		
		JPanel panelSimulationProgress = new JPanel();
		panelSimulationProgress.setBorder(new TitledBorder(null, "Simulation progress", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSimulationProgress.setBounds(10, 438, 773, 223);
		contentPane.add(panelSimulationProgress);
		panelSimulationProgress.setLayout(null);
		
		// JLabel Current simulation progress:
		
		JLabel lblCurrentSimulationProgress = new JLabel("Current simulation progress:");
		lblCurrentSimulationProgress.setBounds(10, 21, 196, 14);
		panelSimulationProgress.add(lblCurrentSimulationProgress);
		
		// JLabel General progress:
		
		JLabel lblGeneralProgress = new JLabel("General programm progress:");
		lblGeneralProgress.setBounds(10, 46, 196, 14);
		panelSimulationProgress.add(lblGeneralProgress);
		
		// JLabel Console output:
		
		JLabel lblConsoleOutput = new JLabel("Console output:");
		lblConsoleOutput.setBounds(10, 71, 139, 14);
		panelSimulationProgress.add(lblConsoleOutput);
		
		// The general progress JProgressBar:
		
		generalProgressBar = new JProgressBar();
		generalProgressBar.setStringPainted(true);
		generalProgressBar.setBounds(212, 46, 551, 14);
		panelSimulationProgress.add(generalProgressBar);
		
		// The current simulation progress JProgressBar:
		
		currentSimulationProgressBar = new JProgressBar();
		currentSimulationProgressBar.setStringPainted(true);
		currentSimulationProgressBar.setBounds(212, 23, 551, 14);
		panelSimulationProgress.add(currentSimulationProgressBar);
		
		// The Console JScrollPane. Stores the Console JTextArea:
		
		JScrollPane consoleScrollPane = new JScrollPane();
		consoleScrollPane.setAutoscrolls(true);
		consoleScrollPane.setBounds(10, 89, 753, 120);
		panelSimulationProgress.add(consoleScrollPane);
		
		// The Console JTextArea. Work as the System.out.prinlt() call to write console lines in it.
		
		txtConsole = new JTextArea();
		txtConsole.setEditable(false);
		consoleScrollPane.setViewportView(txtConsole);
		txtConsole.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		// JButton Finish!:
		
		btnFinish = new JButton("Finish!");
		btnFinish.addActionListener(new ActionListener() {
			
			/**
			 * If the button is pressed, the current window will be closed and it opens the Current
			 * Statistics window to fill it with the weghted total statistics.
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				dispose();
				if(!cs.isVisible()){
					cs.setVisible(true);
				}
				cs.fillWindowData(-1);
			}
		});
		btnFinish.setEnabled(false);
		btnFinish.setBounds(697, 669, 89, 23);
		contentPane.add(btnFinish);
		
		// JButtonStatistics:
		
		JButton btnStatistics = new JButton("Statistics");
		btnStatistics.addActionListener(new ActionListener() {
			
			/**
			 * If the button is pressed, the Current Statistics window will be open to let the
			 * user choose a simulation to view it's statistics.
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				cs.setVisible(true);
			}
		});
		btnStatistics.setBounds(499, 669, 89, 23);
		contentPane.add(btnStatistics);
	}
	
	/**
	 * Updates the data in the window.
	 * @param a The string to write in the window's console output
	 * @param e The event to show it's data in the event table.
	 */
	public void update(String a, Event e){
		
		// Write in the console:
		
		txtConsole.append(a + "\n");
		txtConsole.setCaretPosition(txtConsole.getDocument().getLength());
		
		// Update the table:
		
		eventTable.setValueAt((e.getType()).toString(), 0, 0);
		eventTable.setValueAt(e.getTime(), 0, 1);
		eventTable.setValueAt(e.getQuery().getID(), 0, 2);
		
		// Updates the current clock time and the total processed connections.
		
		this.lblCurrentClock.setText(String.format("%.3f", e.getTime()));
		this.lblProcessedNumber.setText(""+((Integer.parseInt(this.lblProcessedNumber.getText()))+1));
	}
	
	/**
	 * Updates the current simulation progress bar based on the current clock time and it's remaining
	 * time to reach the time to run the simulation.
	 * @param e The event to obtains it's current clock time.
	 */
	public void updateBar(Event e){
		double number = e.getTime()/(Double.parseDouble(this.lblNumberSeconds.getText())*0.01);
		if(number <= 99.99){ // Because of the mathematical operation, is posible that the bar
							 // reaches more than 100% (about 105%).
			currentSimulationProgressBar.setString(String.format("%.2f", number) + "%");
			currentSimulationProgressBar.setValue((int) Math.round(number));
		}
	}
	
	/**
	 * Set the current simulation progress bar as finished and updates the general progress bar based on the current
	 * simulation's index and it's remaining value to reach the total of simulations that will be processed.
	 */
	public void setBarFinished(){
		if(!currentSimulationProgressBar.getString().equals("100%")){
			currentSimulationProgressBar.setString("100%");
			currentSimulationProgressBar.setValue(100);
		}
		double number = (Double.parseDouble(this.lblNumber.getText()) / Double.parseDouble(this.lblNumberTimes.getText())) / 0.01;
		generalProgressBar.setString(String.format("%.2f", number) + "%");
		generalProgressBar.setValue((int) Math.round(number));
	}
	
	/**
	 * Let the user to press the Finish! button to view the weighted total simulation data.
	 */
	public void setFinishAvailable(){
		generalProgressBar.setString("100%");
		generalProgressBar.setValue(100);
		this.btnFinish.setEnabled(true);
		this.btnStop.setEnabled(false);
	}
	
	/**
	 * Updates the data in the Modules table with the current modules data in the current simulation.
	 * @param cam The simulation's Client Administrator Module.
	 * @param pam The simulation's Process Administrator Module.
	 * @param qpm The simulation's Query Processor Module.
	 * @param tam The simulation's Transaction Access Module.
	 * @param qem The simulation's Query Executor Module.
	 */
	public void updateModules(ClientAdministrator cam, ProcessAdministrator pam, 
		QueryProcesor qpm, TransactionAccess tam, QueryExecutor qem){
		modulesTable.setValueAt((cam.getCapacity() == cam.getTotalBusy()), 0, 1);
		modulesTable.setValueAt(cam.getTotalBusy(), 0, 2);
		modulesTable.setValueAt(cam.getTotalArrived(), 0, 4);
		
		modulesTable.setValueAt(pam.isBusy(), 1, 1);
		if(pam.isBusy()){
			modulesTable.setValueAt(1, 1, 2);
		}else{
			modulesTable.setValueAt(0, 1, 2);
		}
		
		modulesTable.setValueAt(pam.getQueueSize(), 1, 3);
		modulesTable.setValueAt(pam.getTotalArrived(), 1, 4);
		
		modulesTable.setValueAt((qpm.getCapacity() == qpm.getTotalBusy()), 2, 1);
		modulesTable.setValueAt(qpm.getTotalBusy(), 2, 2);
		modulesTable.setValueAt(qpm.getQueueSize(), 2, 3);
		modulesTable.setValueAt(qpm.getTotalArrived(), 2, 4);
		
		modulesTable.setValueAt((tam.getCapacity() == tam.getTotalBusy()), 3, 1);
		modulesTable.setValueAt(tam.getTotalBusy(), 3, 2);
		modulesTable.setValueAt(tam.getQueueSize(), 3, 3);
		modulesTable.setValueAt(tam.getTotalArrived(), 3, 4);
		
		modulesTable.setValueAt((qem.getCapacity() == qem.getTotalBusy()), 4, 1);
		modulesTable.setValueAt(qem.getTotalBusy(), 4, 2);
		modulesTable.setValueAt(qem.getQueueSize(), 4, 3);
		modulesTable.setValueAt(qem.getTotalArrived(), 4, 4);
	}
	
	/**
	 * Updates the total of served connections.
	 */
	public void updateServed(){
		this.lblNumberServed.setText("" + (Integer.parseInt(this.lblNumberServed.getText()) + 1));
	}
	
	/**
	 * Updates the total of discarded connections.
	 */
	public void updateDiscarded(){
		this.lblNumberDiscarded.setText("" + (Integer.parseInt(this.lblNumberDiscarded.getText()) + 1));
	}
	
	/**
	 * Update the current simulation index.
	 * @param data The "staring new simulation" text to write in console.
	 */
	public void updateSimulationNumber(String data){
		this.txtConsole.append(data + "\n");
		this.txtConsole.setCaretPosition(this.txtConsole.getDocument().getLength());
		this.lblNumber.setText(""+(Integer.parseInt(this.lblNumber.getText())+1));
		
		// Resets the dynamic labels:
		
		this.lblNumberServed.setText("0");
		this.lblNumberDiscarded.setText("0");
		this.lblProcessedNumber.setText("0");
	}
	
	/**
	 * Gets the current simulation index.
	 * @return The simulation index.
	 */
	public int getSimulationNumber(){
		return Integer.parseInt(this.lblNumber.getText());
	}
	
	/**
	 * Gets the program simulation pointer by the window.
	 * @return The program simulation.
	 */
	public Simulation getSimulation(){
		return this.sim;
	}
}
