package model.flights.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import controller.flightZone.FlightZoneManager;
import controller.helper.Coordinates;

/**
 * Provides a mapping for importing XML based flight plan.
 * Called by file for SAX XML File loader
 * @author Jane
 * Version0.1
 */
public class XMLFlightPlanImportHandler extends DefaultHandler {

	   boolean bFlight = false;
	   boolean bLat = false;
	   boolean bLong = false;
	   boolean bAlt = false;
	   FlightZoneManager fzm;
	   
	   ArrayList<Coordinates> wayPoints = null;
	   Long lat, lon;
	   int alt;
	   
	   public XMLFlightPlanImportHandler(FlightZoneManager fzm){
		   this.fzm = fzm;
	   }
	 
	   @Override
	   public void startElement(String uri, 
	      String localName, String qName, Attributes attributes)
	         throws SAXException {
	      if (qName.equalsIgnoreCase("flight")) {
	         bFlight = true;
	      } else if (qName.equalsIgnoreCase("latitude")) {
	         bLat = true;
	      } else if (qName.equalsIgnoreCase("longitude")) {
	         bLong = true;
	      }
	      else if (qName.equalsIgnoreCase("alt")) {
	         bAlt = true;
	      }
	   }

	   @Override
	   public void endElement(String uri, 
	      String localName, String qName) throws SAXException {
	      if (qName.equalsIgnoreCase("flight")) {
	         System.out.println("End Element :" + qName);
	         System.out.println("Adding all waypoints: " + wayPoints.size());
	         fzm.planFlight(wayPoints.get(0), wayPoints);
	      }
	   }

	   @Override
	   public void characters(char ch[], 
	      int start, int length) throws SAXException {
	      if (bLat) {         
	         String strLat =  new String(ch, start, length);
	         lat = Long.parseLong(strLat);
	         System.out.println("Latitude: " + strLat);	 
	         bLat = false;
	      } else if (bLong) {
	    	 String strLong =  new String(ch, start, length);
	         lon = Long.parseLong(strLong);
	         System.out.println("Longitude: " + strLong);	 
	         bLong = false;
	      } else if (bAlt) {
	    	  String strAlt =  new String(ch, start, length);
	          alt = Integer.parseInt(strAlt);
	          System.out.println("Altitude: " + strAlt);	 
	          bAlt = false;
	          wayPoints.add(new Coordinates(lat,lon,alt));
	      } else if (bFlight) {
	         System.out.println("Flight: "); 
	         //+ new String(ch, start, length));
	    	 wayPoints = new ArrayList<Coordinates>();
	         bFlight = false;
	      }
	   }
	}