package model.drone.runtime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import controller.flightZone.FlightZoneException;
import controller.helper.Coordinates;
import controller.movement.SoloFlightDirector;
import controller.movement.SafetyManager;
import controller.movement.iFlightDirector;
import view.DroneImage;

/**
 * Creates a Managed drone.
 * @author Jane Cleland-Huang
 * @version 0.01
 */
public class ManagedDrone extends Observable implements Runnable, Observer{
	
	iDrone drone;  // Controls primitive flight commands for drone
	Coordinates currentPosition;
	String droneName;
	DroneImage droneImage;
	SafetyManager safetyMgr;
	Coordinates basePosition; // In current version drones always return to base at the end of their flights.
	DroneFlightModeState droneState;
	DroneSafetyModeState droneSafetyState;
	boolean missionCompleted = false;
	Coordinates targetCoordinates = null;
	
	Thread thread;	
	int normalSleep= 1;
	int currentSleep = normalSleep;
	 
	// Flight plan
	iFlightDirector flightDirector = null; // Each drone can be assigned a single flight plan.
	int targetAltitude=0;

	/**
	 * Constructs drone
	 * @param drnName 
	 */
	public ManagedDrone(iDrone drone, String drnName) {
		this.drone = drone ;// specify 
		droneState = new DroneFlightModeState();
		droneSafetyState = new DroneSafetyModeState();
		currentPosition = null; //new Coordinates(lat,lon,alt); // NEED TO SET CURRENT POSITION.
		droneName = drnName;
		this.flightDirector = new SoloFlightDirector(this); // Don't really want to create it here.
		thread = new Thread(this);
	}
		
	/**
	 * Sets drone coordinates
	 * @param lat
	 * @param lon
	 * @param alt
	 */
	public void setCoordinates(long lat, long lon, int alt) {
		drone.setCoordinates(lat, lon, alt); 		
	}
	
	
	/**
	 * Assigns a flight directive to the managed drone
	 * @param flightDirective
	 */
	public void assignFlight(iFlightDirector flightDirective){
		this.flightDirector = flightDirective;
		this.flightDirector.addWayPoint(getBaseCoordinates()); // Currently must always return home.
	}
	
	/**
	 * Removes an assigned flight
	 */
	public void unassignFlight(){
		flightDirector = null;  // DANGER.  NEEDS FIXING.  CANNOT UNASSIGN FLIGHT WITHOUT RETURNING TO BASE!!!
		System.out.println("UNassigned DRONE: " + getDroneName() );
	}

	/**
	 * @return latitude of current drone position
	 */
	public long getLatitude() {
		return drone.getLatitude(); //currentPosition.getLatitude();
	}

	/**
	 * 
	 * @return longitude of current drone position
	 */
	public long getLongitude() {
		return drone.getLongitude();
	}
	
	public void returnToHome(){
		flightDirector.returnHome(getBaseCoordinates());
		getFlightSafetyModeState().setSafetyModeToNormal();
		
		
	}

	/**
	 * 
	 * @return Altitude of current drone position
	 */
	public int getAltitude() {
		return drone.getAltitude();
	}
	
	/**
	 * 
	 * @param targetAltitude Sets target altitude for takeoff
	 */
	public void setTargetAltitude(int targetAltitude){
		this.targetAltitude = targetAltitude;
	}

	/**
	 * Controls takeoff of drone
	 * @throws FlightZoneException
	 */
	public void takeOff() throws FlightZoneException {
		missionCompleted = false;
		if (targetAltitude==0)
			throw new FlightZoneException("Target Altitude is 0");
		System.out.println("TAKING OFF DRONE: " + getDroneName());
		droneState.setModeToTakingOff();
		drone.takeOff(targetAltitude); 
		droneState.setModeToFlying();
	}

	/**
	 * Delegates flyto behavior to virtual or physical drone
	 * @param targetCoordinates
	 */
	public void flyTo(Coordinates targetCoordinates) {
		drone.flyTo(targetCoordinates);
	}

	/**
	 * Gets current coordinates from virtual or physical drone
	 * @return current coordinates
	 */
	public Coordinates getCoordinates() {
		return drone.getCoordinates();
	}

	/**
	 * Registers an image with the drone
	 * @param img
	 */
	public void registerImage(DroneImage img) {
		this.droneImage = img;		
	}

	
	/**
	 * Keeps the coordinates of the drone image for display synched with actual drone coordinates
	 * @param img
	 */
	public void updateImageLocation() throws FlightZoneException {
		droneImage.updateImageCoordinates();	
	}

	public void startThread() {
		thread.start();		
	}
	
	@Override
	public void run() {
		
		while (true){// && j < 500){
			
			// Drone has been temporarily halted.  Reset to normal mode once sleep is completed.
			try {
				Thread.sleep(currentSleep);
				currentSleep = normalSleep;  
				if(droneSafetyState.isSafetyModeHalted())
					droneSafetyState.setSafetyModeToNormal();
				} catch (InterruptedException e) {
					e.printStackTrace();
			}
					
			// Drone currently is assigned a flight directive.
			if (flightDirector!=null && droneState.isFlying()){
				
				targetCoordinates = flightDirector.flyToNextPoint();
				
				
				
				
				// Move the drone.  Returns FALSE if it cannot move because it has reached destination
				if (!drone.move(10)) 
					flightDirector.clearCurrentWayPoint();
			
				// Check for end of flight
				checkForEndOfFlight();
				
			
				// Check for takeoff conditions
				checkForTakeOff();
								
				// Set check voltage		
				drone.setVoltageCheckPoint();
			}											
		}
	}
	
	// Check for end of flight.  Land if conditions are satisfied
	private boolean checkForEndOfFlight() {	
		if (flightDirector!=null && flightDirector.readyToLand())
			return false;  // it should have returned here.
		if (droneState.isLanding())
			return false;
		if (droneState.isOnGround())
			return false;
		
		// Otherwise
		try {
		    land();
		} catch (FlightZoneException e) {
				System.out.println(getDroneName() + " is not able to land!!");
				e.printStackTrace();
		}		
		return true;
	}
	
	// Check for takeoff.  Takeoff if conditions are satisfied.
	private boolean checkForTakeOff() {

		if (flightDirector!=null && flightDirector.readyToTakeOff())
			return false;
		if (droneState.isTakingOff())
			return false;
		if (safetyMgr == null) // Sometimes caused at startup by race conditions
			return false;
		if (!safetyMgr.permittedToTakeOff(this))
			return false;
		
		System.out.println("Passed takeoff test");
		// Otherwise
		try {
			takeOff();
		} catch (FlightZoneException e) {
			System.out.println(getDroneName() + " is not able to takeoff!!");
			e.printStackTrace();
		}
		return true;
	}
	

	/**
	 * 
	 * @return unique drone ID
	 */
	public String getDroneName() {
		return droneName;
	}

	/**
	 * 
	 * @return target coordinates
	 */
	public Coordinates getTargetCoordinates() {
		return targetCoordinates;
	}
		
	/**
	 * 
	 * @return current flight directive assigned to the managed drone
	 */
	public iFlightDirector getFlightDirective(){
		return flightDirector;
	}
	
	
	/**
	 * Land the drone.
	 * Delegate land functions to virtual or physical drone
	 * @throws FlightZoneException
	 */
	public void land() throws FlightZoneException {
		if (!droneState.isLanding() || !droneState.isOnGround()){
			droneState.setModeToLanding();
			drone.land();
			droneState.setModeToOnGround();
     		unassignFlight();
		}
	}
	
	/**
	 * Temporarily Halt
	 * @param halt time in seconds
	 */
	public void haltInPlace(int seconds) {
		currentSleep = seconds*1000;
		droneSafetyState.setSafetyModeToHalted();
	}
	
	/**
	 * Get unique base coordinates for the drone
	 * @return base coordinates
	 */
	public Coordinates getBaseCoordinates() {
		return basePosition;
	}

	/**
	 * Set base coordinates for the drone
	 * @param basePosition
	 */
	public void setBaseCoordinates(Coordinates basePosition) {
		this.basePosition = new Coordinates(basePosition.getLatitude(), basePosition.getLongitude(), basePosition.getAltitude());
		currentPosition = new Coordinates(basePosition.getLatitude(), basePosition.getLongitude(), basePosition.getAltitude());		
	}

	/**
	 * 
	 * @return image associated with drone
	 */
	public DroneImage getDroneImage() {
		return droneImage;
	}

	/**
	 * 
	 * return current flight mode state
	 */
	public DroneFlightModeState getFlightModeState() {
		return droneState;
	}

	/**
	 * 
	 * @return current safety mode state
	 */
	public DroneSafetyModeState getFlightSafetyModeState() {
		return droneSafetyState;
	}

	/**
	 * Set mission completed status
	 */
	public void setMissionCompleted() {
		missionCompleted = true;		
	}

	/**
	 * 
	 * @return mission status
	 */
	public boolean missionInProgress() {
		return !missionCompleted;
	}
	
	/**
	 * Retrieve battery status from drone
	 * @return remaining voltage
	 */
	public double getBatteryStatus(){
		return drone.getBatteryStatus();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	
}

