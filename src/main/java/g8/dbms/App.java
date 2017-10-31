package g8.dbms;

import java.awt.EventQueue;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import g8.dbms.userinterface.ConfigurationWindow;

/**
 * Class App. Executes the main method to start the program.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam
 *
 */
public class App 
{
    /**
     * Main method to start the program.
     * @param args The arguments to run the main method.
     */
    public static void main( String[] args )
    {
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			JOptionPane.showMessageDialog(null, "Failed to set the program appearance to the\n system default.",
					"Appearance error", JOptionPane.ERROR_MESSAGE);
		}
		
		EventQueue.invokeLater(new Runnable() {
			
			/**
			 * run method to implement the Runnable Java interface and run the current thread with it
			 */
			public void run() {
				try {
					ConfigurationWindow frame = new ConfigurationWindow();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Failed to run the program. Cannot set the thread to execute it.",
							"Execution error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
    }
}
