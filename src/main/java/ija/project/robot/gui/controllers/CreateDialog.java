package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.Dialog;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.logic.room.Room;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static ija.project.robot.RobotApp.logger;

/**
 * Controller class for the room creation dialog.
 * This class handles the input the dimensions of a room.
 * It provides methods to react to user actions (e.g., clicking OK or Cancel).
 *
 * The user must enter positive integers for both rows and columns to proceed.
 * Error messages are displayed if the inputs are invalid.
 */
public class CreateDialog extends Dialog {
    @FXML
    public TextField rowsInput;
    @FXML
    public TextField colsInput;
    @FXML
    public Button okButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Label errorLabel;

    /**
     * Invoked when the OK button is clicked.
     * This method sets the dimensions of the room based on the user input.
     * If the input is invalid, an error message is displayed.
     */
    public void Ok() {
        errorLabel.setText("");
        try {
            int rows = Integer.parseInt(rowsInput.getText());
            int cols = Integer.parseInt(colsInput.getText());
            if (rows < 1 || cols < 1) {
                errorLabel.setText("Rows and cols must be positive!");
                logger.warning("Rows and cols must be positive!");
                return;
            }
            logger.info("Setting room dimensions to " + cols + "x" + rows);
            Room.getInstance().setDimensions(cols, rows);
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
            Stage owner = (Stage) stage.getOwner();
            Scene playGround = Playground.getScene();
            owner.setScene(playGround);
            owner.show();
        } catch (NumberFormatException e) {
            errorLabel.setText("Rows and cols be positive integers!");
            logger.warning("Rows and cols be positive integers!");
        }
    }

    /**
     * Retrieves and returns the scene for the room creation dialog using the {@link SceneInterface}.
     * This method facilitates the loading of the appropriate FXML file for the room creation dialog and
     * is typically called to initialize the dialog in the UI.
     *
     * @return The loaded {@link Scene} for the room creation dialog.
     */
    public static Scene getScene() {
        return SceneInterface.getScene(CreateDialog.class, "create_dialog.fxml");
    }
}
