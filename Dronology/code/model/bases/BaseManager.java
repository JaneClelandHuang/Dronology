package model.bases;

import java.util.ArrayList;
import java.util.HashMap;

import controller.flightZone.FlightZoneException;
import controller.helper.Coordinates;
import model.drone.runtime.ManagedDrone;
import model.drone.runtime.iDrone;


public class BaseManager {
	//GraphicsContext gc;
	int numberOfBases = 0;
	int baseID;
	int radius = 40;
	//ArrayList<DroneBase> droneBases;
	HashMap<String,DroneBase> baseMap;
	
	public BaseManager(int numberOfBases) throws FlightZoneException{
		this.numberOfBases = numberOfBases;
		baseMap = new HashMap<String,DroneBase>();
		baseID = 1;
		generateBases();
	}
	
	public boolean assignDroneToBase(ManagedDrone drone){
		for (DroneBase droneBase: baseMap.values()){
			if (!droneBase.hasDroneAssigned()){
				droneBase.assignDroneToBase(drone);
				drone.setBaseCoordinates(droneBase.getCoordinates());
				return true;
			}
		}
		return false; // No base found.
	}
	
	/**
	 * Generates all bases
	 * @throws FlightZoneException
	 */
	public void generateBases() throws FlightZoneException{
		//droneBases = new ArrayList<DroneBase>();  // Only creates them from scratch
		for (int j = 0; j < numberOfBases; j++){
			BaseCoordinates baseCoords = BaseCoordinates.getInstance();
			Coordinates basePosition = baseCoords.getNextBase();
			String baseName = "Base-" + new Integer(baseID++).toString();
			if(!baseMap.containsKey(baseName)){
				DroneBase droneBase = new DroneBase(baseName, basePosition.getLatitude(), basePosition.getLongitude(), basePosition.getAltitude(), radius);
				baseMap.put(baseName, droneBase);
			}
				else
				System.out.println("NOT able to add " + baseName + " to hashmap as it has already been added");
		}
	}
	
	public HashMap<String,DroneBase> getBases(){
		return baseMap;
	}

}
