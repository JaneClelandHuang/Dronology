package controller.movement;

import java.util.ArrayList;

import controller.helper.Coordinates;

public class PlatoonFollowerDirector implements iFlightDirector{

	@Override
	public void returnHome(Coordinates home) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Coordinates flyToNextPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWayPoints(ArrayList<Coordinates> wayPoints) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearWayPoints() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasMoreWayPoints() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUnderSafetyDirectives() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clearCurrentWayPoint() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWayPoint(Coordinates wayPoint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRoundabout(ArrayList<Coordinates> roundAboutPoints) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flyHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean readyToLand() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean readyToTakeOff() {
		// TODO Auto-generated method stub
		return false;
	}

		
}
