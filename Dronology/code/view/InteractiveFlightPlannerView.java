package view;

import java.io.File;
import java.io.IOException;

import controller.flightZone.FlightZoneException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.flights.data.FlightPlanWayPointCollection;
import model.flights.xml.SaveXMLFlight;

public class InteractiveFlightPlannerView {
	
	Stage mainStage;
	Scene mainScene;
	FlightPlanWayPointCollection wayPoints;
	FlightZoneView fzv;
	private double xOffset = 10;
	private double yOffset = 700;
	SaveXMLFlight saveFlight;
	
	public InteractiveFlightPlannerView(Scene mainScene, Stage mainStage, FlightPlanWayPointCollection flightPlanWayPointCollection, FlightZoneView fzv){
		this.mainStage = mainStage;
		this.mainScene = mainScene;	
		wayPoints = flightPlanWayPointCollection;
		createFlightPlanningWindow();
		this.fzv = fzv;
	}
	
	private void createFlightPlanningWindow(){	
		     
      AnchorPane fpRoot = new AnchorPane();
      Scene flightPlanScene = new Scene(fpRoot, 300, 300); 
	  Stage flightPlanStage = new Stage();
      flightPlanStage.setTitle("Interactive Flight Planner");
      flightPlanStage.setScene(flightPlanScene);                  
      
      Button btnNewFlight = createNewFlightButton(flightPlanStage);
	  fpRoot.getChildren().add(btnNewFlight);
	  
	  Button btnSaveFlight = createSaveFlightButton(flightPlanStage);
	  fpRoot.getChildren().add(btnSaveFlight);
	          
      //Set position of second window, related to primary window.
      flightPlanStage.setX(mainStage.getX()+10);
      flightPlanStage.setY(700);//mainStage.getY() + 100);
      flightPlanStage.show();
      flightPlanStage.setAlwaysOnTop(true);
      
      fpRoot.setOnMousePressed(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
              xOffset = event.getSceneX();
              yOffset = event.getSceneY();
          }
      });
      fpRoot.setOnMouseDragged(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
              flightPlanStage.setX(event.getScreenX() - xOffset);
              flightPlanStage.setY(event.getScreenY() - yOffset);
          }
      });      
	}
	
	private Button createNewFlightButton(Stage stage){
        Button btnNewFlight = new Button();
        btnNewFlight.setLayoutX(10);
        btnNewFlight.setLayoutY(10);;
        btnNewFlight.setText("Record New Flight");
        btnNewFlight.setOnAction(
        new EventHandler<ActionEvent>() {
           @Override
           public void handle(final ActionEvent e) {
        	   fzv.setPlanningMode(true);
        	}});
        return btnNewFlight;
	}
	
	private Button createSaveFlightButton(Stage stage){
        Button btnSaveFlight = new Button();
        btnSaveFlight.setLayoutX(10);
        btnSaveFlight.setLayoutY(60);;
        btnSaveFlight.setText("Save Flight");

        btnSaveFlight.setOnAction(       
        new EventHandler<ActionEvent>() {
           @Override
           public void handle(final ActionEvent e) {
        	   FileChooser fileChooser = new FileChooser();
       		   fileChooser.setTitle("Flight plan (xml)");
       		   File file = fileChooser.showSaveDialog(stage);
       		   if (file != null) {
       		       System.out.println("Selected " + file);
       		       try {
       		    	    fzv.setPlanningMode(false);
    				    new SaveXMLFlight(file.getAbsolutePath(),wayPoints);
				   } catch (IOException | FlightZoneException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    			   }     
       	    }
        }});
        return btnSaveFlight;
	}	
}
	
	