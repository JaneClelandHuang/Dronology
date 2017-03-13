package model.drone.fleet;

import java.util.ArrayList;

import controller.flightZone.FlightZoneException;
import model.bases.BaseManager;
import model.drone.runtime.ManagedDrone;
import view.FlightZoneView;

/**
 * Holds a fleet of virtual or physical drones.
 * @author Jane
 *
 */
public class DroneFleet {
	ArrayList<ManagedDrone> availableDrones;
	ArrayList<ManagedDrone> busyDrones;
	final static int fleetSize = 5;
	
	/**
	 * Specifies whether virtual or physical drones will be created according to the previously specified
	 * runtime drone type.  (See RuntimeDroneTypes.java)
	 * @param fzView 
	 */
	public DroneFleet(BaseManager baseMgr, FlightZoneView fzView){
		if (RuntimeDroneTypes.getInstance().isSimulation())
			availableDrones = (new VirtualDroneFleetFactory(fleetSize, baseMgr).getDrones());
		else
			availableDrones = (new PhysicalDroneFleetFactory(fleetSize, baseMgr).getDrones());
		
		for(ManagedDrone drone: availableDrones){
			try {
				fzView.createDroneImage(drone);
			} catch (FlightZoneException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		busyDrones = new ArrayList<ManagedDrone>();
	}
	
	/**
	 * Checks for an available drone from the fleet.
	 * @return true if drone is available, false if it is not.
	 */
	public boolean hasAvailableDrone(){
		if (availableDrones.size() > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * Returns the next available drone.  Currently uses FIFO to recycle drones.
	 * @return
	 */
	public ManagedDrone getAvailableDrone(){
		if (!availableDrones.isEmpty()){
			ManagedDrone drone = availableDrones.get(0);
			availableDrones.remove(drone);
			busyDrones.add(drone);
			return drone;
		}	
		else
			return null;
	}
	
	public void setupPlatoon(){
		System.out.println("Ready to setup Platoon");
	}
	
	/**
	 * When a drone completes a mission, returns it to the pool of available drones.
	 * @param drone
	 */
	public void returnDroneToAvailablePool(ManagedDrone drone){
		if (busyDrones.contains(drone)){
			busyDrones.remove(drone);
			availableDrones.add(drone);
		}
	}
}

