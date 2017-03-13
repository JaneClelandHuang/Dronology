package view;

import java.io.File;

import controller.flightZone.FlightZoneException;
import controller.flightZone.FlightZoneManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ButtonGenerator {
	
	// Assumes a left to right layout.
	int btnXPosition = 0;
	int btnYPosition= 0;
			
	public ButtonGenerator(int startingY, int startingX){
		btnXPosition = startingX;
		btnYPosition = startingY;
	}
	
	public Button createButton(String btnText, int width){
		Button btn = new Button();
		btn.setLayoutX(btnXPosition);
		btn.setLayoutY(btnYPosition);
		btn.setPrefWidth(width);
		btn.setText(btnText);
		btnXPosition = btnXPosition + width + 20;
		return btn;
	}

	/**
	 * Load last flight plan
	 * @param flightZoneView
	 * @param btnText
	 * @param stage
	 * @return
	 */
	Button makeQuickFlightPickerButton(final FlightZoneView flightZoneView, String btnText, Stage stage){
	    Button quickLoad = createButton(btnText, 120);
	    quickLoad.setOnAction(
	    new EventHandler<ActionEvent>() {
	       @Override
	       public void handle(final ActionEvent e) {
	      	       flightZoneView.flightManager.loadFlightFromXML();
	   	       }
	    });
	    return quickLoad;
	}

	/**
	 * Interactively create a new flight plan
	 * @param flightZoneView TODO
	 * @param btnText TODO
	 * @param stage
	 * @return
	 */
	Button defineFlightButton(final FlightZoneView flightZoneView, String btnText, Stage stage){
	    Button quickLoad = createButton(btnText, 120);
	    FlightZoneView fzv = flightZoneView;
	    quickLoad.setOnAction(
	    new EventHandler<ActionEvent>() {
	       @Override
	       public void handle(final ActionEvent e) {       	   
	    	   flightZoneView.flightPlanWayPointCollection.reset();   
	    	   new InteractiveFlightPlannerView(flightZoneView.scene,stage, flightZoneView.flightPlanWayPointCollection, fzv);
	   	       }
	    });
	    return quickLoad;
	}

	/**
	 * Initiate the construction of a platoon
	 * @param flightManager
	 * @param btnText
	 * @param stage
	 * @return
	 */
	Button makePlatoonButton(FlightZoneManager flightManager, String btnText, Stage stage){
	    Button platoonBtn = createButton(btnText, 120);
	    platoonBtn.setText(btnText);
	    platoonBtn.setOnAction(
	    new EventHandler<ActionEvent>() {
	       @Override
	       public void handle(final ActionEvent e) {
	    	  flightManager.getDroneFleet().setupPlatoon();
	   	   }
	    });
	    return platoonBtn;
	}

	/**
	 * Load flight plans from file
	 * @param flightZoneView
	 * @param btnText
	 * @param stage
	 * @return
	 */
	Button makeFlightPickerButton(final FlightZoneView flightZoneView, String btnText, Stage stage){
		Button btnFlights = createButton(btnText, 120);
		btnFlights.setOnAction(
	       new EventHandler<ActionEvent>() {
	       @Override
	       public void handle(final ActionEvent e) {
	           // File file = fileChooser.showOpenDialog(stage);
	            FileChooser fileChooser = new FileChooser();
	   		    fileChooser.setTitle("Flight plan (xml)");
	   		    fileChooser.getExtensionFilters().addAll(
	   		         new ExtensionFilter("XML Files", "*.xml"));
	   		    File selectedFile = fileChooser.showOpenDialog(stage);
	   		    if (selectedFile != null) {
	   		       System.out.println("Selected " + selectedFile);
	   		       flightZoneView.flightManager.loadFlightFromXML(selectedFile.toString());
	   		    }
	            }
	       });
	    return btnFlights;
	}

	/**
	 * Initial return to home base command
	 * @param flightZoneView
	 * @param btnText
	 * @param stage
	 * @return
	 */
	Button makeReturnHomeButton(final FlightZoneView flightZoneView, String btnText, Stage stage){
	    Button platoonBtn = createButton(btnText, 120);
	    platoonBtn.setOnAction(
	    new EventHandler<ActionEvent>() {
	       @Override
	       public void handle(final ActionEvent e) {
	      	    try {
					flightZoneView.flightManager.getFlights().groundAllFlights();
				} catch (FlightZoneException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	   	     }
	    });
	    return platoonBtn;
	}
}
