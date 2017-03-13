package view;

import java.awt.Point;
import java.util.Random;

import controller.helper.DecimalDegreesToXYConverter;
import controller.flightZone.FlightZoneException;
import controller.helper.Coordinates;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.drone.runtime.ManagedDrone;
import model.drone.runtime.iDrone;

/**
 * Manages the current image for one drone object.
 * @author Jane Cleland-Huang
 *
 */
public class DroneImage {
	ImageView droneImageView;
	ImageView droneOnGroundImageView;
	FlightZoneView fzView;
	DecimalDegreesToXYConverter coordTrans;	
	
	ManagedDrone drone;
	
	/**
	 * Constructor for Drone Image
	 * @param drone2 that the image will represent
	 * @param fzView View for displaying drone image
	 * @throws FlightZoneException 
	 */
	public DroneImage(ManagedDrone drone2, FlightZoneView fzView) throws FlightZoneException{
		this.drone = drone2;
		this.fzView = fzView;
		coordTrans = DecimalDegreesToXYConverter.getInstance();
		Point pnt = coordTrans.getPoint(drone2.getLatitude(), drone2.getLongitude());
		
		Image droneImage = new Image("images\\drone.png",50,50,true,true);	
		droneImageView = new ImageView(droneImage);
		droneImageView.setX(pnt.x);
		droneImageView.setY(pnt.y);	
	}
	
	/**
	 * 
	 * @return ImageView (needed by JavaFX)
	 */
	public ImageView getDroneImage(){
		return droneImageView;
	}
	
	/**
	 * 
	 * @return alternate view for Drone on Ground
	 * Not yet fully implemented.
	 */
	public ImageView getDroneOnGroundImage(){
		return droneOnGroundImageView;
	}
	
	/**
	 *  Updates image coordinates from the drone's current position.
	 * @throws FlightZoneException 
	 */
	public void updateImageCoordinates() throws FlightZoneException{
		
		Point pnt = coordTrans.getPoint(drone.getLatitude(), drone.getLongitude());
		
		droneImageView.setX(pnt.x);
		droneImageView.setY(pnt.y);
	}
	
	//Temporary
	public ManagedDrone getDroneFromImage(){
		return drone;
	}
}
