package controller.movement;

import java.util.ArrayList;
import controller.helper.PointDelta;
import model.drone.runtime.ManagedDrone;
import model.drone.runtime.iDrone;


/**
 * Safety manager is responsible for monitoring drone positions to ensure minimum safety distance is not violated
 * @author jane
 *
 */
	public class SafetyManager{
	ArrayList<ManagedDrone> drones = new ArrayList<ManagedDrone>();
	Long safetyZone;  // Set arbitrarily for now.
	
	/**
	 * Construct the safety manager.  SafetyZone size is hard coded at 10000 degree points.
	 */
	public SafetyManager(){
		drones = new ArrayList<ManagedDrone>();
		safetyZone = (long) 10000; 
	}
	
	/** 
	 * Attach a drone to the safety manager.  Only attached drones are managed.
	 * @param drone
	 */
	public void attachDrone(ManagedDrone drone){
		drones.add(drone);
	}
	
	/**
	 * Detach a drone from the safety manager.  
	 * @param drone
	 */
	public void detachDrone(ManagedDrone drone){
		if(drones.contains(drone))
			drones.remove(drone);
	}
	
	/**
	 * Computes the distance between two drones
	 * @return distance remaining in degree points.
	 */
	public long getDistance(ManagedDrone D1, ManagedDrone D2){
		long longDelta = Math.abs(D1.getLongitude() - D2.getLongitude());
		long latDelta = Math.abs(D1.getLatitude() - D2.getLatitude());
		return (long) Math.sqrt((Math.pow(longDelta, 2)) + (Math.pow(latDelta, 2)));
	}
	
	/** 
	 * Checks if the two drones will get any closer in the future.  
	 * @param D1 Drone 1
	 * @param D2 Drone 2
	 * @return
	 */
	public boolean willGetCloser(ManagedDrone D1, ManagedDrone D2){
		if (D1 == null || D2 == null)
			System.out.println("NULL HERE");
		long longDelta = Math.abs(D1.getLongitude() - D2.getLongitude());
		long latDelta = Math.abs(D1.getLatitude() - D2.getLatitude());
		long currentDistance = (long) Math.sqrt((Math.pow(longDelta, 2)) + (Math.pow(latDelta, 2)));
		// There are times when D1 or D2 coordinates are in the process of being reset by their individual drone threads
		// This would cause a null pointer exception.  Instead of adding more complex synchronization code we skip analysis under such circumstances.
		if(!(D1.getTargetCoordinates() == null ||D2.getTargetCoordinates() == null)){
			long longNextDelta = Math.abs((D1.getTargetCoordinates()).getLongitude() - (D2.getTargetCoordinates()).getLongitude());
			long latNextDelta = Math.abs((D1.getTargetCoordinates()).getLatitude() - (D2.getTargetCoordinates()).getLatitude());
			long nextDistance = (long) Math.sqrt((Math.pow(longNextDelta, 2)) + (Math.pow(latNextDelta, 2)));
			if (nextDistance >= currentDistance)
				return false;
			else
				return true;
		} else
			return false; // Skipped the check because coordinates were being reset.
	}
	
	/**
	 * Checks if a drone has permission to take off. A drone may NOT take off if any other drone currently attached to the
	 * safety manager is in the vicinity.
	 * @param managedDrone The drone which is requesting permission to take off.  This is checked against the complete list of drones.
	 * @return
	 */
	public boolean permittedToTakeOff(ManagedDrone managedDrone){
		for(ManagedDrone drone2: drones){
			if (!managedDrone.equals(drone2) && drone2.getFlightModeState().isFlying())
				if (getDistance(managedDrone,drone2) < safetyZone*2) // We require extra distance prior to takeoff
					return false;		
		}
		return true;
	}				
	
	/**
	 * Performs pairwise checks of safety violations between all drones currently attached to the safety manager.
	 */
	public void checkForViolations(){		
		for(ManagedDrone drone: drones){
			for(ManagedDrone drone2: drones) {
				if (!drone.equals(drone2) && drone.getFlightModeState().isFlying() && drone2.getFlightModeState().isFlying()){//!drone.isUnderSafetyDirectives() && !drone2.isUnderSafetyDirectives()){
					if (getDistance(drone,drone2) < safetyZone){
						//System.out.println("IN SAFETY ZONE");
						//if(drone.getTargetCoordinates() == null)
						//	System.out.println("D1 target coords are null");
							double angle1 = PointDelta.computeAngle(drone.getCoordinates(),drone.getTargetCoordinates());
							double angle2 = PointDelta.computeAngle(drone2.getCoordinates(),drone2.getTargetCoordinates());
						//	System.out.println("Angle: " + Math.abs(angle1-angle2));
							new Roundabout(drone, drone2);
						}
					}				
			}
		
		}
	}
}

