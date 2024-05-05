package ija.project.robot;

import ija.project.robot.gui.App;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The main class for the robot simulation application.
 * This class initializes the logging system and launches the application.
 */
public class RobotApp {

    /**
     * Global logger for the application. Configured from a properties file.
     */
    public static final Logger logger = Logger.getAnonymousLogger();

    /**
     * The main method that starts the entire application.
     * It initializes the logging configuration and starts the GUI application.
     *
     * @param args Command-line arguments passed to the application. Currently not used.
     */
    public static void main(String[] args) {
        try {
            // Load logging configuration from a properties file.
            LogManager.getLogManager().readConfiguration(RobotApp.class.getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            logger.warning("Could not load logging configuration");
        }
        logger.info("Starting application");
        App.run();
    }
}