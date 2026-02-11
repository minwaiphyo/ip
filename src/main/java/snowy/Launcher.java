package snowy;

import javafx.application.Application;

/**
 * Serves as the entry point for the Snowy application.
 * This launcher class is used to workaround classpath issues that arise when
 * using JavaFX with certain build tools. It delegates the actual application
 * launching to the Main class.
 */
public class Launcher {
    /**
     * Launches the JavaFX application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
