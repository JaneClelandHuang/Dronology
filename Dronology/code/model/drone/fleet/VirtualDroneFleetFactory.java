package model.drone.fleet;

import controller.helper.Coordinates;
import controller.movement.SoloFlightDirector;
import model.bases.BaseManager;
import model.drone.runtime.ManagedDrone;
import model.drone.runtime.VirtualDrone;
import model.drone.runtime.iDrone;

public class VirtualDroneFleetFactory extends DroneFleetFactory{
	
	public VirtualDroneFleetFactory(int fleetSize, BaseManager baseMgr) {
		super(fleetSize, baseMgr);
	}
	
	@Override
	protected ManagedDrone makeDroneAtUniqueBase(BaseManager baseManager){
		iDrone drone = new VirtualDrone(createDroneID(uniqDroneID++));
		ManagedDrone managedDrone = new ManagedDrone(drone,createDroneID(uniqDroneID++));
		baseManager.assignDroneToBase(managedDrone); // Assigns drone.  Sets coordiantes.
		Coordinates currentPosition = managedDrone.getBaseCoordinates();
		drone.setCoordinates(currentPosition.getLatitude(), currentPosition.getLongitude(), currentPosition.getAltitude());
		//drone.setBaseCoordinates(BaseCoordinates.getInstance().getNextBase());
		return managedDrone;
	}
	
}

