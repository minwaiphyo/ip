package snowy;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Snowy using FXML.
 */
public class Main extends Application {

    private final Snowy snowy = new Snowy();


    /**
     * Starts the JavaFX application by setting up the primary stage.
     * Loads the MainWindow FXML layout, creates the scene, sets the stage title,
     * injects the Snowy instance into the controller, and displays the stage.
     *
     * @param stage The primary stage for this application, provided by the JavaFX platform.
     */
    @Override
    public void start(Stage stage) {
        try {
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Snowy");
            fxmlLoader.<MainWindow>getController().setSnowy(snowy);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
