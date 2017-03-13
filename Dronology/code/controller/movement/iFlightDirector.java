package controller.movement;

import java.util.ArrayList;

import controller.helper.Coordinates;

public interface iFlightDirector {
	
	public void returnHome(Coordinates home);
	
		
	public Coordinates flyToNextPoint();

	/**
	 * Set a series of waypoints.
	 * @param wayPoints
	 */
	void setWayPoints(ArrayList<Coordinates> wayPoints);

	/**
	 * Clear all waypoints
	 */
	void clearWayPoints();

	/**
	 * Creates a roundabout as a diversion
	 * @param roundAboutPoints
	 */
	void setRoundabout(ArrayList<Coordinates> roundAboutPoints);
	
	/** 
	 * Check if more waypoints exist
	 * @return boolean
	 */
	boolean hasMoreWayPoints();

	/**
	 * Specifies if flight is currently under a safety directive.
	 * @return
	 */
	boolean isUnderSafetyDirectives();

	/**
	 * Removes one wayPoint -- typically when a drone reaches a waypoint.
	 * @param wayPoint
	 */
	void clearCurrentWayPoint();

	/**
	 * Add a waypoint to the flight directive.
	 * @param wayPoint
	 */
	void addWayPoint(Coordinates wayPoint);

	void flyHome();

	boolean readyToLand();

	boolean readyToTakeOff();


}