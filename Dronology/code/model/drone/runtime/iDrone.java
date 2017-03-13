package model.drone.runtime;

import controller.flightZone.FlightZoneException;
import controller.helper.Coordinates;
import controller.movement.SoloFlightDirector;
import controller.movement.SafetyManager;
import view.DroneImage;


/**
 * iDrone interface
 * @author Jane Cleland-Huang
 * @version 0.01
 *
 */
public interface iDrone {
	

	/**
	 * 
	 * @return latitude of drone
	 */
	public long getLatitude();

	/**
	 * 
	 * @return longitude of drone
	 */
	public long getLongitude();

	/**
	 * 
	 * @return altitude of drone
	 */
	public int getAltitude();
	
	/**
	 * Fly drone to target coordinates
	 * @param targetCoordinates
	 */
	public void flyTo(Coordinates targetCoordinates);

	/**
	 * 
	 * @return current coordinates
	 */
	public Coordinates getCoordinates();

	/**
	 * 
	 * @return unique name of drone
	 */
	public String getDroneName();

	/**
	 * Land the drone.  Update status.
	 * @throws FlightZoneException 
	 */
	void land() throws FlightZoneException;
	
	/**
	 * Takeoff.  Update status.
	 * @throws FlightZoneException 
	 */
	void takeOff(int altitude) throws FlightZoneException;

	/**
	 * Sets drones coordinates
	 * @param lat latitude
	 * @param lon Longitude
	 * @param alt Altitude
	 */
	public void setCoordinates(long lat, long lon, int alt);
	
	public double getBatteryStatus();

	public boolean move(int i);

	public void setVoltageCheckPoint();

	public boolean isDestinationReached(int i);

}
