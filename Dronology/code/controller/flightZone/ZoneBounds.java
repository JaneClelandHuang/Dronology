package controller.flightZone;

import controller.helper.Coordinates;

/**
 * Establishes geographical zone for the simulation
 * Singleton
 * @author Jane
 * @version 0.1
 */
public class ZoneBounds {
	long westLongitude = 0;
	long eastLongitude = 0;
	long northLatitude = 0;
	long southLatitude = 0;
	int maxAltitude = 0;
		
	private static ZoneBounds instance = null;
	protected ZoneBounds() {}
	
	/**
	 * Return an instance of ZoneBounds
	 * @return
	 */
	public static ZoneBounds getInstance() {
	   if(instance == null) {
	      instance = new ZoneBounds();
	   }
	   return instance;
	}
	
	/** 
	 * setup the boundary of the Zone based on top left and bottom right coordinates as well as maximum altitude
	 * @param northLat
	 * @param westLon
	 * @param southLat
	 * @param eastLon
	 * @param maxAlt
	 */
	public void setZoneBounds(long northLat, long westLon, long southLat, long eastLon, int maxAlt){
		westLongitude = westLon;
		eastLongitude = eastLon;
		northLatitude = northLat;
		southLatitude = southLat;
		maxAltitude = maxAlt;
	}
	
	/**
	 * Checks whether a coordinate is inside the zone
	 * @param coords
	 * @return
	 * @throws FlightZoneException
	 */
	public boolean inBounds(Coordinates coords) throws FlightZoneException{
		if (westLongitude!=0 || eastLongitude!=0){ // Assume two longitude values must not both be zero if bounds are set.
			// Check if in bounds
			return true; // or false
		} else
			throw new FlightZoneException("Coordinate check has failed - because you have not set zone bounds yet.");				
	}
	
	/**
	 * Get westerly longitude degree
	 * @return longitude degree
	 */
	public long getWestLongitude(){
		return westLongitude;
	}
	
	/**
	 * Get most easterly longitude degree
	 * @return
	 */
	public long getEastLongitude(){
		return eastLongitude;
	}
	
	/**
	 * Get north most latitude degree
	 * @return
	 */
	public long getNorthLatitude(){
		return northLatitude;
	}
	
	/** 
	 * Get south most latitude degree
	 * @return southLatitude degree
	 */
	public long getSouthLatitude(){
		return southLatitude;
	}
	
	/**
	 * Return top left coordinates
	 * @return Coordinates representing the top left position
	 */
	public Coordinates getTopLeft(){
		return new Coordinates(northLatitude, westLongitude, maxAltitude);
	}
	
	/**
	 *  Return delta in degrees between leftmost and rightmost longitude
	 * @return distance in degrees of longitude (x axis)
	 */
	public long getXRange(){
		return Math.abs(eastLongitude-westLongitude);
	}
	
	/**
	 * Return delta in degrees between northmost and southmost latitude
	 * @return
	 */
	public long getYRange(){
		return Math.abs(northLatitude-southLatitude);
	}
	
}
