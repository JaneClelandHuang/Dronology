

package view;
import java.util.ArrayList;
import java.util.HashMap;
import controller.flightZone.FlightZoneException;
import controller.flightZone.FlightZoneManager;
import controller.flightZone.ZoneBounds;
import controller.helper.DecimalDegreesToXYConverter;
import controller.helper.DegreesFormatter;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.bases.BaseManager;
import model.bases.DroneBase;
import model.drone.fleet.RuntimeDroneTypes;
import model.drone.runtime.ManagedDrone;
import model.flights.data.FlightPlan;
import model.flights.data.FlightPlanWayPointCollection;
import model.flights.data.Flights;
import model.flights.data.InteractiveWayPointDot;
import model.flights.xml.SaveXMLFlight;
import javafx.stage.Stage;
import view.DroneImage;

/**
 * Starts up the drone formation simulation extends Application class from JavaFX
 * Needs JavaFX to be installed. 
 * For eclipse:  Help / Add new Software / http://download.eclipse.org/efxclipse/updates-released/2.4.0/site
 * @author Jane Cleland-Huang
 * @version 0.1
 *
 */
public class FlightZoneView extends Application {
		
	Scene scene;
	FlightZoneManager flightManager;
	BaseManager baseManager;
	SaveXMLFlight saveFlight;
	Flights flights;
	AnchorPane root;
	static long xRange = 1600;
	static long yRange = 960;
	ArrayList<DroneImage> allDroneImages;
	ArrayList<ImageView> imageViewQueue;  // Temporarily stores new images so that they can be added to the scene by the FX thread.
	ArrayList<ImageView> delImageViews; 

	 	
	static int LeftDivider = 180;
	ButtonGenerator btnMaker = new ButtonGenerator((int)yRange-40,LeftDivider);
	ArrayList<FlightPlan> currentFlights;
	ArrayList<FlightPlan> pendingFlights;
	ArrayList<FlightPlan> completedFlights;
	
    FlightPlanWayPointCollection flightPlanWayPointCollection;
    
	final FileChooser fileChooser = new FileChooser();
	private FlightZoneStatusPanel leftStatusDisplay;	
	boolean planningMode = false;
	
	/**
	 * Initial setup included setting simulation type
	 * @param args
	 */
	public static void startFlightZoneView(String[] args){		
		RuntimeDroneTypes runtimeMode = RuntimeDroneTypes.getInstance();
		try {
			runtimeMode.setVirtualEnvironment();
		} catch (FlightZoneException e) {
			e.printStackTrace();
		}		
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception { 
		
		    flightPlanWayPointCollection = new FlightPlanWayPointCollection();
		    allDroneImages = new ArrayList<DroneImage>();
		    delImageViews = new ArrayList<ImageView>();
	        root = new AnchorPane();	
	       
	        imageViewQueue = new ArrayList<ImageView>();
			scene = new Scene(root,xRange,yRange); 
			
			stage.setScene(scene);
			stage.show();
			
			Canvas canvas = new Canvas(LeftDivider, yRange);
	        GraphicsContext gc = canvas.getGraphicsContext2D();
			root.getChildren().add(canvas);
			
			startFlightManager();	
			
			ZoneBounds zb = ZoneBounds.getInstance();
			String flightArea = "(" + DegreesFormatter.prettyFormatDegrees(zb.getNorthLatitude()) + "," + 
					DegreesFormatter.prettyFormatDegrees(zb.getWestLongitude()) + ") - (" + 
					DegreesFormatter.prettyFormatDegrees(zb.getSouthLatitude()) + "," + 
					DegreesFormatter.prettyFormatDegrees(zb.getEastLongitude())+")";
			
			stage.setTitle("Formation Simulator: " + flightArea);

		
			leftStatusDisplay = new FlightZoneStatusPanel(gc, LeftDivider,(int)yRange);

	        displayFlightInfo(gc);
	        
	        addButtons(root, stage);
	        
			    scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    public void handle(MouseEvent me) {
			    	if (planningMode == true){
			    		InteractiveWayPointDot dot;
						try {
							dot = new InteractiveWayPointDot((int)(me.getSceneX()),(int)(me.getSceneY()));
							int wayPointNum = flightPlanWayPointCollection.addPoint(dot);
				    		dot.setWayPointNumber(wayPointNum);
				    	    root.getChildren().add(dot.getWayPointDot());
				    	    Text text = dot.getWayPointTextNumber();
				    	  	root.getChildren().add(text);
				    		System.out.println("Mouse entered " + me.getSceneX() + " " + me.getSceneY()); 
						} catch (FlightZoneException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		    		
			    	}
			    }
			});
			
			new AnimationTimer() { 
	            @Override
	            public void handle(long now) {
	            	
	            	// We cannot directly call loadImage from another thread.  FX thread has to load them itself.
	            	while (!imageViewQueue.isEmpty()){
	            		loadImage(imageViewQueue.get(0));
	            	    imageViewQueue.remove(0);
	            	}
	            	
	            	for(ImageView imgView: delImageViews){
	            		if(root.getChildren().contains(imgView))
	            			root.getChildren().remove(imgView);
	            	}
	            		
	            	for(DroneImage droneImage: allDroneImages){
	            			// The droneImage holds references to both the ImageView and an inner drone.
	            			// Ask the droneImage object to update the ImageView's coordinates from the drone's current position.
	            			//iDrone drone = droneImage.getDroneFromImage();
	            			try {
								droneImage.updateImageCoordinates();
							} catch (FlightZoneException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	            	} 
	            	displayFlightInfo(gc);
	            }
	        }.start();
	}	
	
	private void addButtons (AnchorPane root, Stage stage){
		// Buttons @Technical Debt.  make a button factory!!
        Button btnFlights = btnMaker.makeFlightPickerButton(this,"Load Flight Plans", stage);
        Button btnQuickFlights = btnMaker.makeQuickFlightPickerButton(this, "Load Last Flight",stage);    
        Button btnDefineNewFlights = btnMaker.defineFlightButton(this, "Create Flight",stage);   
        Button btnFormPlatoon = btnMaker.makePlatoonButton(flightManager, "Form Platoon",stage);
        Button btnReturnHome = btnMaker.makeReturnHomeButton(this, "Ground All Flights",stage);
        root.getChildren().add(btnFlights);
	    root.getChildren().add(btnQuickFlights);
	    root.getChildren().add(btnDefineNewFlights);
	    root.getChildren().add(btnFormPlatoon);
	    root.getChildren().add(btnReturnHome);
	}
	
	public int getReservedLeftHandSpace(){
		return LeftDivider;
	}
	
	private void displayFlightInfo(GraphicsContext gc){
		leftStatusDisplay.displayTopOfPanel();
		leftStatusDisplay.displayCurrentFlights(flights.getCurrentFlights(), flights.getAwaitingTakeOffFlights()); // delegate
		leftStatusDisplay.displayPendingFlights(flights.getPendingFlights());
		leftStatusDisplay.displayCompletedFlights(flights.getCompletedFlights());
	}
			
	/**
	 * Starts the flight manager.  Sets zone bounds for the simulation.
	 * @throws InterruptedException
	 */
	public void startFlightManager() throws InterruptedException{
		ZoneBounds zoneBounds = ZoneBounds.getInstance();
	    //zoneBounds.setZoneBounds(42722381, -86290828, 41660473, -86140256, 100);
	    zoneBounds.setZoneBounds(41761022, -86243311, 41734699, -86168252, 100);
		DecimalDegreesToXYConverter.getInstance().setUp(xRange, yRange, LeftDivider);  //Setup happens only once.  Must happen after Zonebounds are set.
		try {
			constructBases(5);
		} catch (FlightZoneException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flightManager = new FlightZoneManager(this, baseManager);
		flights = flightManager.getFlights();
		flightManager.startThread();
	}
	
	public void constructBases(int baseCount) throws FlightZoneException{
		baseManager = new BaseManager(5);
		HashMap<String,DroneBase> droneBases = baseManager.getBases();
		for (DroneBase base: droneBases.values()) 
		    root.getChildren().add(base.getCircle());	    
	}
	
	/** 
	 * Add an drone image
	 * @param droneImg
	 */
	public void addDroneImage(DroneImage droneImg){
		allDroneImages.add(droneImg);
	}
	
	/**
	 * Remove a drone image
	 * @param droneImg
	 * @throws FlightZoneException
	 */
	public void removeDroneImage(DroneImage droneImg) throws FlightZoneException{
		if(allDroneImages.contains(droneImg))
			allDroneImages.remove(droneImg);
	    else 
	    	throw new FlightZoneException("DroneFlight image not found in list and therefore not removed.");
	}
	
	/**
	 * Creates a drone image for a drone
	 * @param drone
	 * @throws FlightZoneException 
	 */
	public void createDroneImage(ManagedDrone drone) throws FlightZoneException{
		DroneImage droneImage;
		if(drone.getDroneImage()== null){
			droneImage = new DroneImage(drone,this);
			drone.registerImage(droneImage);
		} else 
			droneImage = drone.getDroneImage();
		imageViewQueue.add(droneImage.getDroneImage());
		allDroneImages.add(droneImage);  
	}
	
	/**
	 * Remove drone image given a drone object
	 * @param drone
	 */
	public void removeDroneImage(ManagedDrone drone){
		if (drone.getDroneImage()!=null){
			DroneImage droneImage = drone.getDroneImage();
			delImageViews.add(droneImage.getDroneImage());
		}
	}
	
	/**
	 * Load an image view
	 * Refactoring needed
	 * @param imgView
	 */
	public void loadImage(ImageView imgView){	
		if(!(root.getChildren().contains(imgView)))
			root.getChildren().add(imgView);
	}
	
	/**
	 * Remove an image view
	 * Refactoring needed
	 * @param imgView
	 */
	public void removeImage(ImageView imgView){
		System.out.println(" NOW REMOVING AN IMAGE ");
		if (root.getChildren().contains(imgView))
			root.getChildren().remove(imgView);
	}
	
	/**
	 * 	
	 * @return X range of screen display
	 */
	public long getXRange(){
		return xRange;
	}
	
	/**
	 * 
	 * @return Y range of screen display
	 */
	public long getYRange(){
		return yRange;
	}
	
	public void setPlanningMode(boolean planningStatus){
		if (planningStatus == true)
			planningMode = true;
		else
			planningMode = false;
	}
	
}	


