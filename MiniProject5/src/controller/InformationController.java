package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import model.object.CubeShapedObject;
import model.object.CylinderShapedObject;
import model.object.MainObject;
import model.MovingObject;
import model.Surface;

public class InformationController implements Initializable, MovingObject.Observer,Surface.Observer{
    
	private MovingObject aMovingObject = new MovingObject(new CubeShapedObject(0, 0, 0));
	private Surface surface = new Surface(0.3,0.5);
	
    
    private boolean swapOccurred = false;

    @FXML
    private BorderPane Container;

    @FXML
    private VBox acceleration;

    @FXML
    private CheckBox accelerationButton;

    @FXML
    private Text accelerationValue;

    @FXML
    private Polygon appliedForce;

    @FXML
    private Text appliedForceValue;

    @FXML
    private CheckBox forceButton;

    @FXML
    private Polygon friction;

    @FXML
    private Text frictionValue;

    @FXML
    private Polygon gravitationalForce;

    @FXML
    private Text gravitationalForceValue;

    @FXML
    private CheckBox massButton;

    @FXML
    private Polygon normalForce;

    @FXML
    private Text normalForceValue;

    @FXML
    private Text objectMass;

    @FXML
    private VBox speed;

    @FXML
    private CheckBox speedButton;

    @FXML
    private Text speedValue;

    @FXML
    private Polygon sumOfForce;

    @FXML
    private CheckBox sumOfForceButton;

    @FXML
    private Text sumOfForceValue;

    @FXML
    private CheckBox valueButton;
    
    @FXML
    private Polygon sumOfForceNegative;

    @FXML
    private Text sumOfForceValueNegative;
    
    public void setData(MovingObject aMovingObject, Surface surface) {
    
       this.aMovingObject = aMovingObject;
       this.surface = surface;
       updateTextValues();
       this.aMovingObject.addObserver(this);
       this.surface.addObserver(this);
    }
    
    public void initialize(URL arg0, ResourceBundle arg1) {
    	showForceArrows(false);
        showSumOfForceArrow(false);
        showForceValues(false);
        showMassValues(false);
        showSpeedVBox(false);
        showAccelerationVBox(false);
        setEventHandlers();
    }
    
    
    private void setEventHandlers() {

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

    
    public void updateTextValues() {
        objectMass.setText(String.format("%.2f kg", aMovingObject.getCurrentObject().getMass()));

        if (aMovingObject.getAppliedForce() > 0) {
            appliedForceValue.setText(String.format("%.2f N", aMovingObject.getAppliedForce()));
            frictionValue.setText(String.format("%.2f N", aMovingObject.getCurrentObject().calculateFrictionForce(surface)));
        } else {
            appliedForceValue.setText(String.format("%.2f N", aMovingObject.getCurrentObject().calculateFrictionForce(surface)));
            frictionValue.setText(String.format("%.2f N", aMovingObject.getAppliedForce()));
        }

        gravitationalForceValue.setText(String.format("%.2f N", aMovingObject.getCurrentObject().calculateGravitationalForce()));
        normalForceValue.setText(String.format("%.2f N", aMovingObject.getCurrentObject().calculateNormalForce()));

        sumOfForceValue.setText(String.format("%.2f N", aMovingObject.getCurrentObject().compositeHForce(surface)));
        sumOfForceValueNegative.setText(String.format("%.2f N", aMovingObject.getCurrentObject().compositeHForce(surface)));
        speedValue.setText(String.format("%.2f m/s", aMovingObject.getCurrentObject().getVelocity()));
        accelerationValue.setText(String.format("%.2f m/s^2", aMovingObject.getCurrentObject().getAcceleration(surface)));

        updateArrow();
    }
    
private void showForceArrows(boolean show) {
    	
        boolean forceButtonSelected = forceButton.isSelected();
        boolean valueButtonSelected = valueButton.isSelected();

        // Update arrow visibility
        appliedForce.setVisible(show && forceButtonSelected && aMovingObject.getCurrentObject().getMass() != 0);
        gravitationalForce.setVisible(show && forceButtonSelected && aMovingObject.getCurrentObject().getMass() != 0);
        normalForce.setVisible(show && forceButtonSelected && aMovingObject.getCurrentObject().getMass() != 0);
        friction.setVisible(show && forceButtonSelected && aMovingObject.getCurrentObject().getMass() != 0);

        // Update text visibility
        appliedForceValue.setVisible(show && forceButtonSelected && valueButtonSelected && appliedForce.isVisible() && aMovingObject.getCurrentObject().getMass() != 0);
        gravitationalForceValue.setVisible(show && forceButtonSelected && valueButtonSelected && gravitationalForce.isVisible()&& aMovingObject.getCurrentObject().getMass() != 0);
        normalForceValue.setVisible(show && forceButtonSelected && valueButtonSelected && normalForce.isVisible()&& aMovingObject.getCurrentObject().getMass() != 0);
        frictionValue.setVisible(show && forceButtonSelected && valueButtonSelected && friction.isVisible()&& aMovingObject.getCurrentObject().getMass() != 0);
    }

    private void showSumOfForceArrow(boolean show) {
    	 
    	boolean sumOfForceButtonSelected = sumOfForceButton.isSelected();
        boolean valueButtonSelected = valueButton.isSelected();

        // Update arrow visibility
        sumOfForce.setVisible(show && sumOfForceButtonSelected && aMovingObject.getCurrentObject().getMass() != 0
        		&& !swapOccurred);
        
          
        // Update text visibility
        sumOfForceValue.setVisible(show && sumOfForceButtonSelected && valueButtonSelected && sumOfForce.isVisible()&& aMovingObject.getCurrentObject().getMass() != 0
        		&& !swapOccurred);
        
        // Update arrow visibility
        sumOfForceNegative.setVisible(show && sumOfForceButtonSelected && aMovingObject.getCurrentObject().getMass() != 0
        		&& swapOccurred);
        
          
        // Update text visibility
        sumOfForceValueNegative.setVisible(show && sumOfForceButtonSelected && valueButtonSelected && sumOfForceNegative.isVisible()&& aMovingObject.getCurrentObject().getMass() != 0
        		&& swapOccurred);
        
    }

    private void showForceValues(boolean show) {
        boolean forceButtonSelected = forceButton.isSelected();
        boolean sumOfForceButtonSelected = sumOfForceButton.isSelected();

        // Check if the Force button is selected
        if (forceButtonSelected) {
            // Show or hide values based on the corresponding arrow visibility
            appliedForceValue.setVisible(show && appliedForce.isVisible()&& aMovingObject.getCurrentObject().getMass() != 0);
            gravitationalForceValue.setVisible(show && gravitationalForce.isVisible()&& aMovingObject.getCurrentObject().getMass() != 0);
            normalForceValue.setVisible(show && normalForce.isVisible()&& aMovingObject.getCurrentObject().getMass() != 0);
            frictionValue.setVisible(show && friction.isVisible()&& aMovingObject.getCurrentObject().getMass() != 0);
        } else {
            // Hide all values if the Force button is not selected
            appliedForceValue.setVisible(false);
            gravitationalForceValue.setVisible(false);
            normalForceValue.setVisible(false);
            frictionValue.setVisible(false);
        }

        // Check if the SumOfForce button is selected and show or hide the SumOfForceValue based on the visibility of the arrow
        sumOfForceValue.setVisible(show && sumOfForceButtonSelected && sumOfForce.isVisible());
        sumOfForceValueNegative.setVisible(show && sumOfForceButtonSelected && sumOfForceNegative.isVisible());
    }

    private void showMassValues(boolean show) {
        objectMass.setVisible(show&& aMovingObject.getCurrentObject().getMass() != 0);
    }

    private void showSpeedVBox(boolean show) {
        speed.setVisible(show&& aMovingObject.getCurrentObject().getMass() != 0);
    }

    private void showAccelerationVBox(boolean show) {
        acceleration.setVisible(show&& aMovingObject.getCurrentObject().getMass() != 0);
    }
    
    private void updateArrow() {
        double scaleFactorA = Math.abs(4 * aMovingObject.getAppliedForce() / aMovingObject.getMaxForce());
        double scaleFactorG = 3 * aMovingObject.getCurrentObject().calculateGravitationalForce() / (aMovingObject.getCurrentObject().getMaxWeight() * 10);
        double scaleFactorN = 3 * aMovingObject.getCurrentObject().calculateNormalForce() / (aMovingObject.getCurrentObject().getMaxWeight() * 10);
        double scaleFactorF = Math.abs(4 * aMovingObject.getCurrentObject().calculateFrictionForce(surface) / aMovingObject.getMaxForce());
        double scaleFactorS = Math.abs(4 * aMovingObject.getCurrentObject().compositeHForce(surface) / aMovingObject.getMaxForce());

        // Save the original layout X positions
        double originalLayoutXA = appliedForce.getLayoutX();
        double originalLayoutXF = friction.getLayoutX();
        double originalLayoutXS = sumOfForce.getLayoutX();
        double originalLayoutXSN = sumOfForceNegative.getLayoutX();
        

        // Save the original layout Y positions
        double originalLayoutYG = gravitationalForce.getLayoutY();
        double originalLayoutYN = normalForce.getLayoutY();

        double originalWidthA = appliedForce.getBoundsInParent().getWidth();
        double originalWidthF = friction.getBoundsInParent().getWidth();
        double originalHeightG = gravitationalForce.getBoundsInParent().getHeight();
        double originalHeightN = normalForce.getBoundsInParent().getHeight();
        double originalWidthS = sumOfForce.getBoundsInParent().getWidth();
        double originalWidthSN = sumOfForceNegative.getBoundsInParent().getWidth();
       

        // Check if appliedForceNum is less than 0 and color swap has not occurred
        if (aMovingObject.getCurrentObject().getAppliedForce() < 0 && !swapOccurred) {
            // Swap colors of appliedForce and friction
            Color tempColor = (Color) appliedForce.getFill();
            appliedForce.setFill(friction.getFill());
            friction.setFill(tempColor);
            // Set the flag to true to indicate that the color swap has occurred
            swapOccurred = true;
            if (sumOfForce.isVisible())
            {
            	sumOfForceNegative.setVisible(true);
            	sumOfForce.setVisible(false);
            }
            if (sumOfForceValue.isVisible())
            {
            	sumOfForceValueNegative.setVisible(true);
            	sumOfForceValue.setVisible(false);
            }
            
            
        } else if (aMovingObject.getCurrentObject().getAppliedForce() >= 0 && swapOccurred) {
            // Reset the color of appliedForce and friction
            Color tempColor = (Color) appliedForce.getFill();
            appliedForce.setFill(friction.getFill());
            friction.setFill(tempColor);
      
            // Set the flag to false to indicate that the color swap has been reset
            swapOccurred = false;
            if (sumOfForceNegative.isVisible())
            {
            	sumOfForce.setVisible(true);
            	sumOfForceNegative.setVisible(false);
            }
            if (sumOfForceValueNegative.isVisible())
            {
            	sumOfForceValue.setVisible(true);
            	sumOfForceValueNegative.setVisible(false);
            }
        }
       

        // Swap scale factors always
        if (aMovingObject.getCurrentObject().getAppliedForce() < 0) {
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
        sumOfForceNegative.setScaleX(scaleFactorS);
        

        // Calculate the translation based on the scale factor
        double translationXA = originalLayoutXA - (originalWidthA - appliedForce.getBoundsInParent().getWidth()) / 2;
        double translationXF = originalLayoutXF + (originalWidthF - friction.getBoundsInParent().getWidth()) / 2;
        double translationYG = originalLayoutYG + (originalHeightG - gravitationalForce.getBoundsInParent().getHeight()) / 2;
        double translationYN = originalLayoutYN - (originalHeightN - normalForce.getBoundsInParent().getHeight()) / 2;
        double translationXS = originalLayoutXS - (originalWidthS - sumOfForce.getBoundsInParent().getWidth()) / 2;
        double translationXSN = originalLayoutXSN + (originalWidthSN - sumOfForceNegative.getBoundsInParent().getWidth()) / 2;  


        // Apply the translation to the force arrows
        appliedForce.setLayoutX(translationXA);
        friction.setLayoutX(translationXF);
        sumOfForce.setLayoutX(translationXS);
        sumOfForceNegative.setLayoutX(translationXSN);
        

        // Keep the specified Y-coordinates fixed
        gravitationalForce.setLayoutY(translationYG);
        normalForce.setLayoutY(translationYN);
    }

	@Override
	public void update() {
		updateTextValues();
        updateArrow();
		
	}
	
	@Override
	public void updateSurface() {
		updateTextValues();
        updateArrow();
		
	}

}
