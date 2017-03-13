package model.drone.runtime;
import controller.flightZone.FlightZoneException;
import controller.helper.Coordinates;
import controller.virtualDroneSimulator.DroneVoltageSimulator;
import controller.virtualDroneSimulator.FlightSimulator;

/**
 * Creates a virtual drone.
 * iDrone interface needs refactoring badly!!!!
 * @author Jane Cleland-Huang
 * @version 0.01
 */
public class VirtualDrone implements iDrone{

	// Drone characteristics
	Coordinates currentPosition;
	String droneName;
	
	// Virtual drone requires simulators
	DroneVoltageSimulator voltageSimulator;
	FlightSimulator sim; 

	/**
	 * Constructs drone without specifying its current position.  This will be used by the physical drone (later)
	 * where positioning status will be acquired from the drone.
	 * @param drnName 
	 */
	public VirtualDrone(String drnName) {
		voltageSimulator = new DroneVoltageSimulator();
		currentPosition = null; 
		sim = new FlightSimulator(this);
		droneName = drnName;
	}
	
	@Override
	public void setCoordinates(long lat, long lon, int alt) {  // For physical drone this must be set by reading position
		currentPosition = new Coordinates(lat,lon,alt);
	}
	
	@Override
	public long getLatitude() {
		return currentPosition.getLatitude();
	}

	@Override
	public long getLongitude() {
		return currentPosition.getLongitude();
	}

	@Override
	public int getAltitude() {
		return currentPosition.getAltitude();
	}

	@Override
	public void takeOff(int targetAltitude) throws FlightZoneException {
		voltageSimulator.startBatteryDrain();
		try {			
			Thread.sleep(targetAltitude*100);  // Simulates attaining height.  Later move to simulator.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void flyTo(Coordinates targetCoordinates) {
		//targetPosition = targetCoordinates;
		sim.setFlightPath(currentPosition, targetCoordinates);
	}

	@Override
	public Coordinates getCoordinates() {
		return currentPosition;
	}


	public String getDroneName() {
		return droneName;
	}

	@Override
	public void land() throws FlightZoneException {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		voltageSimulator.checkPoint();
		voltageSimulator.stopBatteryDrain();		
	}
	
	@Override
	public double getBatteryStatus(){
		return voltageSimulator.getVoltage();
	}

	@Override
	public boolean move(int i) {  // ALSO NEEDS THINKING ABOUT FOR non-VIRTUAL
	//	System.out.println("Trying to move: " + droneName);
		return sim.move(10);
		//return true;// remove boolean return type!!
	}

	@Override
	public void setVoltageCheckPoint() {
		voltageSimulator.checkPoint();
		
	}

	@Override
	public boolean isDestinationReached(int distanceMovedPerTimeStep) {
		if (sim.isDestinationReached(distanceMovedPerTimeStep))
			return true;
		else
			return false;
	}

	
}
