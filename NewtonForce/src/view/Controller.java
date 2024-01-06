package view;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    
	private CubeShapedObject aCube;
	
	@FXML
    private Polygon AppliedForce;
	
	@FXML
    private Polygon Friction;

    @FXML
    private Polygon GravitationalForce;

    @FXML
    private Polygon NormalForce;
    
    @FXML
    private Polygon SumOfForce;
	
    @FXML
    private Text AppliedForceValue;
    
    @FXML
    private ImageView Background;

    @FXML
    private ImageView Object;
    
    @FXML
    private Text CubeMass;
    
    @FXML
    private Text FrictionValue;
    
    @FXML
    private Text CylinderMass;

    @FXML
    private Text GravitationalForceValue;

    @FXML
    private Text NormalForceValue;
    
    @FXML
    private Text ObjectMass;

    @FXML
    private Text SumOfForceValue;

    @FXML
    private ImageView Cube;

    @FXML
    private ImageView Cylinder;

    @FXML
    private Pane Container;

    @FXML
    private Button pauseButton;
    
    @FXML
    private Button ReplayButton;
    
    @FXML
    private VBox Acceleration;
    
    @FXML
    private VBox Speed;
    
    @FXML
    private CheckBox ValueButton;
    
    @FXML
    private CheckBox SumOfForceButton;

    @FXML
    private CheckBox SpeedButton;
    
    @FXML
    private CheckBox MassButton;

    @FXML
    private CheckBox ForceButton;
    
    @FXML
    private CheckBox AccelerationButton;

    private double initialX;
    private double initialY;
    
    private double objectSceneX;
    private double objectSceneY;
    
    private int current;

    private TranslateTransition translate;
    private RotateTransition rotate;
    
    private ContextMenu objectContextMenu;
    
    public void initialize(URL arg0, ResourceBundle arg1) {
    	showForceArrows(false);
        showSumOfForceArrow(false);
        showForceValues(false);
        showMassValues(false);
        showSpeedVBox(false);
        showAccelerationVBox(false);
        Object.setImage(null);
        setUpAnimations();

        // Set up drag and drop for the Cube
        setDragAndDrop(Cube);

        // Set up drag and drop for the Cylinder
        setDragAndDrop(Cylinder);
        setDragAndDropObject(Object);

        // Set up event handler for the Pause button
        pauseButton.setOnAction(event -> toggleAnimations());
     // Set up event handler for the Replay button
        ReplayButton.setOnAction(event -> replayAnimations());
        
        Object.setOnMouseClicked(event -> showPopupMenu());
        
     // Set up event handler for Force checkbox
        ForceButton.setOnAction(event -> {
            showForceArrows(ForceButton.isSelected());
        });

        // Set up event handler for SumOfForce checkbox
        SumOfForceButton.setOnAction(event -> {
            showSumOfForceArrow(SumOfForceButton.isSelected());
        });

        // Set up event handler for Value checkbox
        ValueButton.setOnAction(event -> {
            showForceValues(ValueButton.isSelected());
        });

        // Set up event handler for Mass checkbox
        MassButton.setOnAction(event -> {
            showMassValues(MassButton.isSelected());
        });

        // Set up event handler for Speed checkbox
        SpeedButton.setOnAction(event -> {
            showSpeedVBox(SpeedButton.isSelected());
        });

        // Set up event handler for Acceleration checkbox
        AccelerationButton.setOnAction(event -> {
            showAccelerationVBox(AccelerationButton.isSelected());
        });
    }

    private void setUpAnimations() {
        translate = new TranslateTransition();
        translate.setNode(Background);
        translate.setDuration(Duration.millis(1000));
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.setByX(-300);
        translate.setInterpolator(Interpolator.LINEAR);

        rotate = new RotateTransition();
        rotate.setNode(Object);
        rotate.setDuration(Duration.millis(1000));
        rotate.setCycleCount(TranslateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setByAngle(360);

      
    }

    private void setDragAndDrop(ImageView image) {
        
    	image.setOnMousePressed(event -> {
            initialX = image.getLayoutX();
            initialY = image.getLayoutY();
            
                        
        });

        image.setOnMouseDragged(event -> {
        	double offsetX = event.getSceneX() - initialX;
            double offsetY = event.getSceneY() - initialY;

            // Update the layout of the image
            image.setTranslateX(image.getTranslateX() + offsetX);
            image.setTranslateY(image.getTranslateY() + offsetY);

            // Update initial position for the next drag event
            initialX = event.getSceneX();
            initialY = event.getSceneY();
        });

        image.setOnMouseReleased(event -> {
        	 objectSceneX = Object.getLayoutX();
        	 objectSceneY = Object.getLayoutY();
        	 double distanceToObject = Math.hypot(initialX - objectSceneX, initialY - objectSceneY);

            // Check if the image is closer to the Object's center or its original place
            if (distanceToObject < 100) {
                // If closer to the Object's center, replace Object with Cube or Cylinder
                if (image == Cube) {
                    
                    if (current == 2)
                    {
                    	Cylinder.setImage(Object.getImage());
                    	
                    	
                    }
                    Object.setImage(Cube.getImage());
                    rotate.pause();
                    Object.setRotate(0);
                    current = 1;
                } else if (image == Cylinder) {
                    
                    
                    if (current == 1)
                    {
                    	Cube.setImage(Object.getImage());
                    }
                    Object.setImage(Cylinder.getImage());
                    if (pauseButton.getText().equals("▐▐")) {
                        rotate.play();
                    }
                    current = 2;
                }
                image.setImage(null);
                // Set image to null so it looks like the object has been switched
                image.setTranslateX(0);
                image.setTranslateY(0);
            } else { 
                image.setTranslateX(0);
                image.setTranslateY(0);
            }
        });

        
    }
    
    private void setDragAndDropObject(ImageView image) {
        
    	image.setOnMousePressed(event -> {
            initialX = image.getLayoutX();
            initialY = image.getLayoutY();
            
                        
        });

        image.setOnMouseDragged(event -> {
        	double offsetX = event.getSceneX() - initialX;
            double offsetY = event.getSceneY() - initialY;

            // Update the layout of the image
            image.setTranslateX(image.getTranslateX() + offsetX);
            image.setTranslateY(image.getTranslateY() + offsetY);

            // Update initial position for the next drag event
            initialX = event.getSceneX();
            initialY = event.getSceneY();
        });

        image.setOnMouseReleased(event -> {
        	double distanceToCylinder = Math.hypot(initialX - Cylinder.getLayoutX(), initialY - Cylinder.getLayoutY());
        	double distanceToCube = Math.hypot(initialX - Cube.getLayoutX(), initialY - Cube.getLayoutY());
        	
       	 if (current == 1) {
        	if (distanceToCylinder < 50 && Cylinder.getImage() != null)
        	{	
        		Cube.setImage(Object.getImage());
       		    image.setImage(Cylinder.getImage());
       		    current = 2;
       		    Cylinder.setImage(null);
       		 if (pauseButton.getText().equals("▐▐")) {
                 rotate.play();
             }
        		 
        	 }
        	
        	 else if (distanceToCube < 50)
        	{	
        	 
        		 Cube.setImage(Object.getImage());
        		 image.setImage(null);
        		 current = 0;
        	}
       	 }
        	else if  (current == 2) {
        		if (distanceToCylinder < 50)
            	{	
        			Cylinder.setImage(Object.getImage());
           		 image.setImage(null);
           		 current = 0;
            		 
            	 }
            	
            	 else	if (distanceToCube < 50 && Cube.getImage() != null)
            	{	
            		Cylinder.setImage(Object.getImage());
           		    image.setImage(Cube.getImage());
           		    Cube.setImage(null);
           		    rotate.pause();
           		    Object.setRotate(0);
           		    current = 1;
            	 
            	 }
        	}
       	 
       	 if (image.getImage() == null) {
             replayAnimations();
         }
       	 
        	image.setTranslateX(0);
            image.setTranslateY(0);
        	
            
        }
       	 );

        
    }


    private void showPopupMenu() {
    	if (Object.getImage() == null) {
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Values");
        dialog.setHeaderText(null);

        GridPane grid = new GridPane();
        grid.setHgap(10);

        TextField firstTextField = new TextField();
        TextField secondTextField = new TextField();

        if (current == 1) {
            grid.add(new Label("Enter Length:"), 0, 0);
            grid.add(firstTextField, 1, 0);
            grid.add(new Label("Enter Mass:"), 0, 1);
            grid.add(secondTextField, 1, 1);
        } else if (current == 2) {
            grid.add(new Label("Enter Radius:"), 0, 0);
            grid.add(firstTextField, 1, 0);
            grid.add(new Label("Enter Mass:"), 0, 1);
            grid.add(secondTextField, 1, 1);
        }

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(result -> {
            double enteredFirstValue = Double.parseDouble(firstTextField.getText());
            double enteredSecondValue = Double.parseDouble(secondTextField.getText());

            // Handle the entered values based on the current state (current == 1 or current == 2)
            if (current == 1) {
                // Handle values for current 1 (Length and Mass)
                // Example: You can use enteredFirstValue as Length and enteredSecondValue as Mass
            } else if (current == 2) {
                // Handle values for current 2 (Radius and Mass)
                // Example: You can use enteredFirstValue as Radius and enteredSecondValue as Mass
            }
        });
    }

    private void showForceArrows(boolean show) {
        boolean forceButtonSelected = ForceButton.isSelected();
        boolean valueButtonSelected = ValueButton.isSelected();

        // Update arrow visibility
        AppliedForce.setVisible(show && forceButtonSelected);
        GravitationalForce.setVisible(show && forceButtonSelected);
        NormalForce.setVisible(show && forceButtonSelected);
        Friction.setVisible(show && forceButtonSelected);

        // Update text visibility
        AppliedForceValue.setVisible(show && forceButtonSelected && valueButtonSelected && AppliedForce.isVisible());
        GravitationalForceValue.setVisible(show && forceButtonSelected && valueButtonSelected && GravitationalForce.isVisible());
        NormalForceValue.setVisible(show && forceButtonSelected && valueButtonSelected && NormalForce.isVisible());
        FrictionValue.setVisible(show && forceButtonSelected && valueButtonSelected && Friction.isVisible());
    }

    private void showSumOfForceArrow(boolean show) {
        boolean sumOfForceButtonSelected = SumOfForceButton.isSelected();
        boolean valueButtonSelected = ValueButton.isSelected();

        // Update arrow visibility
        SumOfForce.setVisible(show && sumOfForceButtonSelected);

        // Update text visibility
        SumOfForceValue.setVisible(show && sumOfForceButtonSelected && valueButtonSelected && SumOfForce.isVisible());
    }



    private void showForceValues(boolean show) {
        boolean forceButtonSelected = ForceButton.isSelected();
        boolean sumOfForceButtonSelected = SumOfForceButton.isSelected();

        // Check if the Force button is selected
        if (forceButtonSelected) {
            // Show or hide values based on the corresponding arrow visibility
            AppliedForceValue.setVisible(show && AppliedForce.isVisible());
            GravitationalForceValue.setVisible(show && GravitationalForce.isVisible());
            NormalForceValue.setVisible(show && NormalForce.isVisible());
            FrictionValue.setVisible(show && Friction.isVisible());
        } else {
            // Hide all values if the Force button is not selected
            AppliedForceValue.setVisible(false);
            GravitationalForceValue.setVisible(false);
            NormalForceValue.setVisible(false);
            FrictionValue.setVisible(false);
        }

        // Check if the SumOfForce button is selected and show or hide the SumOfForceValue based on the visibility of the arrow
        SumOfForceValue.setVisible(show && sumOfForceButtonSelected && SumOfForce.isVisible());
    }





    private void showMassValues(boolean show) {
        ObjectMass.setVisible(show);
        CylinderMass.setVisible(show);
        CubeMass.setVisible(show);
    }

    private void showSpeedVBox(boolean show) {
        Speed.setVisible(show);
    }

    private void showAccelerationVBox(boolean show) {
        Acceleration.setVisible(show);
    }

    private void playAnimations() {
    	if (Object.getImage() != null) {
            translate.play();
            pauseButton.setText("▐▐");
        }
    	
        
        if (Cylinder.getImage() == null) {
            rotate.play();
        }
        
        // Set button text to Pause
    }

    private void pauseAnimations() {
        translate.pause();
        rotate.pause();
        pauseButton.setText("▶"); // Set button text to Play
    }
    
    private void replayAnimations() {
        translate.stop(); // Stop the current animation
        
        if (Cylinder.getImage() == null) {
            rotate.stop(); // Stop the rotation animation only if the Cylinder image is null
        }

        Background.setTranslateX(0);
        Object.setRotate(0);

        pauseAnimations(); // Pause the animations after resetting
    }

    private void toggleAnimations() {
        if (translate.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            pauseAnimations();
        } else {
            playAnimations();
        }
    }
}
