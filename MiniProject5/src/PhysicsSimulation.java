import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PhysicsSimulation extends Application {

	@Override
	public void start(Stage stage) throws Exception {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainView.fxml"));
	    Parent root = loader.load();

	    Scene scene = new Scene(root);
	    stage.setTitle("Physics Simulation");
	    stage.setScene(scene);

	    // Make the window not resizable
	    stage.setResizable(false);

	    stage.show();
	}


    public static void main(String[] args) {
        launch(args);
    }
}
