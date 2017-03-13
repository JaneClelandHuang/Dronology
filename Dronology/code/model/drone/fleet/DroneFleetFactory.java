package model.drone.fleet;
import java.util.ArrayList;

import model.bases.BaseManager;
import model.bases.DroneBase;
import model.drone.runtime.ManagedDrone;
import model.drone.runtime.iDrone;

/**
 * Abstract factory class for drone fleet factory
 * @author Jane
 *
 */
public abstract class DroneFleetFactory {
	private final ArrayList<ManagedDrone> drones = new ArrayList<ManagedDrone>();
	int uniqDroneID = 0;
	BaseManager baseManager;
	
	/**
	 * Creates a fleet of size fleetSize.  Defers creation of the drone to a subclass for creating a fleet of
	 * virtual drones or physical drones.
	 * @param fleetSize
	 */
	public DroneFleetFactory(int fleetSize, BaseManager baseManager){
		for(int j=0;j<fleetSize;j++){
			ManagedDrone drone = makeDroneAtUniqueBase(baseManager);
			drones.add(drone);
			drone.startThread();
			this.baseManager = baseManager;
		}
	}
	
	protected String createDroneID(int ID){
		return "DRN" + Integer.toString(ID);		
	}
	
	/**
	 * Returns list of drones
	 * @return array list of iDrones
	 */
	public ArrayList<ManagedDrone> getDrones(){
		return drones;
	}
	
	protected void readBases(){
		
	}
	
	abstract protected ManagedDrone makeDroneAtUniqueBase(BaseManager baseManager);
	
}

