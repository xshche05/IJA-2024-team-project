/*
 * Author: Kirill Shchetiniuk (xshche05), Artur Sultanov (xsulta01)
 * Description: This file provides the logic for the dialog windows in the application
 * and shared dialog buttons logic for handling user actions.
 */
package ija.project.robot.gui.interfaces;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import static ija.project.robot.RobotApp.logger;

/**
 * An abstract class representing a dialog window in the application.
 * This class provides a common interface for dialog windows, including OK and Cancel actions.
 */
public abstract class Dialog implements SceneInterface {
    /** OK button. */
    @FXML public Button okButton;
    /** Cancel button. */
    @FXML public Button cancelButton;
    /** Error message label. */
    @FXML public Label errorLabel;

    /**
     * Handles the OK action it dialog window.
     */
    abstract public void Ok();

    /**
     * Handles the Cancel action which closes the dialog window.
     */
    public void Cancel() {
        logger.info("Dialog window closed");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
