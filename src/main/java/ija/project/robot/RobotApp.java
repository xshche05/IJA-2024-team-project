package ija.project.robot;

import ija.project.robot.gui.App;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RobotApp {

    private static final Logger logger = Logger.getAnonymousLogger();

    public static void main(String[] args) throws InterruptedException, IOException {
        LogManager.getLogManager().readConfiguration(RobotApp.class.getResourceAsStream("logging.properties"));
        logger.info("Starting application");
        App.run(args);
    }
}