package controller.flightZone;

/**
 * Supports customized flight zone exceptions
 * @author Jane Cleland-Huang
 * @version 0.1
 *
 */
public class FlightZoneException extends Throwable{
   public FlightZoneException(String msg){
	  super(msg);
   }
}
