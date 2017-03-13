package model.drone.runtime;

/**
 * Associates a drone safety state object with a drone.
 * ONLY set this in the drone constructor.  NEVER interchange at runtime - otherwise drone state will be incorrectly changed.
 * @author Jane Cleland-Huang
 * @version 0.01
 *
 */
public class DroneSafetyModeState {
		    
	    public enum SafetyMode {Diverted, Halted, Normal};
	    SafetyMode safetyMode; 
	    
	    /**
	     * Constructor
	     * States for both FlightMode and SafetyMode set to initial state
	     */
	    public DroneSafetyModeState(){
	    	safetyMode = SafetyMode.Normal;
	    }
	    
	    // Setters
	    public void setSafetyModeToNormal(){
	    	safetyMode = SafetyMode.Normal;
	    }
	    
	    public void setSafetyModeToDiverted(){
	    	safetyMode = SafetyMode.Diverted;
	    }
	    
	    public void setSafetyModeToHalted(){
	    	safetyMode = SafetyMode.Halted;
	    }
	    	    
	    public boolean isSafetyModeNormal(){
	    	if (safetyMode == SafetyMode.Normal)
	    		return true;
	    	else
	    		return false;
	    }
	    
	    public boolean isSafetyModeDiverted(){
	    	if (safetyMode == SafetyMode.Diverted)
	    		return true;
	    	else
	    		return false;
	    }	    	    
	   
	    public boolean isSafetyModeHalted(){
	    	if (safetyMode == SafetyMode.Halted)
	    		return true;
	    	else
	    		return false;
	    }		

	    public String getSafetyStatus() {
			return safetyMode.toString();
		}

}
