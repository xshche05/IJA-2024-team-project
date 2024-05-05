package ija.project.robot;

import ija.project.robot.gui.App;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RobotApp {

    public static final Logger logger = Logger.getAnonymousLogger();

    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(RobotApp.class.getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            logger.warning("Could not load logging configuration");
        }
        logger.info("Starting application");
        App.run();
    }
}