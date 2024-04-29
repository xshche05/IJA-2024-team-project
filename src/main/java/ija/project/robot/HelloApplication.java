package ija.project.robot;

import ija.project.robot.gui.App;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.robots.AutomatedRobot;
import ija.project.robot.logic.room.Room;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class HelloApplication {

    private static final Logger logger = Logger.getLogger(HelloApplication.class.getName());

//    @Override
//    public void start(Stage stage) throws IOException {
//        InputStream iconStream = getClass().getResourceAsStream("icon.png");
//        if (iconStream != null) {
//            Image image = new Image(iconStream);
//            stage.getIcons().add(image);
//            logger.info("Icon loaded");
//        } else {
//            logger.warning("Icon not found");
//        }
//        logger.info("Loading FXML file");
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
//        logger.info("Setting stage");
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        logger.info("Showing stage");
//        stage.show();
//    }

    public static void main(String[] args) throws InterruptedException, IOException {
        LogManager.getLogManager().readConfiguration(HelloApplication.class.getResourceAsStream("logging.properties"));
        logger.info("Starting application");
        App.run(args);
    }
}