package model.bases;

import java.awt.Point;

import controller.helper.DecimalDegreesToXYConverter;
import controller.flightZone.FlightZoneException;
import controller.helper.Coordinates;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.drone.runtime.ManagedDrone;
import model.drone.runtime.iDrone;

/**
 * Defines a drone base at which drones are allowed to takeoff and land
 * @author Jane
 *
 */
public class DroneBase {
	Coordinates basePosition;
	ImageView baseImage;
	String baseName;
	Image img;
	DecimalDegreesToXYConverter coordTransform;
	Circle circle;
	ManagedDrone drone = null;  // Each drone currently has its own landing base.
	
	/** 
	 * Constructor
	 * @param baseName Unique basename
	 * @param latitude of base
	 * @param longitude of base
	 * @param altitude of base
	 * @throws FlightZoneException 
	 */
	public DroneBase(String baseName, long lat, long lon, int alt, int radius) throws FlightZoneException{
		basePosition = new Coordinates(lat,lon,alt);
		coordTransform = DecimalDegreesToXYConverter.getInstance();
		this.baseName = baseName;	
		circle = new Circle();
		circle.setRadius(radius);
		Point point = coordTransform.getPoint(lat, lon);
		circle.setCenterX(point.getX()+radius*.6);
		circle.setCenterY(point.getY()+radius*.6);
		circle.setFill(Color.LIGHTYELLOW);
		circle.setStroke(Color.BLACK);
		circle.setStrokeWidth(1);
	}
	
	/**
	 * 
	 * @return if a drone has already been assigned to the base.
	 */
	public boolean hasDroneAssigned(){
		if (drone == null)
			return false;
		else
			return true;
	}
		
	/**
	 * Assign a drone to the base
	 * @param drone2
	 */
	public void assignDroneToBase(ManagedDrone drone2){
		this.drone = drone2;
	}
	
	/**
	 * @return base latitude
	 */
    public long getLatitude(){
    	return basePosition.getLatitude();
    }
    
    /**
     * @return base longitude
     */
    public long getLongitude(){
    	return basePosition.getLongitude();
    }
    
    
    /**
     * @return base altitude (currently not used)
     */
    public int getAltitude(){
    	return basePosition.getAltitude();
    }
    
    /**
     * @return baseCoordinates
     */
    public Coordinates getCoordinates(){
    	return basePosition;
    }
        
    //@TD move this over to center it.
    /**
     * Creates a circle representing the base
     * @return circle
     */
    public Circle getCircle(){
	  return circle;
    }
}
