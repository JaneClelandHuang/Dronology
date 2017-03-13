package model.flights.data;

import controller.flightZone.FlightZoneException;
import controller.helper.DecimalDegreesToXYConverter;
import controller.helper.DegreesFormatter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InteractiveWayPointDot {

	Circle wayPointDot;
	int wayPointNumber = 0;
	int radius = 10;
	int x;
	int y;
	long longitude;
	long latitude;
	
	public InteractiveWayPointDot(int x, int y) throws FlightZoneException{
		wayPointDot = new Circle();
		wayPointDot.setRadius(radius);;
		wayPointDot.setCenterX(x);
		wayPointDot.setCenterY(y);
		wayPointDot.setFill(Color.LIGHTSTEELBLUE);
		wayPointDot.setStroke(Color.BLACK);
		wayPointDot.setStrokeWidth(1);
		longitude = DecimalDegreesToXYConverter.getInstance().ConvertXCoordsToDecimalDegrees(x);
		latitude = DecimalDegreesToXYConverter.getInstance().ConvertYCoordsToDecimalDegrees(y);
		this.x = x;
		this.y = y;
	}
	
	public void setWayPointNumber(int wayPointNumber){
		this.wayPointNumber= wayPointNumber;
	}
	
	public Circle getWayPointDot(){
		return wayPointDot;
	}
	
	public Text getWayPointTextNumber(){
		String degrees = " ("+DegreesFormatter.prettyFormatDegrees(latitude) + "," + DegreesFormatter.prettyFormatDegrees(longitude)+")";
		Text t = new Text(wayPointDot.getCenterX()+radius, wayPointDot.getCenterY()-radius/2,((Integer)wayPointNumber).toString() + degrees);
		t.setFont(Font.font ("Verdana", 10));
		return t;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
}
