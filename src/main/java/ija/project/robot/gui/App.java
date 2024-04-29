package ija.project.robot.gui;

import ija.project.robot.HelloApplication;
import ija.project.robot.gui.controllers.Start;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class App extends Application {

    private static final Logger logger = Logger.getAnonymousLogger();
    @Override
    public void start(Stage stage) throws Exception {
        setIcon(stage);
        stage.setTitle("Robot simulation");
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
