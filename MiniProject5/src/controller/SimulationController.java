package controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import model.MovingObject;
import model.object.MainObject;

import java.net.URL;
import java.util.ResourceBundle;

public class SimulationController implements Initializable {

	
	 @FXML private ObjectController ObjectViewController;
	 @FXML private InformationController InformationViewController;
	 @FXML private ForceAndFrictionController ForceAndFrictionViewController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
          //Passing objects between controllers
        MovingObject movingObject = ObjectViewController.getMovingObject();
        InformationViewController.setData(movingObject, ForceAndFrictionViewController.getSurface());
        ForceAndFrictionViewController.setData(movingObject);
        ObjectViewController.setData(ForceAndFrictionViewController.getSurface());
    }
}
