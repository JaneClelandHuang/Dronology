package model.drone.fleet;

import model.bases.BaseManager;
import model.drone.runtime.ManagedDrone;
import model.drone.runtime.PhysicalDrone;
import model.drone.runtime.VirtualDrone;
import model.drone.runtime.iDrone;

/**
 * Creates a fleet of physical drones.
 * Not yet implemented.  
 * @author Jane
 *
 */
public class PhysicalDroneFleetFactory extends DroneFleetFactory{

	public PhysicalDroneFleetFactory(int fleetSize, BaseManager baseMgr) {
		super(fleetSize, baseMgr);
	}
	@Override
	protected ManagedDrone makeDroneAtUniqueBase(BaseManager baseManager){
		iDrone drone = new PhysicalDrone(createDroneID(uniqDroneID++));
		ManagedDrone managedDrone = new ManagedDrone(drone,createDroneID(uniqDroneID++));
		baseManager.assignDroneToBase(managedDrone); // Assigns drone.  Sets coordinates
		//drone.setBaseCoordinates(BaseCoordinates.getInstance().getNextBase());
		return managedDrone;
	}
}