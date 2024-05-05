package ija.project.robot.gui;

import ija.project.robot.gui.controllers.Start;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.InputStream;
import java.util.logging.Logger;

public class App extends Application {

    private static final Logger logger = Logger.getAnonymousLogger();
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

    public static void run(String[] args) {
        launch();
    }
}
