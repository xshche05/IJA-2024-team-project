package ija.project.robot;

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
        Image image = new Image(iconStream);
        stage.getIcons().add(image);


        LOG.log(System.Logger.Level.INFO, "Loading FXML file");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        LOG.log(System.Logger.Level.INFO, "Setting stage");

        stage.setTitle("Hello!");
        stage.setScene(scene);
        LOG.log(System.Logger.Level.INFO, "Showing stage");
        stage.show();
    }

    public static void main(String[] args) {
        LOG.log(System.Logger.Level.INFO, "Launching application");
        launch();
    }
}