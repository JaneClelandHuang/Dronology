package model.flights.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import controller.flightZone.FlightZoneException;
import controller.helper.Coordinates;
import model.drone.runtime.ManagedDrone;
import model.drone.runtime.iDrone;

/**
 * Stores flight information including its waypoints and current status.
 * @author Jane Cleland-Huang
 * @version 0.1
 *
 */
public class FlightPlan {
	static int flightNumber = 0; 
	String flightID;
	
	ArrayList<Coordinates> wayPoints;
	Coordinates startLocation;
	Coordinates endLocation;
	
	private enum Status {Planned, Flying, Completed };
	Status status;
	ManagedDrone drone = null;
	
	public Date startTime;
	public Date endTime;
	DateFormat df = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 * Loads flight information and assigns a flight ID.  ID's are generated automatically and are
	 * unique in each run of the simulation.
	 * @param start Starting coordinates
	 * @param wayPoints
	 */
	public FlightPlan(Coordinates start, ArrayList<Coordinates> wayPoints ){
		this.wayPoints = wayPoints;
		this.startLocation = start;
		if (wayPoints.size() > 0)
			this.endLocation = wayPoints.get(wayPoints.size()-1);
		else
			endLocation = startLocation;
		this.flightID = "DF-" + Integer.toString(++flightNumber);	
		status = Status.Planned;		
	}
	
	/**
	 * 
	 * @return flight ID
	 */
	public String getFlightID(){
		return flightID;
	}
	
	/**
	 * 
	 * @return Starting Coordinates
	 */
	public Coordinates getStartLocation(){
		return startLocation;
	}
	
	/**
	 * 
	 * @return Ending Coordinates
	 */
	public Coordinates getEndLocation(){
		return endLocation;
	}
	
	/**
	 * Returns the drone assigned to the flight plan.
	 * Will return null if no drone is yet assigned.
	 * @return iDrone
	 */
	public ManagedDrone getAssignedDrone(){
		return drone;
	}
	
	public void clearAssignedDrone(){
		drone = null;
	}
	
	/**
	 * 
	 * @param iDrone  
	 * @return true if drone is currently flying, false otherwise.
	 * @throws FlightZoneException
	 */
	public boolean setStatusToFlying(ManagedDrone drone) throws FlightZoneException{
		if (status == Status.Planned){
		    status = Status.Flying;
			startTime = new Date();
		    this.drone = drone;
		    return true;
		}
		else throw new FlightZoneException("Only currently planned flights can have their status changed to flying");
	}
	
	/**
	 * Sets flightplan status to completed when called.	
	 * @return
	 * @throws FlightZoneException
	 */
	public boolean setStatusToCompleted() throws FlightZoneException{
		if (status == Status.Flying){
			status = Status.Completed;
			endTime = new Date();
			return true; // success
		}
		else throw new FlightZoneException("Only currently flying flights can have their status changed to completed");
	}
	
	/**
	 * Returns current flightplan status (Planned, Flying, Completed)
	 * @return
	 */
	public String getStatus(){
		switch(status) {
		   case Planned :
			   return "Planned";
		   case Flying:
			   return "Flying";
		   case Completed:
			   return "Completed";
		   default:
			   return " "; // never reached
		}		
	}
	
	public String toString(){
		return flightID + "\n" + getStartLocation() + " - " + getEndLocation() + "\n" + getStatus();
	}
	
	/**
	 * Returns way points
	 * @return ArrayList<Coordinates>
	 */
	public ArrayList<Coordinates> getWayPoints(){
		return wayPoints;
	}
	
	/** 
	 * Returns total number of waypoints in flight plan
	 * @return int
	 */
	public int getNumberWayPoints(){
		return wayPoints.size();
	}

	/**
	 * Returns start time of flight.
	 * @return date object
	 */
	public Date getStartTime() {
		return startTime;
	}
	
	/** 
	 * REturns end time of flight.
	 * @return date object
	 */
	public Date getEndTime() {
		return endTime;
	}
}
