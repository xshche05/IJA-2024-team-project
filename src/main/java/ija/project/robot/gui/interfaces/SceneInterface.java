package ija.project.robot.gui.interfaces;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Provides a utility method to load JavaFX scenes from FXML files.
 * This interface is meant to be implemented by controllers that need to dynamically load scenes.
 */
public interface SceneInterface {
    /**
     * Loads a JavaFX {@link Scene} from an FXML file associated with a specific class.
     *
     * @param sceneClass The class related to the FXML file. Typically, this class will be the controller class.
     * @param fxml The path to the FXML file relative to {@code sceneClass}.
     * @return A new {@link Scene} object if the FXML file is successfully loaded; otherwise, {@code null}.
     */
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
