package view;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.util.Duration;
import Object.CubeShapedObject;
import Object.CylinderShapedObject;
import Surface.Surface;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // Constants
    private static final int NONE = 0;
    private static final int CUBE = 1;
    private static final int CYLINDER = 2;

    private static final double MAX_FORCE = 100.0;
    private static final double MAX_WEIGHT = 100.0;
    private static final double MAX_SIDE_LENGTH = 100.0;
    private static final double MAX_RADIUS = 100.0;
    private static final double MAX_SPEED = 200;
    private static final double MAX_ANGULAR_VELOCITY = 10;
    private static final double TIME = 100;
    private static final double DELTA_TIME = 30;
    private static final double A = 0.1;

    // Objects
    private CubeShapedObject aCube;
    private CylinderShapedObject aCylinder;
    private Surface surface;

    // UI Elements
    @FXML private Polygon appliedForce, friction, gravitationalForce, normalForce, sumOfForce;
    @FXML private ImageView background, road, object, cube, cylinder;
    @FXML private Text objectMass, appliedForceValue, gravitationalForceValue, normalForceValue,
            frictionValue, sumOfForceValue, speedValue, accelerationValue;
    @FXML private Pane container;
    @FXML private Button pauseButton, replayButton;
    @FXML private VBox acceleration, speed;
    @FXML private CheckBox valueButton, sumOfForceButton, speedButton, massButton, forceButton, accelerationButton;
    @FXML private Slider forceSlider, staticFrictionSlider, kineticFrictionSlider;
    @FXML private TextField staticFrictionTextField, kineticFrictionTextField, forceTextField;

    // Animation Transitions
    private TranslateTransition translate;
    private RotateTransition rotate;

    // Drag and Drop
    private double initialX, initialY, objectSceneX, objectSceneY;
    private int current = NONE;

	private double objectOriginalWidth, objectOriginalHeight, originalLayoutSum;

    // Object Properties
    private double objectMassValue, sideLengthValue, radiusValue;
    private double staticFrictionNum, kineticFrictionNum;
    private double appliedForceNum, gravitationalForceNum, normalForceNum, cubeFrictionNum, cylinderFrictionNum,
            frictionNum, sumOfForceNum, speedNum, accelerationNum,
            angularVelocityNum, angularAccelerationNum;

    // Other
    private boolean swapOccurred = false;
    
 // Default values
    private static final double DEFAULT_MASS = 50.0;
    private static final double DEFAULT_SIDE_LENGTH = 100.0;
    private static final double DEFAULT_RADIUS = 100.0;
    private static final double DEFAULT_STATIC_FRICTION = 0.0;
    private static final double DEFAULT_KINETIC_FRICTION = 0.0;
    private static final double DEFAULT_APPLIED_FORCE = 0.0;
    private static final double DEFAULT_SPEED = 0.0;
    private static final double DEFAULT_ANGULAR_VELOCITY = 0.0;

    private void initializeDefaults() {
        objectMassValue = DEFAULT_MASS;
        sideLengthValue = DEFAULT_SIDE_LENGTH;
        radiusValue = DEFAULT_RADIUS;
        staticFrictionNum = DEFAULT_STATIC_FRICTION;
        kineticFrictionNum = DEFAULT_KINETIC_FRICTION;
        appliedForceNum = DEFAULT_APPLIED_FORCE;
        speedNum = DEFAULT_SPEED;
        angularVelocityNum = DEFAULT_ANGULAR_VELOCITY;
        // Update the object properties with default values
        updateObjectProperties();
        updateFrictionText();
        updateFrictionSlider();
        updateForceText();
        updateForceSlider();
    }
    

    private void changeSpeed() {
        double speedFactor = speedNum / MAX_SPEED;
        double angularFactor = angularVelocityNum / MAX_ANGULAR_VELOCITY;

        // Check if the animation is playing
        if ("▐▐".equals(pauseButton.getText()))  {
            // Update current speed only when the animation is playing
            if (speedNum < MAX_SPEED) {
                speedNum += accelerationNum * A;
            } else {
                accelerationNum = 0;
                appliedForceNum = 0;
            }

            // Update angular velocity only when the animation is playing
            if (angularVelocityNum < MAX_ANGULAR_VELOCITY) {
                angularVelocityNum += angularAccelerationNum * A;
            } else {
                angularAccelerationNum = 0;
                appliedForceNum = 0;
            }
        }

        // Update TranslateTransition rate
        translate.setRate(speedFactor);

        // Update RotateTransition rate
        rotate.setRate(angularFactor);

        updateObjectProperties();
    }

  
    public void initialize(URL arg0, ResourceBundle arg1) {
    	setUpSliders();
        setUpForceTextField();
    	showForceArrows(false);
        showSumOfForceArrow(false);
        showForceValues(false);
        showMassValues(false);
        showSpeedVBox(false);
        showAccelerationVBox(false);
        object.setImage(null);
        setUpAnimations();
        setEventHandlers();
        
        surface = new Surface(0, 0);

        // Create objects
        aCube = new CubeShapedObject(DEFAULT_MASS, 0, DEFAULT_SIDE_LENGTH);  // Add suitable initial values
        aCylinder = new CylinderShapedObject(DEFAULT_MASS, 0, DEFAULT_RADIUS, 0);  // Add suitable initial values

        // Set initial values for the object in the JavaFX application
        updateObjectProperties();
        
        updateTextValues();
        
        objectOriginalWidth = object.getFitWidth();
        objectOriginalHeight = object.getFitHeight();
        originalLayoutSum = sumOfForce.getLayoutX();
        
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(DELTA_TIME), event -> changeSpeed())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        initializeDefaults();
    }
    
    private void updateObjectProperties() {
        surface.setStaticFrictionCoefficient(staticFrictionNum);
        surface.setKineticFrictionCoefficient(kineticFrictionNum);

        if (current == CUBE) {
        	
        	aCube.setSideLength(sideLengthValue);
            aCube.setMass(objectMassValue);
            aCube.setAppliedForce(appliedForceNum);
            gravitationalForceNum = aCube.calculateGravitationalForce();
            normalForceNum = aCube.calculateNormalForce();
            cubeFrictionNum = aCube.calculateFrictionForce(surface);
            frictionNum = cubeFrictionNum;
            sumOfForceNum = aCube.compositeHForce(surface);
            accelerationNum = aCube.getAcceleration(surface);
            aCube.setVelocity(speedNum);
        } else if (current == CYLINDER) {
        	aCylinder.setRadius(radiusValue);
            aCylinder.setMass(objectMassValue);
            aCylinder.setAppliedForce(appliedForceNum);
            gravitationalForceNum = aCylinder.calculateGravitationalForce();
            normalForceNum = aCylinder.calculateNormalForce();
            cylinderFrictionNum = aCylinder.calculateFrictionForce(surface);
            frictionNum = cylinderFrictionNum;
            sumOfForceNum = aCylinder.compositeHForce(surface);
            accelerationNum = aCylinder.getAcceleration(surface);
            angularAccelerationNum = aCylinder.calculateAngularAcceleration(surface);
            aCylinder.setVelocity(speedNum);
            aCylinder.setAngularVelocity(angularVelocityNum);
        }
     // Check if checkboxes are ticked and the object is not null
        boolean objectNotNull = current != NONE;

        // Show/hide corresponding information based on each checkbox
        showForceArrows(forceButton.isSelected() && objectNotNull);
        showSumOfForceArrow(sumOfForceButton.isSelected() && objectNotNull);
        showForceValues(valueButton.isSelected() && objectNotNull);
        showMassValues(massButton.isSelected() && objectNotNull);
        showSpeedVBox(speedButton.isSelected() && objectNotNull);
        showAccelerationVBox(accelerationButton.isSelected() && objectNotNull);
        updateTextValues();
    }
    
    private void updateTextValues() {
        objectMass.setText(String.format("%.2f kg", objectMassValue));

        if (appliedForceNum > 0) {
            appliedForceValue.setText(String.format("%.2f N", appliedForceNum));
            frictionValue.setText(String.format("%.2f N", frictionNum));
        } else {
            appliedForceValue.setText(String.format("%.2f N", frictionNum));
            frictionValue.setText(String.format("%.2f N", appliedForceNum));
        }

        gravitationalForceValue.setText(String.format("%.2f N", gravitationalForceNum));
        normalForceValue.setText(String.format("%.2f N", normalForceNum));

        sumOfForceValue.setText(String.format("%.2f N", sumOfForceNum));
        speedValue.setText(String.format("%.2f m/s", speedNum));
        accelerationValue.setText(String.format("%.2f m/s^2", accelerationNum));

        updateArrow();
    }

    
    private void setUpSliders() {
        // Set up forceSlider
        forceSlider.setMin(-MAX_FORCE);
        forceSlider.setMax(MAX_FORCE);
        forceSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            appliedForceNum = newValue.doubleValue();
            updateObjectProperties();
            updateForceText();
        });

        // Set up kineticFrictionSlider
        kineticFrictionSlider.setMin(0);
        kineticFrictionSlider.setMax(1);
        kineticFrictionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            kineticFrictionNum = newValue.doubleValue();

            // Check if kinetic coefficient exceeds static coefficient
            if (kineticFrictionNum > staticFrictionNum) {
                staticFrictionNum = kineticFrictionNum;
                staticFrictionSlider.setValue(staticFrictionNum);
            }
            updateObjectProperties();
            updateFrictionText();
        });

        // Set up staticFrictionSlider
        staticFrictionSlider.setMin(0);
        staticFrictionSlider.setMax(1);
        staticFrictionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            staticFrictionNum = newValue.doubleValue();

            // Ensure static coefficient is always higher than or equal to kinetic coefficient
            if (staticFrictionNum < kineticFrictionNum) {
                staticFrictionNum = kineticFrictionNum;
                staticFrictionSlider.setValue(staticFrictionNum);
            }
            updateObjectProperties();
            updateFrictionText();
        });
    }


    private void updateFrictionText() {
        kineticFrictionTextField.setText(String.valueOf(kineticFrictionNum));
        staticFrictionTextField.setText(String.valueOf(staticFrictionNum));
    }

    private void updateFrictionSlider() {
        kineticFrictionSlider.setValue(kineticFrictionNum);
        staticFrictionSlider.setValue(staticFrictionNum);
    }

    private void setUpForceTextField() {
        forceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                appliedForceNum = Double.parseDouble(newValue);
                updateForceSlider();
                updateObjectProperties();
            } catch (NumberFormatException e) {
                // Handle invalid input (non-numeric value)
                showAlert("Invalid Input", "Please enter a valid numeric value for force.");
                forceTextField.setText(String.format("%.2f", appliedForceNum));  // Reset the text field to the previous valid value
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void updateForceText() {
        forceTextField.setText(String.format("%.2f", appliedForceNum));
        
    }

    private void updateForceSlider() {
        forceSlider.setValue(appliedForceNum);
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
        rotate.setCycleCount(TranslateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setByAngle(360);
    }

    private void setEventHandlers() {
        // Set up drag and drop for the Cube
        setDragAndDrop(cube);

        // Set up drag and drop for the Cylinder
        setDragAndDrop(cylinder);
        // Set up drag and drop for the object
        setDragAndDropObject(object);
        
        //Set up event for entering the parameters
        object.setOnMouseClicked(event -> showPopupMenu());
        
        // Set up event handler for the Pause button
        pauseButton.setOnAction(event -> toggleAnimations());

        // Set up event handler for the Replay button
        replayButton.setOnAction(event -> replayAnimations());

        // Set up event handler for the Force checkbox
        forceButton.setOnAction(event -> showForceArrows(forceButton.isSelected()));

        // Set up event handler for the SumOfForce checkbox
        sumOfForceButton.setOnAction(event -> showSumOfForceArrow(sumOfForceButton.isSelected()));

        // Set up event handler for Value checkbox
        valueButton.setOnAction(event -> showForceValues(valueButton.isSelected()));

        // Set up event handler for Mass checkbox
        massButton.setOnAction(event -> showMassValues(massButton.isSelected()));

        // Set up event handler for Speed checkbox
        speedButton.setOnAction(event -> showSpeedVBox(speedButton.isSelected()));

        // Set up event handler for Acceleration checkbox
        accelerationButton.setOnAction(event -> showAccelerationVBox(accelerationButton.isSelected()));
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
            objectSceneX = object.getLayoutX();
            objectSceneY = object.getLayoutY();
            double distanceToObject = Math.hypot(initialX - objectSceneX, initialY - objectSceneY);

            // Check if the image is closer to the Object's center or its original place
            if (distanceToObject < 100) {
                // If closer to the Object's center, replace Object with Cube or Cylinder
                if (image == cube) {

                    if (current == CYLINDER) {
                        cylinder.setImage(object.getImage());
                    }
                    object.setImage(cube.getImage());
                    rotate.pause();
                    object.setRotate(0);
                    current = CUBE;
                } else if (image == cylinder) {

                    if (current == CUBE) {
                        cube.setImage(object.getImage());
                    }
                    object.setImage(cylinder.getImage());
                    if (pauseButton.getText().equals("▐▐")) {
                        rotate.play();
                    }
                    current = CYLINDER;
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
            double distanceToCylinder = Math.hypot(initialX - cylinder.getLayoutX(), initialY - cylinder.getLayoutY());
            double distanceToCube = Math.hypot(initialX - cube.getLayoutX(), initialY - cube.getLayoutY());

            if (current == CUBE) {
                if (distanceToCylinder < 50 && cylinder.getImage() != null) {
                    cube.setImage(object.getImage());
                    image.setImage(cylinder.getImage());
                    current = CYLINDER;
                    cylinder.setImage(null);
                    if (pauseButton.getText().equals("▐▐")) {
                        rotate.play();
                    }

                } else if (distanceToCube < 50) {
                    cube.setImage(object.getImage());
                    image.setImage(null);
                    current = NONE;
                }
            } else if (current == CYLINDER) {
                if (distanceToCylinder < 50) {
                    cylinder.setImage(object.getImage());
                    image.setImage(null);
                    current = NONE;

                } else if (distanceToCube < 50 && cube.getImage() != null) {
                    cylinder.setImage(object.getImage());
                    image.setImage(cube.getImage());
                    cube.setImage(null);
                    rotate.pause();
                    object.setRotate(0);
                    current = CUBE;
                }
            }

            if (image.getImage() == null) {
                replayAnimations();
            }

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

        if (current == CUBE) {
            grid.add(new Label("Enter Length:"), 0, 0);
            grid.add(firstTextField, 1, 0);
            grid.add(new Label("Enter Mass:"), 0, 1);
            grid.add(secondTextField, 1, 1);
        } else if (current == CYLINDER) {
            grid.add(new Label("Enter Radius:"), 0, 0);
            grid.add(firstTextField, 1, 0);
            grid.add(new Label("Enter Mass:"), 0, 1);
            grid.add(secondTextField, 1, 1);
        }

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(result -> {
            double enteredFirstValue = Double.parseDouble(firstTextField.getText());
            double enteredSecondValue = Double.parseDouble(secondTextField.getText());

            // Check if the entered mass exceeds the maximum allowed value
            if (enteredSecondValue > MAX_WEIGHT) {
                showAlert("Invalid Input", "Mass must be less than or equal to " + MAX_WEIGHT);
                return;
            }

            // Handle the entered values based on the current state (current == CUBE or current == CYLINDER)
            if (current == CUBE) {
                // Handle values for current CUBE (Length and Mass)
                if (enteredFirstValue <= 0 || enteredFirstValue > MAX_SIDE_LENGTH) {
                    showAlert("Invalid Input", "Side length must be greater than 0 and less than or equal to " + MAX_SIDE_LENGTH);
                    return;
                }
                sideLengthValue = enteredFirstValue;
                objectMassValue = enteredSecondValue;
                gravitationalForceNum = objectMassValue * 10;
                normalForceNum = gravitationalForceNum;
                updateDimensions();  // Update cube dimensions

            } else if (current == CYLINDER) {
                // Handle values for current CYLINDER (Radius and Mass)
                if (enteredFirstValue <= 0 || enteredFirstValue > MAX_RADIUS) {
                    showAlert("Invalid Input", "Radius must be greater than 0 and less than or equal to " + MAX_RADIUS);
                    return;
                }
                radiusValue = enteredFirstValue;
                objectMassValue = enteredSecondValue;
                gravitationalForceNum = objectMassValue * 10;
                normalForceNum = gravitationalForceNum;
                updateDimensions();  // Update cylinder dimensions
            }
        });
        updateObjectProperties();
    }

    private void updateDimensions() {
        double scaleFactor = 1;

        if (current == CYLINDER) {
            scaleFactor = radiusValue / MAX_RADIUS;
        } else if (current == CUBE) {
            scaleFactor = sideLengthValue / MAX_SIDE_LENGTH;
        }

        double originalHeight = object.getFitHeight();
        double originalLayoutY = object.getLayoutY();

        object.setFitWidth(objectOriginalWidth * scaleFactor);
        object.setFitHeight(objectOriginalHeight * scaleFactor);

        // Adjust the Y-coordinate to keep the lowest point at the same height
        double newLayoutY = originalLayoutY + (originalHeight - object.getFitHeight());
        object.setLayoutY(newLayoutY);
    }

    private void updateArrow() {
        double scaleFactorA = Math.abs(4 * appliedForceNum / MAX_FORCE);
        double scaleFactorG = 3 * gravitationalForceNum / (MAX_WEIGHT * 10);
        double scaleFactorN = 3 * normalForceNum / (MAX_WEIGHT * 10);
        double scaleFactorF = Math.abs(4 * frictionNum / MAX_FORCE);
        double scaleFactorS = 4 * sumOfForceNum / MAX_FORCE;

        // Save the original layout X positions
        double originalLayoutXA = appliedForce.getLayoutX();
        double originalLayoutXF = friction.getLayoutX();
        
        

        // Save the original layout Y positions
        double originalLayoutYG = gravitationalForce.getLayoutY();
        double originalLayoutYN = normalForce.getLayoutY();

        double originalWidthA = appliedForce.getBoundsInParent().getWidth();
        double originalWidthF = friction.getBoundsInParent().getWidth();
        double originalHeightG = gravitationalForce.getBoundsInParent().getHeight();
        double originalHeightN = normalForce.getBoundsInParent().getHeight();
        double originalWidthS = sumOfForce.getBoundsInParent().getWidth();
       

        // Check if appliedForceNum is less than 0 and color swap has not occurred
        if (appliedForceNum < 0 && !swapOccurred) {
            // Swap colors of appliedForce and friction
            Color tempColor = (Color) appliedForce.getFill();
            appliedForce.setFill(friction.getFill());
            friction.setFill(tempColor);
            // Set the flag to true to indicate that the color swap has occurred
            swapOccurred = true;
            sumOfForce.setLayoutX(originalLayoutSum);
            
            
        } else if (appliedForceNum >= 0 && swapOccurred) {
            // Reset the color of appliedForce and friction
            Color tempColor = (Color) appliedForce.getFill();
            appliedForce.setFill(friction.getFill());
            friction.setFill(tempColor);
      
            // Set the flag to false to indicate that the color swap has been reset
            swapOccurred = false;
            sumOfForce.setLayoutX(originalLayoutSum);
        }
        double originalLayoutXS = sumOfForce.getLayoutX();

        // Swap scale factors always
        if (appliedForceNum < 0) {
            // Swap scale factors
            double tempScaleFactor = scaleFactorA;
            scaleFactorA = scaleFactorF;
            scaleFactorF = tempScaleFactor;
            swapOccurred = true;
        }

        // Apply the scale factor to the force arrows
        appliedForce.setScaleX(scaleFactorA);
        gravitationalForce.setScaleX(scaleFactorG);
        normalForce.setScaleX(scaleFactorN);
        friction.setScaleX(scaleFactorF);
        sumOfForce.setScaleX(scaleFactorS);
        

        // Calculate the translation based on the scale factor
        double translationXA = originalLayoutXA - (originalWidthA - appliedForce.getBoundsInParent().getWidth()) / 2;
        double translationXF = originalLayoutXF + (originalWidthF - friction.getBoundsInParent().getWidth()) / 2;
        double translationYG = originalLayoutYG + (originalHeightG - gravitationalForce.getBoundsInParent().getHeight()) / 2;
        double translationYN = originalLayoutYN - (originalHeightN - normalForce.getBoundsInParent().getHeight()) / 2;
        double translationXS = originalLayoutXS - (originalWidthS - sumOfForce.getBoundsInParent().getWidth()) / 2
                * Math.signum(appliedForceNum);


        // Apply the translation to the force arrows
        appliedForce.setLayoutX(translationXA);
        friction.setLayoutX(translationXF);
        sumOfForce.setLayoutX(translationXS);
        

        // Keep the specified Y-coordinates fixed
        gravitationalForce.setLayoutY(translationYG);
        normalForce.setLayoutY(translationYN);
    }

    private void showForceArrows(boolean show) {
    	
        boolean forceButtonSelected = forceButton.isSelected();
        boolean valueButtonSelected = valueButton.isSelected();

        // Update arrow visibility
        appliedForce.setVisible(show && forceButtonSelected && object.getImage() != null);
        gravitationalForce.setVisible(show && forceButtonSelected && object.getImage() != null);
        normalForce.setVisible(show && forceButtonSelected && object.getImage() != null);
        friction.setVisible(show && forceButtonSelected && object.getImage() != null);

        // Update text visibility
        appliedForceValue.setVisible(show && forceButtonSelected && valueButtonSelected && appliedForce.isVisible() && object.getImage() != null);
        gravitationalForceValue.setVisible(show && forceButtonSelected && valueButtonSelected && gravitationalForce.isVisible()&& object.getImage() != null);
        normalForceValue.setVisible(show && forceButtonSelected && valueButtonSelected && normalForce.isVisible()&& object.getImage() != null);
        frictionValue.setVisible(show && forceButtonSelected && valueButtonSelected && friction.isVisible()&& object.getImage() != null);
    }

    private void showSumOfForceArrow(boolean show) {
    	 
    	boolean sumOfForceButtonSelected = sumOfForceButton.isSelected();
        boolean valueButtonSelected = valueButton.isSelected();

        // Update arrow visibility
        sumOfForce.setVisible(show && sumOfForceButtonSelected && object.getImage() != null);
        
          
        // Update text visibility
        sumOfForceValue.setVisible(show && sumOfForceButtonSelected && valueButtonSelected && sumOfForce.isVisible()&& object.getImage() != null);
        
    }

    private void showForceValues(boolean show) {
        boolean forceButtonSelected = forceButton.isSelected();
        boolean sumOfForceButtonSelected = sumOfForceButton.isSelected();

        // Check if the Force button is selected
        if (forceButtonSelected) {
            // Show or hide values based on the corresponding arrow visibility
            appliedForceValue.setVisible(show && appliedForce.isVisible()&& object.getImage() != null);
            gravitationalForceValue.setVisible(show && gravitationalForce.isVisible()&& object.getImage() != null);
            normalForceValue.setVisible(show && normalForce.isVisible()&& object.getImage() != null);
            frictionValue.setVisible(show && friction.isVisible()&& object.getImage() != null);
        } else {
            // Hide all values if the Force button is not selected
            appliedForceValue.setVisible(false);
            gravitationalForceValue.setVisible(false);
            normalForceValue.setVisible(false);
            frictionValue.setVisible(false);
        }

        // Check if the SumOfForce button is selected and show or hide the SumOfForceValue based on the visibility of the arrow
        sumOfForceValue.setVisible(show && sumOfForceButtonSelected && sumOfForce.isVisible());
    }

    private void showMassValues(boolean show) {
        objectMass.setVisible(show&& object.getImage() != null);
    }

    private void showSpeedVBox(boolean show) {
        speed.setVisible(show&& object.getImage() != null);
    }

    private void showAccelerationVBox(boolean show) {
        acceleration.setVisible(show&& object.getImage() != null);
    }

    private void playAnimations() {
    	updateObjectProperties();
        if (object.getImage() != null) {
            translate.play();
            pauseButton.setText("▐▐");
        }


        if (cylinder.getImage() == null) {
            rotate.play();
        }
    }

    private void pauseAnimations() {
        translate.pause();
        rotate.pause();
        pauseButton.setText("▶"); // Set button text to Play
    }

    private void replayAnimations() {
    	initializeDefaults();
        translate.stop(); // Stop the current animation

        if (cylinder.getImage() == null) {
            rotate.stop(); // Stop the rotation animation only if the Cylinder image is null
        }

        road.setTranslateX(0);
        object.setRotate(0);

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
