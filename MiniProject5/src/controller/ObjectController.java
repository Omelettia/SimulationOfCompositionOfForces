package controller;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import model.MovingObject;
import model.Surface;
import model.object.CubeShapedObject;
import model.object.CylinderShapedObject;
import model.object.MainObject;

public class ObjectController implements Initializable {

    private CubeShapedObject aCube;
    private CylinderShapedObject aCylinder;
    private CubeShapedObject anObject;
    private MovingObject aMovingObject;
    private Surface surface;


    @FXML
    private ImageView cube;

    @FXML
    private ImageView cylinder;

    @FXML
    private ImageView object;

    @FXML
    private ImageView road;
    
    @FXML private Button pauseButton, replayButton;

    private double initialX, initialY, objectSceneX, objectSceneY, cubeSceneX, cubeSceneY, cylinderSceneX, cylinderSceneY;

    private double objectOriginalWidth, objectOriginalHeight;
    
 // Animation Transitions
    private TranslateTransition translate;
    private RotateTransition rotate;
    
    private static final double DEFAULT_MASS = 50.0;
    private static final double DEFAULT_SIDE_LENGTH = 100.0;
    private static final double DEFAULT_RADIUS = 50.0;
    private static final double MAX_SPEED = 200;
    private static final double MAX_ANGULAR_VELOCITY = 10;
    private static final double TIME = 100;
    private static final double DELTA_TIME = 30;
    private static final double A = 0.1;

    // Map to store the relationship between ImageView and corresponding object
    private Map<ImageView, MainObject> objectMap;
    
    public void setData(Surface surface) {
        this.surface = surface;   
     }
    
    
    
    public MovingObject getMovingObject() {
		return aMovingObject;
	}



	public void initialize(URL arg0, ResourceBundle arg1) {
        object.setImage(null);
        // Initialize your objects
        aCube = new CubeShapedObject(DEFAULT_MASS, 0, DEFAULT_SIDE_LENGTH);
        aCylinder = new CylinderShapedObject(DEFAULT_MASS, 0, DEFAULT_RADIUS, 0);
        anObject = new CubeShapedObject(0, 0, 0);
        aMovingObject = new MovingObject();
        aMovingObject.addObject(anObject);

        // Initialize the objectMap
        objectMap = new HashMap<>();
        objectMap.put(cube, aCube);
        objectMap.put(cylinder, aCylinder);
        objectMap.put(object, anObject);

        objectSceneX = object.getLayoutX();
        objectSceneY = object.getLayoutY();

        cubeSceneX = cube.getLayoutX();
        cubeSceneY = cube.getLayoutY();

        cylinderSceneX = cylinder.getLayoutX();
        cylinderSceneY = cylinder.getLayoutY();
                  
        objectOriginalWidth = object.getFitWidth();
        objectOriginalHeight = object.getFitHeight();
        
        // Set up drag-and-drop for each image
        setDragAndDrop(cube);
        setDragAndDrop(cylinder);
        setDragAndDrop(object);
        
     // Set up event handler for the Pause button
        pauseButton.setOnAction(event -> toggleAnimations());

        // Set up event handler for the Replay button
        replayButton.setOnAction(event -> replayAnimations());
        
      //Set up event for entering the parameters
        object.setOnMouseClicked(event -> showPopupMenu());
        setUpAnimations();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(DELTA_TIME), event -> changeSpeed())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
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
            double distanceToObject = Math.hypot(initialX - objectSceneX, initialY - objectSceneY);
            double distanceToCube = Math.hypot(initialX - cubeSceneX, initialY - cubeSceneY);
            double distanceToCylinder = Math.hypot(initialX - cylinderSceneX, initialY - cylinderSceneY);

            ImageView targetImage;
            if (distanceToObject < distanceToCube && distanceToObject < distanceToCylinder) {
                // Switch with object
                targetImage = object;
            } else if (distanceToCube < distanceToCylinder) {
                // Switch with cube
                targetImage = cube;
            } else {
                // Switch with cylinder
                targetImage = cylinder;
            }

            // Switch images
            Image tempImage = targetImage.getImage();
            targetImage.setImage(image.getImage());
            image.setImage(tempImage);

            // Retrieve the corresponding object from the map
            MainObject originalObject = objectMap.get(image);
            MainObject targetObject = objectMap.get(targetImage);

            // Update the objectMap with the new associations
            objectMap.put(targetImage, originalObject);
            objectMap.put(image, targetObject);
            aMovingObject.swapObject(objectMap.get(object));
            if (objectMap.get(object) == aCube) {
                object.setRotate(0);
            }
            	replayAnimations();
            
            
            // Reset translation
            image.setTranslateX(0);
            image.setTranslateY(0);
        });
    }
    
    private void showPopupMenu() {
        if (object.getImage() == null) {
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Values");
        dialog.setHeaderText(null);

        GridPane grid = new GridPane();
        grid.setHgap(10);

        TextField firstTextField = new TextField();
        TextField secondTextField = new TextField();

        // Add labels and text fields to the grid
        grid.add(new Label("Enter " + aMovingObject.getCurrentObject().getDimensionName()), 0, 0);
        grid.add(firstTextField, 1, 0);
        grid.add(new Label("Enter Mass:"), 0, 1);
        grid.add(secondTextField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(result -> {
            try {
                double enteredFirstValue = Double.parseDouble(firstTextField.getText());
                double enteredSecondValue = Double.parseDouble(secondTextField.getText());

                // Check if the entered mass exceeds the maximum allowed value
                if (enteredSecondValue > aMovingObject.getCurrentObject().getMaxWeight()) {
                    AlertHelper.showAlert("Invalid Input", "Mass must be less than or equal to " + aMovingObject.getCurrentObject().getMaxWeight());
                    return;
                }

                // Check if the entered dimension is within the valid range
                if (enteredFirstValue <= 0 || enteredFirstValue > aMovingObject.getCurrentObject().getMaxDimension()) {
                    AlertHelper.showAlert("Invalid Input", aMovingObject.getCurrentObject().getDimensionName() +
                            " must be greater than 0 and less than or equal to " + aMovingObject.getCurrentObject().getMaxDimension());
                    return;
                }

                // Set the dimensions in the MovingObject's current object
                aMovingObject.getCurrentObject().setDimension(enteredFirstValue);
                aMovingObject.getCurrentObject().setMass(enteredSecondValue);
                updateDimensions();
            } catch (NumberFormatException e) {
                AlertHelper.showAlert("Invalid Input", "Please enter valid numeric values for dimensions and mass.");
            }
        });
    }
    
    private void updateDimensions() {
        double scaleFactor = aMovingObject.getCurrentObject().getDimension()/
        		aMovingObject.getCurrentObject().getMaxDimension()		;
        double originalHeight = object.getFitHeight();
        double originalLayoutY = object.getLayoutY();

        object.setFitWidth(objectOriginalWidth * scaleFactor);
        object.setFitHeight(objectOriginalHeight * scaleFactor);

        // Adjust the Y-coordinate to keep the lowest point at the same height
        double newLayoutY = originalLayoutY + (originalHeight - object.getFitHeight());
        object.setLayoutY(newLayoutY);
    }
    
    private void setUpAnimations() {
        translate = new TranslateTransition();
        translate.setNode(road);
        translate.setDuration(Duration.millis(TIME));
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.setByX(-300);
        translate.setInterpolator(Interpolator.LINEAR);

        rotate = new RotateTransition();
        rotate.setNode(object);
        rotate.setDuration(Duration.millis(TIME));
        rotate.setCycleCount(RotateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setByAngle(360);
    }
    
    private void changeSpeed() {
        double speedFactor = aMovingObject.getCurrentObject().getVelocity() / MAX_SPEED;
        double angularFactor = aMovingObject.getCurrentObject().getAngularVelocity() / MAX_ANGULAR_VELOCITY;

        // Check if the animation is playing
        if ("▐▐".equals(pauseButton.getText()))  {
        	aMovingObject.getCurrentObject().updateMovement(surface);
            // Update current speed only when the animation is playing
            if (Math.abs(aMovingObject.getCurrentObject().getVelocity()) > MAX_SPEED ||
            		Math.abs(aMovingObject.getCurrentObject().getAngularVelocity()) > MAX_ANGULAR_VELOCITY) {
            	aMovingObject.setAppliedForce(0);;
            }
        }
        // Update TranslateTransition rate
        translate.setRate(speedFactor);
        // Update RotateTransition rate
        rotate.setRate(angularFactor);
    }
    
    private void playAnimations() {
            translate.play();
            rotate.play();
            pauseButton.setText("▐▐");
    }

    private void pauseAnimations() {
        translate.pause();
        rotate.pause();
        pauseButton.setText("▶"); // Set button text to Play
    }

    private void replayAnimations() {
        road.setTranslateX(0);
        object.setRotate(0);
        aCube.setVelocity(0);
        aCylinder.setVelocity(0);
        aCylinder.setAngularVelocity(0);
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
