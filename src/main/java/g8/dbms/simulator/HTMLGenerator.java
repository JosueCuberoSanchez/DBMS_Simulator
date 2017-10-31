package g8.dbms.simulator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import g8.dbms.simulator.event.enumerator.QueryType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;


/**
 * HTMLGenerator class. Creates HTML files to save and show the entire simulations statistics.
 * 
 * @author Josue Cubero, Juan Rodriguez, Kevin Waltam.
 *
 */
public class HTMLGenerator {
	private VelocityEngine apacheVelocity;	//
	private Template template;				//
	private VelocityContext context;		//
	private StringWriter writer;			//
	private String path;					//
	private Statistics scr;					//
	private int totalRuns;					//
	private int currentRun;					//
	private String links;					//
	
		
	/**
	 * Class constructor. Works for each individual HTML page.
	 * @param stats The Statistics class instance to get the general simulation statistics.
	 * @param p The path to create the HTML file.
	 * @param t The total times that the simulation was executed in the program.
	 * @param n The current simulation run number in one execution.
	 */
	public HTMLGenerator(Statistics stats, String p, int t, int n){
		this.apacheVelocity = new VelocityEngine();
        this.apacheVelocity.init();
		this.path= p;
		this.scr = stats;
		this.totalRuns = t;
		this.currentRun = n;		
		this.template = this.apacheVelocity.getTemplate("." + File.separator + "html" + File.separator + "prueba.html");
		this.writer = new StringWriter();
		this.context = new VelocityContext();
		this.fillContext();
		this.fillModules();
	}
	
	/**
	 * Class constructor. Works for the principal HTML "index" page.
	 * @param s The Statistics class instance to get the simulation statistics.
	 * @param total Number of files to create with the generator.
	 * @param k Total of servers in the Client Administrator Module in the simulation.
	 * @param n Total of servers in the Query Processor Module in the simulation.
	 * @param p Total of servers in the Transaction Access Module in the simulation.
	 * @param m Total of servers in the Query Executor in the simulation.
	 */
	public HTMLGenerator(Statistics s, int total, int k, int n, int p, int m){
		this.scr = s;
		this.apacheVelocity = new VelocityEngine();
        this.apacheVelocity.init();
        this.context = new VelocityContext();
        this.writer = new StringWriter();
        this.links = new String();
        this.generateLinks(links, total);
		this.path = "./html/index.html";  
		this.template = this.apacheVelocity.getTemplate("." + File.separator + "html" + File.separator + "start.html");
		this.fillContext();
		this.context.put("totalC", k);
		this.context.put("totalqpm", n);
		this.context.put("totaltam", p);
		this.context.put("totalQPM", m);
	}
	
	/**
	 * Creates every HTML links that will be the files to save the simulation statistics.
	 * @param s Saves the files names as links to create and connect the HTML files later.
	 * @param t Total of simulations executed to create this same total of HTML files.
	 */
	private void generateLinks(String s, int t){
		for(int i=1; i<t ;i++){
			s+="<tr><td> "+(i+1)+" </td><td> <a href=\"run-"+(i+1)+".html\">run-"+(i+1)+".html</a> </td></tr>";
		}
		this.context.put("tableLinks", s);
	}
	
	/**
	 * Fills an HTML file with the simulation general statistics.
	 */
	public void fillContext(){
		String pathA = "";
		String pathB = "";
		
		if(this.currentRun > 1){
			pathA = "." + File.separator + "run-" + (this.currentRun - 1) + ".html";
			this.context.put("prevPage", pathA);
		}else{
			pathA = "." + File.separator + "index.html";
		}
		
		if(this.currentRun != this.totalRuns){
			pathB = "." + File.separator + "run-" + (this.currentRun + 1) + ".html";
		}else{
			pathB = "." + File.separator + "index.html";
		}
		
		this.context.put("nextPage", pathB);
		this.context.put("prevPage", pathA);
		this.context.put("Wqvalue", String.format("%.4f", scr.getAverageQueueTime(5)));
		this.context.put("Lambdavalue", String.format("%.4f", scr.getLambda(0)));
		this.context.put("Muvalue", String.format("%.4f", scr.getMu(5)));
		this.context.put("Wsvalue", String.format("%.4f", scr.getAverageServiceTime(5)));
		this.context.put("Wvalue", String.format("%.4f", scr.getAverageTime(5)));
		this.context.put("Lvalue", String.format("%.4f", scr.getL(5)));
		this.context.put("Lqvalue", String.format("%.4f", scr.getLq(5)));
		this.context.put("Lsvalue", String.format("%.4f", scr.getLs(5)));
		this.context.put("rhoV", String.format("%.4f", scr.getRho(5)));
		this.context.put("numeroDeCorrida", this.currentRun);
		this.context.put("corridasTotales", this.totalRuns);
	}
	
	/**
	 * Fills an HTML file with the simulation specific statistics for each module in the simulation.
	 */
	public void fillModules(){
		this.context.put("W0", String.format("%.3f", scr.getAverageTime(0)));
		this.context.put("DLLAavg0", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.DDL, 0)));
		this.context.put("UpdateAvg0", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.UPDATE, 0)));
		this.context.put("JOINavg0", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.JOIN, 0)));
		this.context.put("Selectavg0", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.SELECT, 0)));
		this.context.put("oscio0", String.format("%.3f", scr.getIdleTime(0)));
		this.context.put("W1", String.format("%.3f", scr.getAverageTime(1)));
		this.context.put("DLLAavg1", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.DDL, 1)));
		this.context.put("UpdateAvg1", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.UPDATE, 1)));
		this.context.put("JOINavg1", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.JOIN, 1)));
		this.context.put("Selectavg1", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.SELECT, 1)));
		this.context.put("oscio1", String.format("%.3f", scr.getIdleTime(1)));
		this.context.put("W2", String.format("%.3f", scr.getAverageTime(2)));
		this.context.put("DLLAavg2", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.DDL, 2)));
		this.context.put("UpdateAvg2", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.UPDATE, 2)));
		this.context.put("JOINavg2", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.JOIN, 2)));
		this.context.put("Selectavg2", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.SELECT, 2)));
		this.context.put("oscio2", String.format("%.3f", scr.getIdleTime(2)));
		this.context.put("W3", String.format("%.3f", scr.getAverageTime(3)));
		this.context.put("DLLAavg3", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.DDL, 3)));
		this.context.put("UpdateAvg3", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.UPDATE, 3)));
		this.context.put("JOINavg3", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.JOIN, 3)));
		this.context.put("Selectavg3", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.SELECT, 3)));
		this.context.put("oscio3", String.format("%.3f", scr.getIdleTime(3)));
		this.context.put("W4", String.format("%.3f", scr.getAverageTime(4)));
		this.context.put("DLLAavg4", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.DDL, 4)));
		this.context.put("UpdateAvg4", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.UPDATE, 4)));
		this.context.put("JOINavg4", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.JOIN, 4)));
		this.context.put("Selectavg4", String.format("%.3f", scr.getQueryTimeByTypeInModule(QueryType.SELECT, 4)));
		this.context.put("oscio4", String.format("%.3f", scr.getIdleTime(4)));
		String no= "RUN "+this.currentRun; 
		this.context.put("title", no);
	}
	
	/**
	 * Generates the HTML files with all the statistics introduced before.
	 */
	public void generateHTML(){
		template.merge(this.context, this.writer);
		
		try {
			PrintWriter writeNewHtml = new PrintWriter(path, "UTF-8");
			writeNewHtml.write(writer.toString());
			writeNewHtml.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(null, "Failed to create the HTML file with the statistics data.\n" +
		"The path is not found or the text encoding is invalid.", "HTML generation error", JOptionPane.ERROR_MESSAGE);
		}
		try {
			writer.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot create the HTML file.\nFailed to close the data buffer to create the file",
					"Closing buffer error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
