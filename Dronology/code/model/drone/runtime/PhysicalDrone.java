package model.drone.runtime;

import controller.flightZone.FlightZoneException;
import controller.helper.Coordinates;
import controller.movement.SoloFlightDirector;
import controller.movement.SafetyManager;
import view.DroneImage;

/**
 * Placeholder for physical drone code.
 * @author Jane
 *
 */
public class PhysicalDrone implements iDrone, Runnable {
	
	public PhysicalDrone(String drnName) {
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getLatitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLongitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAltitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void flyTo(Coordinates targetCoordinates) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Coordinates getCoordinates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDroneName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void land() throws FlightZoneException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void takeOff(int altitude) throws FlightZoneException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCoordinates(long lat, long lon, int alt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getBatteryStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean move(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setVoltageCheckPoint() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDestinationReached(int i) {
		// TODO Auto-generated method stub
		return false;
	}

}
