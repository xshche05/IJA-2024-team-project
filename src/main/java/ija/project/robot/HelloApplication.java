package ija.project.robot;

import ija.project.robot.common.Position;
import ija.project.robot.robots.AutomatedRobot;
import ija.project.robot.room.Room;
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

public class HelloApplication extends Application {

    private static final Logger logger = Logger.getLogger(HelloApplication.class.getName());

    @Override
    public void start(Stage stage) throws IOException {
        InputStream iconStream = getClass().getResourceAsStream("icon.png");
        if (iconStream != null) {
            Image image = new Image(iconStream);
            stage.getIcons().add(image);
            logger.info("Icon loaded");
        } else {
            logger.warning("Icon not found");
        }
        logger.info("Loading FXML file");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        logger.info("Setting stage");
        stage.setTitle("Hello!");
        stage.setScene(scene);
        logger.info("Showing stage");
        stage.show();
    }

    public static void setUpRoom() throws InterruptedException {
        logger.info("Setting up room");
        Room room = Room.getInstance();
        room.setDimensions(10, 10);
        AutomatedRobot r = room.addAutoRobot(new Position(4, 4));
        r.rotate();
        r.rotate();
        r.rotate();
        room.addObstacle(new Position(0, 0));
        room.addObstacle(new Position(1, 1));

        room.addObstacle(new Position(9, 0));
        room.addObstacle(new Position(8, 1));

        room.addObstacle(new Position(0, 9));
        room.addObstacle(new Position(1, 8));

        room.addObstacle(new Position(9, 9));
        room.addObstacle(new Position(8, 8));

        room.addAutoRobot(new Position(5, 5));
        logger.log(Level.OFF, "\n" + room);
        Room.getInstance().runAutomatedRobots();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        LogManager.getLogManager().readConfiguration(HelloApplication.class.getResourceAsStream("logging.properties"));
        logger.info("Starting application");
        setUpRoom();
        launch();
    }
}