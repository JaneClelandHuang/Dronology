package controller.helper;

import model.drone.runtime.iDrone;

/**
 * Computes distance between two drones.
 * Does not take into account earth's curviture because distances are expected to be small.
 * @author Jane
 *
 */
class Distance {	
	/**
	 * 
	 * @param D1 Drone-11
	 * @param D2 Drone-2
	 * @return distance computed using degrees latitude and longitude
	 */
	public static long getDistance(iDrone D1, iDrone D2){
		long longDelta = Math.abs(D1.getLongitude() - D2.getLongitude());
		long latDelta = Math.abs(D1.getLatitude() - D2.getLatitude());
		return (long) Math.sqrt((Math.pow(longDelta, 2)) + (Math.pow(latDelta, 2)));
	}
}
