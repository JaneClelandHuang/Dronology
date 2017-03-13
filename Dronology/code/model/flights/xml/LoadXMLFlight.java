package model.flights.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import controller.flightZone.FlightZoneManager;

/**
 * Loads flightplan from xml file.  See documentation.
 * @author Jane
 */
public class LoadXMLFlight {
	
	/**
	 * Loads flightplan from filepath
	 * @param flightZoneMgr
	 * @param filename
	 */
   public LoadXMLFlight(FlightZoneManager flightZoneMgr, String filename){
      try {	
         File inputFile = new File(filename);
   	  		System.out.println("Loading file: " + filename);
         SAXParserFactory factory = SAXParserFactory.newInstance();
         SAXParser saxParser = factory.newSAXParser();
         XMLFlightPlanImportHandler userhandler = new XMLFlightPlanImportHandler(flightZoneMgr);
         saxParser.parse(inputFile, userhandler);     
      } catch (Exception e) {
         e.printStackTrace();
      }
      SaveLastFileName(filename);
   } 
   
   /**
    * Loads flightplan using the previously used xml file.
    * @param flightZoneMgr
    */
   public LoadXMLFlight(FlightZoneManager flightZoneMgr){
	  String filename = RetrieveLastFileName();
	  System.out.println("Loading file: " + filename);
	  try {	
	     File inputFile = new File(filename);
	     SAXParserFactory factory = SAXParserFactory.newInstance();
	     SAXParser saxParser = factory.newSAXParser();
	     XMLFlightPlanImportHandler userhandler = new XMLFlightPlanImportHandler(flightZoneMgr);
	     saxParser.parse(inputFile, userhandler);     
	  } catch (Exception e) {
	     e.printStackTrace();
	  }
   }   
   
   /**
    * Saves current XML filename for future use.
    * @param filename
    */
   private void SaveLastFileName(String filename){
	   File fout = new File("configuration\\LastFileName.txt");
	   FileOutputStream fos;
	   try {
		   fos = new FileOutputStream(fout);
	  	   BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		   bw.write(filename);
		   bw.newLine();
	       bw.close();
	       fos.close();
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
	   System.out.println("End save");
   }
   
   /**
    * Get filename of previously loaded flightplan.
    * @return
    */
   private String RetrieveLastFileName(){
	   try {
		   	BufferedReader br = new BufferedReader(new FileReader("configuration\\LastFileName.txt"));
			String filename = br.readLine();
			br.close();		
			return filename;
	   } catch (IOException e) {
			e.printStackTrace();
	   }
	return null;
   }
}

