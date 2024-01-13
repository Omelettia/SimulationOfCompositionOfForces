package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import model.MovingObject;
import model.object.CubeShapedObject;
import model.object.CylinderShapedObject;
import model.object.MainObject;
import model.Surface;

public class ForceAndFrictionController implements Initializable, MovingObject.Observer{
	
	
	private MovingObject aMovingObject = new MovingObject() ;
	private Surface surface = new Surface(0,0);

    @FXML
    private BorderPane Container;

    @FXML
    private Slider forceSlider;

    @FXML
    private TextField forceTextField;

    @FXML
    private Slider kineticFrictionSlider;

    @FXML
    private TextField kineticFrictionTextField;

    @FXML
    private Slider staticFrictionSlider;

    @FXML
    private TextField staticFrictionTextField;
    
    public void setData(MovingObject aMovingObject) {
        this.aMovingObject = aMovingObject;
        this.aMovingObject.addObserver(this);// Register this controller as an observer
        updateForceText();
     }
    
    
    public void initialize(URL arg0, ResourceBundle arg1) {
    	setUpSliders();
        setUpForceTextField();
        setUpKineticFrictionTextField();  
        setUpStaticFrictionTextField();  
        updateFrictionText();
        updateFrictionSlider();
    }
    
    private void setUpSliders() {
        // Set up forceSlider
        forceSlider.setMin(-aMovingObject.getMaxForce());
        forceSlider.setMax(aMovingObject.getMaxForce());
        forceSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
        	aMovingObject.setAppliedForce(newValue.doubleValue());
            updateForceText();
        });

        // Set up kineticFrictionSlider
        kineticFrictionSlider.setMin(0);
        kineticFrictionSlider.setMax(1);
        kineticFrictionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            surface.setKineticFrictionCoefficient(newValue.doubleValue());

            // Check if kinetic coefficient exceeds static coefficient
            if (surface.getKineticFrictionCoefficient() > surface.getStaticFrictionCoefficient()) {
                surface.setStaticFrictionCoefficient(surface.getKineticFrictionCoefficient());
                staticFrictionSlider.setValue( surface.getStaticFrictionCoefficient());             
            }
            updateFrictionText();
        });

        // Set up staticFrictionSlider
        staticFrictionSlider.setMin(0);
        staticFrictionSlider.setMax(1);
        staticFrictionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
        	surface.setStaticFrictionCoefficient(newValue.doubleValue());

            // Ensure static coefficient is always higher than or equal to kinetic coefficient
            if (surface.getKineticFrictionCoefficient() > surface.getStaticFrictionCoefficient()) {
            	surface.setStaticFrictionCoefficient(surface.getKineticFrictionCoefficient());
                staticFrictionSlider.setValue( surface.getStaticFrictionCoefficient());
            }
            updateFrictionText();
            
        });
    }


    private void updateFrictionText() {
    	kineticFrictionTextField.setText(String.format("%.2f", surface.getKineticFrictionCoefficient()));
    	staticFrictionTextField.setText(String.format("%.2f", surface.getStaticFrictionCoefficient()));
    }

    private void updateFrictionSlider() {
        kineticFrictionSlider.setValue(surface.getKineticFrictionCoefficient());
        staticFrictionSlider.setValue(surface.getStaticFrictionCoefficient());
    }

    private void setUpForceTextField() {
        forceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
            	aMovingObject.setAppliedForce(Double.parseDouble(newValue));
                updateForceSlider();
            } catch (NumberFormatException e) {
                // Handle invalid input (non-numeric value)
                AlertHelper.showAlert("Invalid Input", "Please enter a valid numeric value for force.");
                forceTextField.setText(String.format("%.2f", aMovingObject.getAppliedForce()));  // Reset the text field to the previous valid value
            }
        });
    }
    
    private void updateForceText() {
        forceTextField.setText(String.format("%.2f",aMovingObject.getAppliedForce()));
        
    }

    private void updateForceSlider() {
        forceSlider.setValue(aMovingObject.getAppliedForce());
    }
    
    private void setUpKineticFrictionTextField() {
        kineticFrictionTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                surface.setKineticFrictionCoefficient(Double.parseDouble(newValue));
                updateFrictionSlider();
            } catch (NumberFormatException e) {
                // Handle invalid input (non-numeric value)
                AlertHelper.showAlert("Invalid Input", "Please enter a valid numeric value for kinetic friction.");
                kineticFrictionTextField.setText(String.format("%.2f", surface.getKineticFrictionCoefficient()));
            }
        });
    }

    private void setUpStaticFrictionTextField() {
        staticFrictionTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                surface.setStaticFrictionCoefficient(Double.parseDouble(newValue));

                // Ensure static coefficient is always higher than or equal to kinetic coefficient
                if (surface.getKineticFrictionCoefficient() > surface.getStaticFrictionCoefficient()) {
                    surface.setStaticFrictionCoefficient(surface.getKineticFrictionCoefficient());
                    staticFrictionTextField.setText(String.format("%.2f", surface.getStaticFrictionCoefficient()));
                }

                updateFrictionSlider();
            } catch (NumberFormatException e) {
                // Handle invalid input (non-numeric value)
                AlertHelper.showAlert("Invalid Input", "Please enter a valid numeric value for static friction.");
                staticFrictionTextField.setText(String.format("%.2f", surface.getStaticFrictionCoefficient()));
            }
        });
    }
    
    public Surface getSurface() {
        return surface;
    }
    @Override
    public void update() {
        // Update the view in response to changes in the MovingObject
        updateForceText();
        updateForceSlider();
    }
}
