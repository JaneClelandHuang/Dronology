package controller.helper;
import java.awt.Point;

import controller.flightZone.FlightZoneException;
import controller.flightZone.ZoneBounds;

/**
 * Given the window coordinates for the flight simulation, and the area of the map that is to be covered by
 * the simulation, this class computes the scaling factor and transforms GPS coordinates into x,y coordinates.
 * @author Jane Cleland-Huang
 * @version 0.1
 * Modification: Changing to singleton as it is called in multiple places.
 */
public class DecimalDegreesToXYConverter {
	ZoneBounds zoneBounds;
	long xRange = 0;  // X coordinates in range of 0 to x
	long yRange = 0;  // Y coordinates in range of 0 to y
    double xScale = 0.0;  // The scale that transforms longitude to x coordinates
    double yScale = 0.0;  // The scale that transforms latitude to y coordinates
    long latitudeOffset=0;
    long longitudeOffset=0;
    int reservedLeftHandSpace = 0;
    
    long zoneXRange = 0l;
    long zoneYRange = 0l;
    
	private static DecimalDegreesToXYConverter instance = null;
	protected DecimalDegreesToXYConverter() {}
	
	/**
	 * Return an instance of DecimalDegreesToXYConverter
	 * @return
	 */
	public static DecimalDegreesToXYConverter getInstance() {
	   if(instance == null) {
	      instance = new DecimalDegreesToXYConverter();
	   }
	   return instance;
	}
		  
    /**
     * Setup
     * @param xSize  Width of window
     * @param ySize	 Height of window
     */
	public void setUp(long xSize, long ySize, int reservedLeftHandSpace){
		this.xRange = xSize;
		this.yRange = ySize;
		zoneBounds = ZoneBounds.getInstance();		
		zoneXRange = zoneBounds.getXRange();
		zoneYRange = zoneBounds.getYRange();
		xScale = (double)(xRange-reservedLeftHandSpace)/zoneXRange;
		yScale = xScale; //(double)yRange/zoneYRange; @TD: Don't want to change scales and disproportionately stretch the map.
		this.reservedLeftHandSpace = reservedLeftHandSpace;
	}
	
	private boolean passSetUpCheck() throws FlightZoneException{
		if (xRange == 0 || yRange == 0 )
			throw new FlightZoneException("Flight zone area has not been setup correctly. xRange = " + xRange + " and yRange = " + yRange + ". Both must be positive, non-zero values");
		else
			return true;
	}
	
	/**
	 * Converts an integer (X) to Longitude decimal degrees.  Uses the scale factor originally computed by ZoneBounds 
	 * according to the area displayed in the screen.
	 * @param X 
	 * @return X converted to decimal degrees
	 * @throws FlightZoneException 
	 */
	public long ConvertXCoordsToDecimalDegrees(int X) throws FlightZoneException{
		System.out.println("Start Convert X: ");
		System.out.println("West Long: " + ZoneBounds.getInstance().getWestLongitude());
		System.out.println("Eash Long: " + ZoneBounds.getInstance().getEastLongitude());
		
		System.out.println("XScale: " + xScale);
		System.out.println("X: " + X);
//		System.out.println("XScale: " + ZoneBounds.getInstance().);
		if (passSetUpCheck()){
			long delta = (long)(((double)X-reservedLeftHandSpace)/xScale);// + longitudeOffset  
			System.out.println("Delta: " + delta);
			System.out.println(zoneBounds.getWestLongitude()+delta);
			return zoneBounds.getWestLongitude() + delta;
		
		}
		else
			return 0L; // Unreachable
	}
	
	/**
	 * Converts an integer (Y) to Latitude decimal degrees.  Uses the scale factor originally computed by ZoneBounds 
	 * according to the area displayed in the screen.
	 * @param Y
	 * @return Y converted to decimal degrees
	 * @throws FlightZoneException 
	 */
	public long ConvertYCoordsToDecimalDegrees(int Y) throws FlightZoneException{
		System.out.println("Start Convert Y: ");
		System.out.println("North Latitude: " + ZoneBounds.getInstance().getNorthLatitude());
		System.out.println("SOuth lat: " + ZoneBounds.getInstance().getSouthLatitude());
		
		System.out.println("YScale: " + yScale);
		System.out.println("Y: " + Y);
		if (passSetUpCheck()){
			long delta = (long)((double)Y/yScale); //latitudeOffset + (long)((double)Y/yScale);
			System.out.println("Delta Y: " + delta);
			System.out.println(zoneBounds.getNorthLatitude()-delta);
			return zoneBounds.getNorthLatitude() - delta;
		}
		
		else
			return 0L; // unreachable
	}
	
	/**
	 * Returns a point representing X and Y screen coordinates given latitude and longitude and assuming
	 * the previously defined ZoneBounds and y and x scaling factors.
	 * @param latitude in degrees (with decimal point removed)
	 * @param longitude in degrees (with decimal point removed)
	 * @return a point representing X,Y coordinates 
	 * @throws FlightZoneException 
	 */
	public Point getPoint(long latitude, long longitude) throws FlightZoneException{
		if (passSetUpCheck()){
			Point point = new Point();
			latitudeOffset = Math.abs(latitude-zoneBounds.getNorthLatitude());
			longitudeOffset = Math.abs(longitude-zoneBounds.getWestLongitude());
			long xLocation = reservedLeftHandSpace + (long)(longitudeOffset*xScale);
			long yLocation = (long)(latitudeOffset*yScale);
			point.setLocation(xLocation,yLocation);
			return point;
		}
		else
			return null;
	}
}
