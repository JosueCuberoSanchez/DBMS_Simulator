package g8.dbms.userinterface;

import java.awt.Desktop;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import g8.dbms.simulator.Statistics;
import g8.dbms.simulator.event.enumerator.QueryType;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

/**
 * Current Statistics Window GUI. Lets the user to show each individual simulation's statistics or
 * the total weighted statistics. In that last case, lets the user to view the HTML files created
 * and it's path in the system.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 *
 */
public class CurrentStatistics extends JFrame {

	// GUI elements needed from methods outside the JFrame constructor.
	
	private JPanel contentPane;
	private JTable modulesTable;
	private JTable queriesTable;
	private JComboBox comboBoxSimulation;
	private RunningWindow rw;
	private JLabel lblNumberAvg;
	private JLabel lblNumberDiscarded;
	private JLabel lblSelectSimulationNumber;
	private JButton btnViewHtml;
	private JLabel lblNumberRequested;
	private JLabel lblTotalExceeded;
	private JLabel lblNumberExceeded;	
	
	// End of the GUI elements declaration.
	
	/**
	 * Class constructor. It creates the frame.
	 * @param window The ancestor Running Window.
	 */
	public CurrentStatistics(RunningWindow window) {
		rw = window;	// A pointer to the ancestor Running Window.
		
		// Main JFrame properties:
		
		setTitle("Current statistics expanded data");
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 586, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// The Modules statistics JPanel:
		
		JPanel panelModulesStatistics = new JPanel();
		panelModulesStatistics.setBorder(new TitledBorder(null, "Modules statistics", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelModulesStatistics.setBounds(10, 36, 561, 144);
		contentPane.add(panelModulesStatistics);
		panelModulesStatistics.setLayout(null);
		
		// The Modules JScrollPane to stores the Modules JTable.
		
		JScrollPane modulesScrollPane = new JScrollPane();
		modulesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		modulesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		modulesScrollPane.setBounds(10, 21, 541, 106);
		panelModulesStatistics.add(modulesScrollPane);
		
		// Modules JTable. It will show the Module name, the module's average queue size (Lq), average
		// queue time (Wq) and average idle time.
		
		modulesTable = new JTable();
		modulesTable.setEnabled(false);
		modulesTable.setModel(new DefaultTableModel(
			new Object[][] {
				{"Cient Administrator", 0, 0, 0},
				{"Process Administrator", 0, 0, 0},
				{"Query Processor", 0, 0, 0},
				{"Transaction Access", 0, 0, 0},
				{"Query Executor", 0, 0, 0},
			},
			new String[] {
				"Module name", "Avg. queue size", "Avg. queue time", "Avg. idle time"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Double.class, Double.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		modulesTable.getColumnModel().getColumn(1).setResizable(false);
		modulesScrollPane.setViewportView(modulesTable);
		
		// The Queries life time JPanel:
		
		JPanel panelQueriesLifetime = new JPanel();
		panelQueriesLifetime.setBorder(new TitledBorder(null, "Queries lifetime in each module", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelQueriesLifetime.setBounds(10, 191, 561, 128);
		contentPane.add(panelQueriesLifetime);
		panelQueriesLifetime.setLayout(null);
		
		// The Queries ScrollPane to stores the Queries JTable:
		
		JScrollPane queriesScrollPane = new JScrollPane();
		queriesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		queriesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		queriesScrollPane.setBounds(10, 21, 541, 90);
		panelQueriesLifetime.add(queriesScrollPane);
		
		// The Queries JTable. It will show the query type, and it's average time (W) in each system module.
		
		queriesTable = new JTable();
		queriesTable.setEnabled(false);
		queriesTable.setModel(new DefaultTableModel(
			new Object[][] {
				{"DDL", 0, 0, 0, 0, 0},
				{"UPDATE", 0, 0, 0, 0, 0},
				{"JOIN", 0, 0, 0, 0, 0},
				{"SELECT", 0, 0, 0, 0, 0},
			},
			new String[] {
				"Query type", "Client Admin.", "Process Admin.", "Query Processor", "Trans. Access", "Query Executor"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Double.class, Double.class, Double.class, Double.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		queriesTable.getColumnModel().getColumn(0).setResizable(false);
		queriesTable.getColumnModel().getColumn(1).setResizable(false);
		queriesTable.getColumnModel().getColumn(2).setResizable(false);
		queriesTable.getColumnModel().getColumn(3).setResizable(false);
		queriesTable.getColumnModel().getColumn(4).setResizable(false);
		queriesTable.getColumnModel().getColumn(5).setResizable(false);
		queriesScrollPane.setViewportView(queriesTable);
		
		// The General data JPanel:
		
		JPanel panelGeneralData = new JPanel();
		panelGeneralData.setBorder(new TitledBorder(null, "General data", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelGeneralData.setBounds(10, 330, 561, 73);
		contentPane.add(panelGeneralData);
		panelGeneralData.setLayout(null);
		
		// JLabel Total discarded:
		
		JLabel lblTotalDiscarded = new JLabel("Total connections discarded:");
		lblTotalDiscarded.setBounds(10, 21, 149, 14);
		panelGeneralData.add(lblTotalDiscarded);
		
		// JLabel Average connection life time (W):
		
		JLabel lblAverageConnectionLifetime = new JLabel("Average connection lifetime:");
		lblAverageConnectionLifetime.setBounds(286, 21, 155, 14);
		panelGeneralData.add(lblAverageConnectionLifetime);
		
		// JLabel Number discarded (will be updated dynamically):
		
		lblNumberDiscarded = new JLabel("0");
		lblNumberDiscarded.setBounds(169, 21, 46, 14);
		panelGeneralData.add(lblNumberDiscarded);
		
		// JLabel Number average (will be updated dynamically):
		
		lblNumberAvg = new JLabel("0");
		lblNumberAvg.setBounds(505, 21, 46, 14);
		panelGeneralData.add(lblNumberAvg);
		
		// JLabel Total requested:
		
		JLabel lblTotalRequested = new JLabel("Total connections requested:");
		lblTotalRequested.setBounds(10, 46, 149, 14);
		panelGeneralData.add(lblTotalRequested);
		
		// JLabel Number requested (will be updated dynamically):
		
		lblNumberRequested = new JLabel("0");
		lblNumberRequested.setBounds(169, 46, 46, 14);
		panelGeneralData.add(lblNumberRequested);
		
		// JLabel Total exceeded:
		
		lblTotalExceeded = new JLabel("Connections that exceeded the timeout:");
		lblTotalExceeded.setBounds(286, 46, 205, 14);
		panelGeneralData.add(lblTotalExceeded);
		
		// JLabel Number exceeded (will be updated dynamically):
		
		lblNumberExceeded = new JLabel("0");
		lblNumberExceeded.setBounds(505, 46, 46, 14);
		panelGeneralData.add(lblNumberExceeded);
		
		// JButton OK:
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			
			/**
			 * If the window shows the individual statistics (the ComboBox is visible when that),
			 * the window will be closed, and if it shows the final weighted statistics, then it
			 * shows the link to the HTML file too.
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				dispose();
				if(!comboBoxSimulation.isVisible()){
					JOptionPane.showMessageDialog(null, "You can find the \"index.html\" file in the following path: " +
				"\n" + System.getProperty("user.dir") + File.separator + "html");
				}
			}
		});
		btnOk.setBounds(482, 412, 89, 23);
		contentPane.add(btnOk);
		
		// JLabel Select simulation number:
		
		lblSelectSimulationNumber = new JLabel("Select simulation number:");
		lblSelectSimulationNumber.setBounds(10, 11, 137, 14);
		contentPane.add(lblSelectSimulationNumber);
		
		// ComboBox Simulation. It lets the user to select a simulation number to view it's statistics:
		
		comboBoxSimulation = new JComboBox();
		comboBoxSimulation.setToolTipText("");
		comboBoxSimulation.addActionListener(new ActionListener() {
			
			/**
			 * If a number is selected (since the index 1), all the data in the window will be
			 * filled with that indexed simulation's statistics data.
			 * Else, if the "Select an option" option is selected (index 0), the window will
			 * show only 0's in every field again.
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				int selected = comboBoxSimulation.getSelectedIndex();
				if((selected != 0) && (selected <= rw.getSimulationNumber())){
					fillWindowData(selected);
				}else if (selected == 0){
					fillEmptyTable();
				}
			}
		});
		comboBoxSimulation.setBounds(157, 8, 110, 20);
		contentPane.add(comboBoxSimulation);
		
		// JButton View HTML:
		
		btnViewHtml = new JButton("View HTML");
		btnViewHtml.addActionListener(new ActionListener() {
			
			/**
			 * If the button is pressed, the "index.html" file will be open with the system default
			 * app selected to open this type of files.
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				File file = new File("." + File.separator + "html" + File.separator + "index.html");
				try {
					Desktop.getDesktop().open(file);
				} catch (IOException | IllegalArgumentException e) {
					JOptionPane.showMessageDialog(null, "Cannot open the HTML file.", "Opening file error", JOptionPane.ERROR_MESSAGE, null);
				}
			}
		});
		btnViewHtml.setBounds(383, 412, 89, 23);
		contentPane.add(btnViewHtml);
		btnViewHtml.setVisible(false);
	}
	
	/**
	 * Fill the ComboBox with the total of simulations that will be processed in an execution.
	 * @param size The total of simulation to process.
	 */
	public void fillCombobox(int size){
		String[] data = new String[size + 1];
		data[0] = "Select an option";
		for(int i = 1; i < data.length; i++){
			data[i] = " " + i;
		}
		comboBoxSimulation.setModel(new DefaultComboBoxModel(data));
	}
	
	/**
	 * Fills the two tables in the window with 0 and the dynamic labels.
	 */
	public void fillEmptyTable(){
		for(int i = 0; i < 5; i++){
			for(int j = 1; j <= 3; j++){
				modulesTable.setValueAt(0.0, i, j);
			}
		}
		
		for(int i = 0; i < 4; i++){
			for(int j = 1; j <= 5; j++){
				queriesTable.setValueAt(0.0, i, j);
			}
		}
		
		this.lblNumberAvg.setText("0");
		this.lblNumberDiscarded.setText("0");
		this.lblNumberExceeded.setText("0");
		this.lblNumberRequested.setText("0");
	}
	
	/**
	 * Fills every field in the window with the current simulation statistics.
	 * @param index The simulation's index selected by the user in the ComboBox if is not -1.
	 * 		  If is -1, the window will change to show only the weighted total simulation data.
	 * 		  and will be prepared to let the program finish.
	 */
	public void fillWindowData(int index){
		Statistics stats = null;
		if(index == -1){ // If the simulation to show is the last.
			stats = this.rw.getSimulation().getFinalStatistics();
			this.comboBoxSimulation.setVisible(false);
			this.btnViewHtml.setVisible(true);
			this.lblSelectSimulationNumber.setText("Final simulation statistics:");
			this.lblNumberExceeded.setText("" + stats.getTotalKilledQueries());
		}else if(index < comboBoxSimulation.getItemCount()){ // If its an individual simulation.
			stats = this.rw.getSimulation().getStatistics(index - 1);
			this.lblNumberExceeded.setText("" + stats.getKilledQueries());
		}
		if(stats != null){ // If the simulation is processed already.
			modulesTable.setValueAt(stats.getLq(0), 0, 1);
			modulesTable.setValueAt(stats.getAverageQueueTime(0), 0, 2);
			modulesTable.setValueAt(stats.getIdleTime(0), 0, 3);
			modulesTable.setValueAt(stats.getLq(1), 1, 1);
			modulesTable.setValueAt(stats.getAverageQueueTime(1), 1, 2);
			modulesTable.setValueAt(stats.getIdleTime(1), 1, 3);
			modulesTable.setValueAt(stats.getLq(2), 2, 1);
			modulesTable.setValueAt(stats.getAverageQueueTime(2), 2, 2);
			modulesTable.setValueAt(stats.getIdleTime(2), 2, 3);
			modulesTable.setValueAt(stats.getLq(3), 3, 1);
			modulesTable.setValueAt(stats.getAverageQueueTime(3), 3, 2);
			modulesTable.setValueAt(stats.getIdleTime(3), 3, 3);
			modulesTable.setValueAt(stats.getLq(4), 4, 1);
			modulesTable.setValueAt(stats.getAverageQueueTime(4), 4, 2);
			modulesTable.setValueAt(stats.getIdleTime(4), 4, 3);
			
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.DDL,0), 0, 1);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.DDL,1), 0, 2);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.DDL,2), 0, 3);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.DDL,3), 0, 4);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.DDL,4), 0, 5);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.UPDATE,0), 1, 1);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.UPDATE,1), 1, 2);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.UPDATE,2), 1, 3);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.UPDATE,3), 1, 4);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.UPDATE,4), 1, 5);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.JOIN,0), 2, 1);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.JOIN,1), 2, 2);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.JOIN,2), 2, 3);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.JOIN,3), 2, 4);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.JOIN,4), 2, 5);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.SELECT,0), 3, 1);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.SELECT,1), 3, 2);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.SELECT,2), 3, 3);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.SELECT,3), 3, 4);
			queriesTable.setValueAt(stats.getQueryTimeByTypeInModule(QueryType.SELECT,4), 3, 5);
			
			this.lblNumberAvg.setText(String.format("%.3f", stats.getAverageTime(5)));
			this.lblNumberDiscarded.setText("" + stats.getDiscarted());
			this.lblNumberRequested.setText("" + stats.getAttemptedConnections());
		}else{ // If the simulation is not finished, it's statistics are not ready. Then, the window
			   // data will be cleared.
			this.fillEmptyTable();
		}
	}
}

