package model.flights.xml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import controller.flightZone.FlightZoneException;
import controller.helper.DecimalDegreesToXYConverter;
import model.flights.data.FlightPlanWayPointCollection;
import model.flights.data.InteractiveWayPointDot;

public class SaveXMLFlight {
	
	BufferedWriter bw;
	FileWriter fwriter;
	FlightPlanWayPointCollection flightPlanner;
	
	public SaveXMLFlight(String filename,FlightPlanWayPointCollection flightPlanner) throws IOException, FlightZoneException{
	   this.flightPlanner = flightPlanner;
	   fwriter = new FileWriter(filename);
	   bw = new BufferedWriter(fwriter);
	   bw.write("<?xml version="+'"'+"1.0"+'"'+"?>");
	   bw.newLine();
	   bw.write("<flights>");
	   bw.newLine();
	   writeOneFlight();
	   bw.write("</flights>");
	   bw.newLine();

	   bw.close();
	   fwriter.close();
	}
	
	private void writeOneFlight() throws IOException, FlightZoneException{
		bw.write("<flight>");
		bw.newLine();
		Iterator<InteractiveWayPointDot> itr = flightPlanner.getIterator();
		DecimalDegreesToXYConverter degConverter = DecimalDegreesToXYConverter.getInstance();
		while(itr.hasNext()){
			InteractiveWayPointDot dot = itr.next();
			long latitude = degConverter.ConvertYCoordsToDecimalDegrees(dot.getY());
			long longitude = degConverter.ConvertXCoordsToDecimalDegrees(dot.getX());
			bw.write("<wayPoint>");
			bw.newLine();
			bw.write('\t'+"<latitude>"+latitude+"</latitude>");
			bw.newLine();
			bw.write('\t'+"<longitude>"+longitude+"</longitude>");
			bw.newLine();
			bw.write('\t'+"<alt>"+10+"</alt>"); // Technical debt!!!
			bw.newLine();
			bw.write("</wayPoint>");
			bw.newLine();
		}
		bw.write("</flight>");
		bw.newLine();
	}
}
