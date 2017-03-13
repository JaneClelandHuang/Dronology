package controller.virtualDroneSimulator;
import controller.flightZone.FlightZoneException;

/**
 * Voltage simulator.  For every minute of flight, the battery decreases by 0.25 volts.
 * @author Jane
 *
 */
public class DroneVoltageSimulator {
	
	double voltage;
	long totalFlyingTime;
	public enum BatteryState {charging, depleting, stable};
	BatteryState batteryState = BatteryState.stable;
	final double voltsDrainedPerMinute = 0.25;
   
	long checkPointTime;
	
	public DroneVoltageSimulator(){
		voltage = 15.0;
		totalFlyingTime = 0;
		checkPointTime = 0;
	}
	
	public void rechargeBattery(){
		batteryState = BatteryState.charging;
		voltage = 15.0;
		batteryState = BatteryState.stable;
	}
	
	public void startBatteryDrain(){
		batteryState = BatteryState.depleting;
		checkPointTime = System.currentTimeMillis();
	}
	
	public void stopBatteryDrain(){
		checkPoint();
		batteryState = BatteryState.stable;		
	}
	
	public void checkPoint(){
		if(batteryState == BatteryState.depleting){
			long timeSinceLastCheckPoint = System.currentTimeMillis() - checkPointTime;
			if (timeSinceLastCheckPoint > 5000) {
				checkPointTime = System.currentTimeMillis(); // Reset checkPoint time
			
				// Volts drained per second * number of elapsed seconds
				double voltageDrain = voltsDrainedPerMinute/60 * (timeSinceLastCheckPoint/1000);
				voltage = voltage - voltageDrain;
			}
		} 
	}
	
	public double getVoltage(){
		return voltage;
	}
}
