package controller.movement;

import java.awt.Point;
import java.util.ArrayList;
import controller.helper.Coordinates;
import model.drone.runtime.ManagedDrone;
import model.drone.runtime.iDrone;


/**
 * Create a new roundabout when two drones violate safety zone.
 * @author Jane Cleland-Huang
 * @version 
 */
public class Roundabout {
	
	Coordinates drone1Coords, drone2Coords;
	Coordinates hub; 
	long radius;
	Coordinates[] circumferenceCoordinates;
	
	/**
	 * Constructs a roundabout to prevent collision of two drones. Creates a circle with 32 points on it.
	 * Drones travel clockwise and enter and exit at points only.
	 * @param drone Drone 1
	 * @param drone2 Drone 2
	 */
	public Roundabout(ManagedDrone drone, ManagedDrone drone2){ //Coordinates D1, Coordinates D2){
		drone1Coords = drone.getCoordinates();
		drone2Coords = drone2.getCoordinates();
		//System.out.println(drone1Coords.toString() + " " + drone2Coords.toString());
		hub = new Coordinates(0,0,0);
		radius = 2000;
		circumferenceCoordinates = new Coordinates[32];
		constructRoundabout();
		Point pnt = new Point();
		pnt.setLocation(hub.getLatitude(),hub.getLongitude());
		computeCirclePoints(32,radius,pnt);
		computeDroneEntryExitPoints(drone);
		computeDroneEntryExitPoints(drone2);
		System.out.println("Roundabout built");
	}
	
	/**
	 * Computes entry and exit points for the drone and sends the drone a waypoint list.
	 * @param drone
	 */
	private void computeDroneEntryExitPoints(ManagedDrone drone){
		
		Coordinates currentPos = drone.getCoordinates();
		Coordinates tgt = drone.getTargetCoordinates();
		
		// Find the entry point.
		long closestDistance = 1000000;
		int closestPoint = 0;
		for (int j = 0; j < circumferenceCoordinates.length; j++){
			long longDelta = Math.abs(currentPos.getLongitude() - (long)circumferenceCoordinates[j].getLongitude());
			long latDelta = Math.abs(currentPos.getLatitude() - (long)circumferenceCoordinates[j].getLatitude());
			if ((long) Math.sqrt((Math.pow(longDelta, 2)) + (Math.pow(latDelta, 2))) < closestDistance){
				closestDistance = (long) Math.sqrt((Math.pow(longDelta, 2)) + (Math.pow(latDelta, 2)));
				closestPoint = j;
			}
		}
		
		//Find the exit point
		long closestTargetDistance = (long)1000000000; // Target could be far away!
		int closestToTargetPoint = 0;
		for (int j = 0; j < circumferenceCoordinates.length; j++){
			long longDelta = Math.abs(tgt.getLongitude() - (long)circumferenceCoordinates[j].getLongitude());
			long latDelta = Math.abs(tgt.getLatitude() - (long)circumferenceCoordinates[j].getLatitude());
			if ((long) Math.sqrt((Math.pow(longDelta, 2)) + (Math.pow(latDelta, 2))) < closestTargetDistance){
				closestTargetDistance = (long) Math.sqrt((Math.pow(longDelta, 2)) + (Math.pow(latDelta, 2)));
				closestToTargetPoint = j;
			}
		}
		
		ArrayList<Coordinates> roundaboutDirections = new ArrayList<Coordinates>();
		
		// If entry point is higher than exit points.  Start by adding points from end of array.
		if (closestPoint > closestToTargetPoint){			
			for(int j = closestPoint; j < circumferenceCoordinates.length; j++){
				roundaboutDirections.add(circumferenceCoordinates[j]);	
			}
			for(int j = 0; j <= closestToTargetPoint; j++) {
				roundaboutDirections.add(circumferenceCoordinates[j]);	
			}
		} else {
			for(int j = closestPoint; j <= closestToTargetPoint; j++){
				roundaboutDirections.add(circumferenceCoordinates[j]);
			}
		}
		
		roundaboutDirections.add(tgt);
		
		// To do send waypoints to drone!
		if (drone.getFlightDirective()!=null)
			(drone.getFlightDirective()).setRoundabout(roundaboutDirections);
	}
	
	// Computes evenly distributed points on the circle.
	private void computeCirclePoints(int points, double radius, Point center)
	{
	    double slice = 2 * Math.PI / points;
	    for (int i = 0; i < points; i++)
	    {
	        double angle = slice * i;
	        int newX = (int)(hub.getLongitude() + radius * Math.cos(angle));
	        int newY = (int)(hub.getLatitude() + radius * Math.sin(angle));
	        Point p = new Point(newX, newY);
	        circumferenceCoordinates[i] = new Coordinates(0,0,0);
	        circumferenceCoordinates[i].setLatitude(p.y);
	        circumferenceCoordinates[i].setLongitude(p.x);
	        circumferenceCoordinates[i].setAltitude(10);
	    }
	}
	
	
	private void constructRoundabout(){
		long longDelta = Math.abs(drone1Coords.getLongitude() - drone2Coords.getLongitude());
		long latDelta = Math.abs(drone1Coords.getLatitude() - drone2Coords.getLatitude());

		if(drone1Coords.getLongitude() < drone2Coords.getLongitude())
			hub.setLongitude(drone1Coords.getLongitude()+ (longDelta/2));
		else
			hub.setLongitude(drone2Coords.getLongitude() + (longDelta/2));
		
		if(drone1Coords.getLatitude() < drone2Coords.getLatitude())
			hub.setLatitude(drone1Coords.getLatitude()+ (latDelta/2));
		else
			hub.setLatitude(drone2Coords.getLatitude() + (latDelta/2));
		
		hub.setAltitude(0); // We ignore altitude for now.
		radius = (long)(1* ((double) Math.sqrt((Math.pow(longDelta, 2)) + (Math.pow(latDelta, 2))))/2);
	}
	
	
}
