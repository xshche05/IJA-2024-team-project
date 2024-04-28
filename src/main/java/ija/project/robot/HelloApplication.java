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

public class HelloApplication extends Application {

    private static final System.Logger LOG = System.getLogger(HelloApplication.class.getName());

    @Override
    public void start(Stage stage) throws IOException {
        InputStream iconStream = getClass().getResourceAsStream("icon.png");
        if (iconStream != null) {
            Image image = new Image(iconStream);
            stage.getIcons().add(image);
            LOG.log(System.Logger.Level.INFO, "Icon loaded");
        } else {
            LOG.log(System.Logger.Level.WARNING, "Icon not found");
        }
        LOG.log(System.Logger.Level.INFO, "Loading FXML file");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        LOG.log(System.Logger.Level.INFO, "Setting stage");
        stage.setTitle("Hello!");
        stage.setScene(scene);
        LOG.log(System.Logger.Level.INFO, "Showing stage");
        stage.show();
    }

    public static void setUpRoom() throws InterruptedException {
        LOG.log(System.Logger.Level.INFO, "Setting up room");
        Room room = Room.getInstance();
        room.setDimensions(10, 10);
        AutomatedRobot r = room.addAutoRobot(new Position(4, 4));
        r.rotate();
        r.rotate();
        r.rotate();
        r.setSpeed(2);
        room.addObstacle(new Position(0, 0));
        room.addObstacle(new Position(1, 1));

        room.addObstacle(new Position(9, 0));
        room.addObstacle(new Position(8, 1));

        room.addObstacle(new Position(0, 9));
        room.addObstacle(new Position(1, 8));

        room.addObstacle(new Position(9, 9));
        room.addObstacle(new Position(8, 8));

        room.addAutoRobot(new Position(5, 5));
        System.out.println(room + "\n");
        Room.getInstance().runAutomatedRobots();
    }

    public static void main(String[] args) throws InterruptedException {
        LOG.log(System.Logger.Level.INFO, "Launching application");
        setUpRoom();
        launch();
    }
}