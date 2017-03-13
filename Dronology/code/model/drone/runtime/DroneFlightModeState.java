package model.drone.runtime;

import controller.flightZone.FlightZoneException;

/**
 * Associates a drone state object with a drone.
 * ONLY set this in the drone constructor.  NEVER interchange at runtime - otherwise drone state will be incorrectly changed.
 * State changes for Flight Modes must follow the transition:
 * OnGround -> AwaitingTakeOffClearance -> TakingOff -> Flying -> Landing
 * All other transitions will result in an exception being thrown
 * @author Jane Cleland-Huang
 * @version 0.01
 *
 */
public class DroneFlightModeState {
	// Status
		public enum FlightMode {OnGround, AwaitingTakeOffClearance, TakingOff, Flying, Landing};
	    FlightMode currentFlightMode = FlightMode.OnGround;
	     	    
	    /**
	     * Constructor
	     * States for both FlightMode and SafetyMode set to initial state
	     */
	    public DroneFlightModeState(){
	    	currentFlightMode = FlightMode.OnGround;
	    }
	    
	    /////////////////////
	    //Setters
	    /////////////////////
	    /**
	     * Set Flight Mode to OnGround
	     * @throws FlightZoneException if mode change does not follow allowed state transition.
	     */
	    public void setModeToOnGround() throws FlightZoneException{
	    	if (currentFlightMode == FlightMode.Landing)
	    		currentFlightMode = FlightMode.OnGround;
	    	else
	    		throw new FlightZoneException("You may not transition to FlightMode.OnGround directly from " + currentFlightMode);
	    }
	    
	    /**
	     * Set Flight mode to awaiting Takeoff Clearance
	     * @throws FlightZoneException if mode change does not follow allowed state transition.
	     */
	    public void setModeToAwaitingTakeOffClearance() throws FlightZoneException{
	       	if (currentFlightMode == FlightMode.OnGround)
	    		currentFlightMode = FlightMode.AwaitingTakeOffClearance;
	    	else
	    		throw new FlightZoneException("You may not transition to FlightMode.AwaitingTakeOffClearance directly from " + currentFlightMode);	
	    }
	    
	    /**
	     * Set flight mode to Taking off
	     * @throws FlightZoneException if mode change does not follow allowed state transition.
	     */
	    public void setModeToTakingOff() throws FlightZoneException{
	    	if (currentFlightMode == FlightMode.AwaitingTakeOffClearance)
	    		currentFlightMode = FlightMode.TakingOff;
	    	else
	    		throw new FlightZoneException("You may not transition to FlightMode.TakingOff directly from " + currentFlightMode);	
	    }
	    
	    /**
	     * Set flight mode to Flying
	     * @throws FlightZoneException if mode change does not follow allowed state transition.
	     */
	    public void setModeToFlying() throws FlightZoneException{
	    	if (currentFlightMode == FlightMode.TakingOff)
	    		currentFlightMode = FlightMode.Flying;
	    	else
	    		throw new FlightZoneException("You may not transition to FlightMode.Flying directly from " + currentFlightMode);	
	    }
	    
	    /**
	     * Set flight mode to Landing
	     * @throws FlightZoneException if mode change does not follow allowed state transition.
	     */
	    public void setModeToLanding() throws FlightZoneException{
	    	if (currentFlightMode == FlightMode.Flying)
	    		currentFlightMode = FlightMode.Landing;
	    	else
	    		throw new FlightZoneException("You may not transition to FlightMode.Landing directly from " + currentFlightMode);	
	    }
	    
	    
	    ///////////////////////////////////
	    // Getters
	    //////////////////////////////////
	    
	    /**
	     * 
	     * @return true if drone is currently on the ground, false otherwise
	     */
	    public boolean isOnGround(){
	    	if (currentFlightMode == FlightMode.OnGround)
	    		return true;
	    	else
	    		return false;
	    }
	    
	    /**
	     * 
	     * @return true if drone is currently in AwaitingTakeOffClearance mode, false otherwise
	     */
	    public boolean isAwaitingTakeoffClearance() {
			if (currentFlightMode == FlightMode.AwaitingTakeOffClearance)
				return true;
			else
				return false;
		}
	    
	    
	    /**
	     * 
	     * @return true if drone is currently taking off, false otherwise
	     */
		public boolean isTakingOff() {
			if (currentFlightMode == FlightMode.TakingOff)
				return true;
			else
				return false;
		}
		
		/**
		 * 
		 * @return true if drone is currently flying, false otherwise
		 */
		public boolean isFlying() {
			if (currentFlightMode == FlightMode.Flying)
				return true;
			else
				return false;
		}
		
		/**
		 * 
		 * @return true if drone is currently landing, false otherwise
		 */
		public boolean isLanding() {
			if (currentFlightMode == FlightMode.Landing)
				return true;
			else
				return false;
		}

		/** 
		 * 
		 * @return current status
		 */
		public String getStatus() {		
			return currentFlightMode.toString();
		}
}
