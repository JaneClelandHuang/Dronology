package model.flights.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FlightPlanWayPointCollection {
	Map<Integer,InteractiveWayPointDot> dotMap;
	int wayPointNumber;
	
	public FlightPlanWayPointCollection(){
		dotMap = new HashMap<Integer,InteractiveWayPointDot>();
		wayPointNumber = 1;
		reset();
	}
	
	public int addPoint(InteractiveWayPointDot dot){
		dotMap.put(wayPointNumber, dot);
		wayPointNumber++;
		return wayPointNumber-1;
	}
	
	public Iterator<InteractiveWayPointDot> getIterator(){
		return dotMap.values().iterator();
	}
	
	public InteractiveWayPointDot getWayPointDot(Integer wayPoint){
		return dotMap.get(wayPoint);
	}
	
	public void reset(){
		wayPointNumber = 1;
		dotMap.clear();
	}
	
	public void saveFile(){}

	
}
