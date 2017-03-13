package view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.flights.data.FlightPlan;


/** 
 * Manages the flight display panel in the view
 * @author Jane
 * @version 0.01
 * 
 */
public class FlightZoneStatusPanel {
	
	int panelWidth;
	int panelHeight;
	GraphicsContext gc;
	int rowCtr;
	DateFormat df = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 * Create the flight display board
	 * @param gc Graphics Context acquired from Canvas
	 * @param panelWidth
	 * @param panelHeight
	 */
	public FlightZoneStatusPanel(GraphicsContext gc, int panelWidth, int panelHeight){
		this.panelWidth = panelWidth;
		this.gc = gc;
		this.panelHeight = panelHeight;
	}
	
	/**
	 * Displays top of flight status board
	 */
	public void displayTopOfPanel() {
		rowCtr = 20;
		gc.clearRect(0, 0, panelWidth, 800);
		drawLine(panelWidth,0,panelWidth, panelHeight);	
	}

	/**
	 * Displays current flights (including those awaiting takeoff)
	 * @param currentFlights
	 * @param awaitingFlights
	 */
	public void displayCurrentFlights(ArrayList<FlightPlan> currentFlights, ArrayList<FlightPlan> awaitingFlights) {
		writeText(12, true, "Current Flights", 5,rowCtr);  // Page Heading
		rowCtr +=15;
		Iterator<FlightPlan> itr = currentFlights.iterator();  // External iterator needed due to external updates
		FlightPlan flightPlan = null;
	    while(itr.hasNext()){
	    	try{
	    		flightPlan = itr.next();
	    		writeText(10, true, flightPlan.getFlightID(),5,rowCtr+=15);
	    		if(flightPlan.getAssignedDrone() != null && flightPlan.getAssignedDrone().getDroneName()!=null)
	    			writeText(10,false,"Assigned to Drone: " + (flightPlan.getAssignedDrone()).getDroneName(),5,rowCtr+=15);
					writeText(10,false,"Orgn: " + (flightPlan.getStartLocation()).getShortString(),5,rowCtr+=15);
					writeText(10,false,"Dest:" + (flightPlan.getEndLocation()).getShortString(),5,rowCtr+=15);
					writeText(10,false,"Remaining Waypoints: " + flightPlan.getNumberWayPoints(),5,rowCtr+=15);
					int volts = ((int) (flightPlan.getAssignedDrone().getBatteryStatus() * 100));
					double dVolts = ((double)volts)/100;
					writeText(10,false,"Voltage: " + dVolts,5,rowCtr+=15);
					if(flightPlan.getStartTime()!=null){
						writeText(10,false,"Started at: " + df.format(flightPlan.getStartTime()),5,rowCtr+=15);
						rowCtr +=10;
					}
	    	}
    		catch(Exception e)//Exception is generic
    		{
    		//Do nothing.  If the flightPlan got moved we just won't display it in this iteration.
    		}	
			}  
	    
	    // Display awaiting flights
	    itr = awaitingFlights.iterator();  // External iterator needed due to external updates		
		flightPlan = null;
	    while(itr.hasNext()){
	    	try{
	    		flightPlan = itr.next();
	    		writeText(10, true, flightPlan.getFlightID(),5,rowCtr+=15);
	    		if(flightPlan.getAssignedDrone() != null && flightPlan.getAssignedDrone().getDroneName()!=null)
	    			writeText(10,false,"Awaiting permission to take off",5,rowCtr+=15);
	    			writeText(10,false,"Assigned to Drone: " + (flightPlan.getAssignedDrone()).getDroneName(),5,rowCtr+=15);
					writeText(10,false,"Orgn: " + (flightPlan.getStartLocation()).getShortString(),5,rowCtr+=15);
					writeText(10,false,"Dest:" + (flightPlan.getEndLocation()).getShortString(),5,rowCtr+=15);
					writeText(10,false,"Remaining Waypoints: " + flightPlan.getNumberWayPoints(),5,rowCtr+=15);
					if(flightPlan.getStartTime()!=null){
						writeText(10,false,"Started at: " + df.format(flightPlan.getStartTime()),5,rowCtr+=15);
						rowCtr +=10;
					}
	    	}
    		catch(Exception e)//Exception is generic
    		{
    		//Do nothing.  If the flightPlan got moved we just won't display it in this iteration.
    		}	
			}  
		rowCtr+=10;
		drawLine(5, rowCtr, panelWidth-10 ,rowCtr);		
	}

	/**
	 * Display pending flights
	 * @param pendingFlights
	 */
	public void displayPendingFlights(ArrayList<FlightPlan> pendingFlights) {
		rowCtr +=20;
		writeText(12, true, "Pending Flights", 5,rowCtr);  // Page Heading
		rowCtr +=10;
		Iterator<FlightPlan> itr = pendingFlights.iterator();  // External iterator needed due to external updates
		FlightPlan flightPlan = null;
	    while(itr.hasNext()){
	    	try{
	    		flightPlan = itr.next();	
	    		writeText(10, true, flightPlan.getFlightID(),5,rowCtr+=15);
	    		writeText(10,false,"Orgn: " + (flightPlan.getStartLocation()).getShortString(),5,rowCtr+=15);
	    		writeText(10,false,"Dest:" + (flightPlan.getEndLocation()).getShortString(),5,rowCtr+=15);
	    		rowCtr +=10;
	    	}
    		catch(Exception e)//Exception is generic
    		{
    		//Do nothing.  If the flightPlan got moved we just won't display it in this iteration.
    		}
		}    
		rowCtr+=10;
		drawLine(5, rowCtr, panelWidth-10 ,rowCtr);		
	}

	/**
	 * Display completed flights
	 * @param completedFlights
	 */
	public void displayCompletedFlights(ArrayList<FlightPlan> completedFlights) {
		rowCtr +=20;
		writeText(12, true, "Completed Flights", 5,rowCtr);  // Page Heading
		rowCtr +=10;
		Iterator<FlightPlan> itr = completedFlights.iterator();  // External iterator needed due to external updates
		FlightPlan flightPlan = null;
	    while(itr.hasNext()){
	    	try{
	    		flightPlan = itr.next();	
	    		writeText(10, true, flightPlan.getFlightID(),5,rowCtr+=15);
	    		writeText(10,false,"Orgn: " + (flightPlan.getStartLocation()).getShortString(),5,rowCtr+=15);
	    		writeText(10,false,"Dest:" + (flightPlan.getEndLocation()).getShortString(),5,rowCtr+=15);
	    		if(flightPlan.getEndTime()!=null)
	    			writeText(10,false,"Completed at: " + df.format(flightPlan.getEndTime()),5,rowCtr+=15);
	    		rowCtr +=10;
	    	}   
	    	catch(Exception e)//Exception is generic
	    	{
	    		//Do nothing.  If the flightPlan got moved we just won't display it in this iteration.
	    	}
	    }
	}
		
	private void writeText(int fontSize, boolean bold, String text, int x, int y){
		 gc.setStroke(Color.DARKGRAY);
         if (bold)
	       	gc.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
	     else
	       	gc.setFont(Font.font("Arial", FontWeight.NORMAL, fontSize));	        	
         gc.fillText(text, x, y);
	}
	
	private void drawLine(int x1, int y1, int x2, int y2) {
       gc.setStroke(Color.DARKGRAY);
       gc.setLineWidth(1);
       gc.strokeLine(x1, y1, x2, y2);
   }
}






