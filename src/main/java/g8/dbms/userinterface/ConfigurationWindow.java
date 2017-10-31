package g8.dbms.userinterface;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import g8.dbms.simulator.Simulation;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 * Configuration Window GUI. Lets the user to configure the simulation.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 */
public class ConfigurationWindow extends JFrame {

	// GUI elements needed from methods outside the JFrame constructor.
	
	private JPanel contentPane;
	private JTextField txtSeconds;
	private JTextField txtTimeout;
	private JTextField txtConnections;
	private JTextField txtQueryProcesses;
	private JTextField txtTransactionProcesses;
	private JTextField txtExecutionProcesses;
	private JTextField txtDelay;
	private JTextField txtTimes;
	private JRadioButton rdbtnActivated;
	private JRadioButton rdbtnDeactivated;
	
	// End of the GUI elements declaration.
	
	private Simulation sim;	// A pointer to the program's simulation.

	/**
	 * Class constructor. It creates the frame.
	 */
	public ConfigurationWindow() {
		
		// Main JFrame properties:
		
		setResizable(false);
		setTitle("Simulator configuration");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 439);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// The Simulation parameters JPanel:
		
		JPanel panelSimulationParameters = new JPanel();
		panelSimulationParameters.setBorder(new TitledBorder(null, "Simulation parameters", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSimulationParameters.setBounds(10, 11, 374, 104);
		contentPane.add(panelSimulationParameters);
		panelSimulationParameters.setLayout(null);
		
		// JLabel Times:
		
		JLabel lblTimes = new JLabel("Number of times to run the simulation:");
		lblTimes.setBounds(10, 21, 241, 14);
		panelSimulationParameters.add(lblTimes);
		
		// JLabel Seconds:
		
		JLabel lblSeconds = new JLabel("Total seconds to run each simulation:");
		lblSeconds.setBounds(10, 46, 220, 14);
		panelSimulationParameters.add(lblSeconds);
		
		// JLabel Timeout:
		
		JLabel lblTimeout = new JLabel("Seconds to terminates a connection (timeout):");
		lblTimeout.setBounds(10, 71, 241, 14);
		panelSimulationParameters.add(lblTimeout);
		
		// JTextField Seconds to add the data requested with the JLabel Seconds:
		
		txtSeconds = new JTextField();
		txtSeconds.addKeyListener(new KeyAdapter() {
			
			/**
			 * Controls the characters insertion. Only numbers and the dot "decimal separator"
			 * are allowed.
			 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyTyped(KeyEvent e) {
				if((e.getKeyChar() < 48 || e.getKeyChar() > 57) && e.getKeyChar() != '.'){
					e.consume();
				}
			}
		});
		txtSeconds.setBounds(278, 43, 86, 20);
		panelSimulationParameters.add(txtSeconds);
		txtSeconds.setColumns(10);
		
		// JTextField Timeout to add the data requested with the JLabel Timeout:
		
		txtTimeout = new JTextField();
		txtTimeout.addKeyListener(new KeyAdapter() {
			
			/**
			 * Controls the characters insertion. Only numbers and the dot "decimal separator"
			 * are allowed.
			 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyTyped(KeyEvent e) {
				if((e.getKeyChar() < 48 || e.getKeyChar() > 57) && e.getKeyChar() != '.'){
					e.consume();
				}
			}
		});
		txtTimeout.setBounds(278, 68, 86, 20);
		panelSimulationParameters.add(txtTimeout);
		txtTimeout.setColumns(10);
		
		// JTextField Times to add the data requested with the JLabel Times:
		
		txtTimes = new JTextField();
		txtTimes.addKeyListener(new KeyAdapter() {
			
			/**
			 * Controls the characters insertion. Only numbers are allowed.
			 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(arg0.getKeyChar() < 48 || arg0.getKeyChar() > 57){
					arg0.consume();
				}
			}
		});
		txtTimes.setBounds(278, 18, 86, 20);
		panelSimulationParameters.add(txtTimes);;
		
		// The Modules parameters JPanel:
		
		JPanel panelModulesParameters = new JPanel();
		panelModulesParameters.setBorder(new TitledBorder(null, "Modules parameters", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelModulesParameters.setBounds(10, 126, 374, 131);
		contentPane.add(panelModulesParameters);
		panelModulesParameters.setLayout(null);
		
		// JLabel Connections:
		
		JLabel lblConnections = new JLabel("Number of posible connections:");
		lblConnections.setBounds(10, 21, 268, 14);
		panelModulesParameters.add(lblConnections);
		
		// JLabel Query Processes:
		
		JLabel lblQueryProcesses = new JLabel("Number of concurrent query processes:");
		lblQueryProcesses.setBounds(10, 46, 211, 14);
		panelModulesParameters.add(lblQueryProcesses);
		
		// JLabel Transaction Processes:
		
		JLabel lblTransactionProcesses = new JLabel("Number of processes to execute transactions:");
		lblTransactionProcesses.setBounds(10, 71, 255, 14);
		panelModulesParameters.add(lblTransactionProcesses);
		
		// JLabel Execution Processes:
		
		JLabel lblExecutionProcesses = new JLabel("Number of processes to execute queries:");
		lblExecutionProcesses.setBounds(10, 96, 232, 14);
		panelModulesParameters.add(lblExecutionProcesses);
		
		// JTextField Connections to add the data requested with the JLabel Connections:
		
		txtConnections = new JTextField();
		txtConnections.addKeyListener(new KeyAdapter() {
			
			/**
			 * Controls the characters insertion. Only numbers are allowed.
			 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() < 48 || e.getKeyChar() > 57){
					e.consume();
				}
			}
		});
		txtConnections.setBounds(278, 18, 86, 20);
		panelModulesParameters.add(txtConnections);
		txtConnections.setColumns(10);
		
		// JTextField Query processes to add the data requested with the JLabel Query processes:
		
		txtQueryProcesses = new JTextField();
		txtQueryProcesses.addKeyListener(new KeyAdapter() {
			
			/**
			 * Controls the characters insertion. Only numbers are allowed.
			 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() < 48 || e.getKeyChar() > 57){
					e.consume();
				}
			}
		});
		txtQueryProcesses.setBounds(278, 43, 86, 20);
		panelModulesParameters.add(txtQueryProcesses);
		txtQueryProcesses.setColumns(10);
		
		// JTextField Transaction processes to add the data requested with the JLabel Transaction processes:
		
		txtTransactionProcesses = new JTextField();
		txtTransactionProcesses.addKeyListener(new KeyAdapter() {
			
			/**
			 * Controls the characters insertion. Only numbers are allowed.
			 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() < 48 || e.getKeyChar() > 57){
					e.consume();
				}
			}
		});
		txtTransactionProcesses.setBounds(278, 68, 86, 20);
		panelModulesParameters.add(txtTransactionProcesses);
		txtTransactionProcesses.setColumns(10);
		
		// JTextField Execution processes to add the data requested with the JLabel Execution processes:
		
		txtExecutionProcesses = new JTextField();
		txtExecutionProcesses.addKeyListener(new KeyAdapter() {
			
			/**
			 * Controls the characters insertion. Only numbers are allowed.
			 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() < 48 || e.getKeyChar() > 57){
					e.consume();
				}
			}
		});
		txtExecutionProcesses.setBounds(278, 93, 86, 20);
		panelModulesParameters.add(txtExecutionProcesses);
		txtExecutionProcesses.setColumns(10);
		
		// The Delay configuration JPanel:
		
		JPanel panelDelayConfiguration = new JPanel();
		panelDelayConfiguration.setBorder(new TitledBorder(null, "Delay configuration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDelayConfiguration.setBounds(10, 268, 374, 104);
		contentPane.add(panelDelayConfiguration);
		panelDelayConfiguration.setLayout(null);
		
		// JLabel Delay boolean:
		
		JLabel lblDelayBool = new JLabel("Screen delay:");
		lblDelayBool.setBounds(10, 21, 139, 14);
		panelDelayConfiguration.add(lblDelayBool);
		
		// RadioButton Activated:
		
		rdbtnActivated = new JRadioButton("Activated");
		rdbtnActivated.addActionListener(new ActionListener() {
			
			/**
			 * If the delay option's RadioButton Activated is selected, the JTextField Delay will
			 * be enabled to introduce the delay seconds.
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				txtDelay.setEnabled(true);
			}
		});
		rdbtnActivated.setBounds(10, 42, 109, 23);
		panelDelayConfiguration.add(rdbtnActivated);
		
		// RadioButton Deactivated:
		
		rdbtnDeactivated = new JRadioButton("Deactivated");
		rdbtnDeactivated.addActionListener(new ActionListener() {
			
			/**
			 * If the delay option's RadioButton Deactivated is selected, the JTextField Delay will
			 * be disabled and don't let to introduce the delay seconds.
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				txtDelay.setEnabled(false);
			}
		});
		rdbtnDeactivated.setSelected(true);
		rdbtnDeactivated.setBounds(10, 68, 109, 23);
		panelDelayConfiguration.add(rdbtnDeactivated);
		
		// ButtonGroup to join the RadioButtons behavior.
		
		ButtonGroup rdbtnGroupDelay = new ButtonGroup();
		rdbtnGroupDelay.add(rdbtnActivated);
		rdbtnGroupDelay.add(rdbtnDeactivated);
		
		// JTextField Delay to add the data requested with the JLabel Delay Seconds:
		
		txtDelay = new JTextField();
		txtDelay.addKeyListener(new KeyAdapter() {
			
			/**
			 * Controls the characters insertion. Only numbers and the dot "decimal separator"
			 * are allowed.
			 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyTyped(KeyEvent e) {
				if((e.getKeyChar() < 48 || e.getKeyChar() > 57) && e.getKeyChar() != '.'){
					e.consume();
				}
			}
		});
		txtDelay.setEnabled(false);
		txtDelay.setBounds(278, 43, 86, 20);
		panelDelayConfiguration.add(txtDelay);
		txtDelay.setColumns(10);
		
		//JLabel Delay Seconds:
		
		JLabel lblDelaySeconds = new JLabel("Delay seconds:");
		lblDelaySeconds.setBounds(278, 21, 86, 14);
		panelDelayConfiguration.add(lblDelaySeconds);
		
		// Button Cancel:
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			
			/**
			 * If the button is pressed, the window will be closed.
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(295, 377, 89, 23);
		contentPane.add(btnCancel);
		
		final ConfigurationWindow cw = this; // To send the current window as a reference later.
		
		// Button Run!:
		
		JButton btnRun = new JButton("Run!");
		btnRun.addActionListener(new ActionListener() {
			
			/**
			 * If the button is pressed, the current window will be closed and the simulation will start.
			 * Also, a new window to let the user view the simulation progress will appear.
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				dispose();
				
				sim = new Simulation(Integer.parseInt(txtConnections.getText()), Integer.parseInt(txtQueryProcesses.getText()),
						Integer.parseInt(txtTransactionProcesses.getText()), Integer.parseInt(txtExecutionProcesses.getText()),
						Double.parseDouble(txtTimeout.getText()), Integer.parseInt(txtTimes.getText()),
						Double.parseDouble(txtSeconds.getText()));
				if(txtDelay.isEnabled()){
					sim.setDelay(Double.parseDouble(txtDelay.getText()) * 1000);
				}
				
				RunningWindow frame = new RunningWindow(sim, cw); // The new window to appear.
				sim.setRunningWindow(frame);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				sim.start(); // The simulation progress start in a different thread.
			}
		});
		btnRun.setBounds(196, 377, 89, 23);
		contentPane.add(btnRun);
	}
	
	/**
	 * Gets the total times to run the simulation that the user introduced.
	 * @return The times to run the simulation.
	 */
	public String getTimes(){
		return this.txtTimes.getText();
	}
	
	/**
	 * Gets the total of seconds to run the simulation that the user introduced.
	 * @return The seconds to run the simulation.
	 */
	public String getSeconds(){
		return this.txtSeconds.getText();
	}
	
	/**
	 * Gets the limit timeout to run the simulation that the user introduced.
	 * @return The limit timeout to run the simulation.
	 */
	public String getTimeout(){
		return this.txtTimeout.getText();
	}
	
	/**
	 * Gets the the total of possible connections that the user introduced.
	 * This is the number of services in the Client Administrator Module.
	 * @return The total of possible connections.
	 */
	public String getTotalConnections(){
		return this.txtConnections.getText();
	}
	
	/**
	 * Gets the the total of query processes that the user introduced.
	 * This is the number of services in the Query Processor Module.
	 * @return The total of query processes.
	 */
	public String getTotalQueryProcesses(){
		return this.txtQueryProcesses.getText();
	}
	
	/**
	 * Gets the the total of transaction processes that the user introduced.
	 * This is the number of services in the Transaction Access Module.
	 * @return The total of transaction processes.
	 * @return
	 */
	public String getTotalTransactionProcesses(){
		return this.txtTransactionProcesses.getText();
	}
	
	/**
	 * Gets the the total of execution processes that the user introduced.
	 * This is the number of services in the Query Executor Module.
	 * @return The total of execution processes.
	 */
	public String getTotalExecutionProcesses(){
		return this.txtExecutionProcesses.getText();
	}
	
	/**
	 * Gets the delay seconds that the user introduced.
	 * @return The delay seconds.
	 */
	public String getDelaySeconds(){
		return this.txtDelay.getText();
	}
}
