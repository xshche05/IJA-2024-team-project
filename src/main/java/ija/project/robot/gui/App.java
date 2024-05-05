package ija.project.robot.gui;

import ija.project.robot.gui.controllers.Start;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Main entry point for the Robot Simulation application.
 * This class sets up the main window and initializes the application.
 */
public class App extends Application {

    private static final Logger logger = Logger.getAnonymousLogger();

    /**
     * Starts and sets up the primary stage for the application.
     * This method is called when the application launches and is used to initialize
     * the main user interface including setting the scene, title, minimum size, and icons.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage stage) {
        setIcon(stage);
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setTitle("Robot simulation");
        stage.setMinHeight(200);
        stage.setMinWidth(300);
        Scene scene = Start.getScene();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the icon for the application window.
     * This method attempts to load an image file from the resources to set it as the icon of the provided stage.
     *
     * @param stage The stage for which to set the icon.
     */
    public void setIcon(Stage stage) {
        InputStream iconStream = getClass().getResourceAsStream("icon.png");
        if (iconStream != null) {
            Image image = new Image(iconStream);
            stage.getIcons().add(image);
            logger.info("Icon loaded");
        } else {
            logger.warning("Icon not found");
        }
    }

    /**
     * Launches the application.
     * This method is the main entry point for the application and is called by the main method.
     */
    public static void run() {
        launch();
    }
}
