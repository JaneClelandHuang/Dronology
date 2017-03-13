package controller;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import controller.flightZone.ConvertDecimalDegreesToXYCoords;
import controller.flightZone.ZoneBounds;

// Add a new test which transforms in both directions accurately!

public class CoordinateTransformationTest {
	@Test
	public void testOriginTransform() {
		ZoneBounds zoneBounds;
		long latNorth = 42720000;
		long latSouth = 42500000;
		long lonWest = -86200000;
		long lonEast = -85000000;
		int leftPanel = 180;
		int alt = 100;
		zoneBounds = ZoneBounds.getInstance();	
		zoneBounds.setZoneBounds(latNorth, lonWest, latSouth, lonEast, alt);		
		long xRange = 1600;
		long yRange = 960;
		ConvertDecimalDegreesToXYCoords coordTransformation = new ConvertDecimalDegreesToXYCoords(xRange,yRange,180);
		Point pnt = coordTransformation.getPoint(latNorth,lonWest);
		System.out.println(pnt);
		assert(pnt.x == leftPanel);
		assert(pnt.y == 0);
	}
	@Test
	public void TestOriginTransform2(){
		ZoneBounds zoneBounds;
		long latNorth = -42720000;
		long latSouth = -42500000;
		long lonWest = 86200000;
		long lonEast = 85000000;
		int alt = 100;
		int leftPanel = 180;
		zoneBounds = ZoneBounds.getInstance();	
		zoneBounds.setZoneBounds(latNorth, lonWest, latSouth, lonEast, alt);		
		long xRange = 1600;
		long yRange = 960;
		ConvertDecimalDegreesToXYCoords coordTransformation = new ConvertDecimalDegreesToXYCoords(xRange,yRange,180);
		Point pnt = coordTransformation.getPoint(latNorth,lonWest);
		System.out.println(pnt);
		assert(pnt.x == leftPanel);
		// Note this only works because the zone is square.  If it is not square, then y and x are scaled according to the x fit.
		assert(pnt.y == 0);
	}
	
	@Test
	public void testDiaganolCornerTransform() {
		ZoneBounds zoneBounds;
		long latNorth = 42720000;
		long latSouth = 42500000;
		long lonWest = -86200000;
		long lonEast = -85000000;
		int alt = 100;
		zoneBounds = ZoneBounds.getInstance();	
		zoneBounds.setZoneBounds(latNorth, lonWest, latSouth, lonEast, alt);		
		long xRange = 1600;
		long yRange = 960;
		ConvertDecimalDegreesToXYCoords coordTransformation = new ConvertDecimalDegreesToXYCoords(xRange,yRange,180);
		Point pnt = coordTransformation.getPoint(latSouth,lonEast);
		System.out.println(pnt);
		assert(pnt.x == xRange);
		// y uses x scaling so we check for y coordinate scal
		assert(pnt.y == 260); 
	}
}
