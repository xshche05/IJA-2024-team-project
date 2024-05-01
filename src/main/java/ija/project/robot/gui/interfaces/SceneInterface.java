package ija.project.robot.gui.interfaces;

import ija.project.robot.gui.controllers.Start;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.logging.Logger;

public interface SceneInterface {
    static Scene getScene(Class<?> sceneClass, String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(sceneClass.getResource(fxml));
        try {
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            Logger.getAnonymousLogger().warning(e.getMessage());
        }
        return null;
    }
}
