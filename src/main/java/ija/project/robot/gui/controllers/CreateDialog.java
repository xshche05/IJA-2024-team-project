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
 * <p>
 * The user must enter positive integers for both rows and columns to proceed.
 * Error messages are displayed if the inputs are invalid.
 */
public class CreateDialog extends Dialog {

    /** Row number input field. */
    @FXML public TextField rowsInput;
    /** Column number input field. */
    @FXML public TextField colsInput;
    /** OK button. */
    @FXML public Button okButton;
    /** Cancel button. */
    @FXML public Button cancelButton;
    /** Error message label. */
    @FXML public Label errorLabel;

    /**
     * OK button action handler.
     */
    public void Ok() {
        errorLabel.setText("");
        try {
            // parse the input values
            int rows = Integer.parseInt(rowsInput.getText());
            int cols = Integer.parseInt(colsInput.getText());
            // check if the values are valid
            if (rows < 1 || cols < 1) {
                errorLabel.setText("Rows and cols must be positive!");
                logger.warning("Rows and cols must be positive!");
                return;
            }
            logger.info("Setting room dimensions to " + cols + "x" + rows);
            // set the room dimensions
            Room.getInstance().setDimensions(cols, rows);
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
            Stage owner = (Stage) stage.getOwner();
            Scene playGround = Playground.getScene();
            owner.setScene(playGround);
            owner.show(); // show the playground
        } catch (NumberFormatException e) {
            // handle invalid input, show an error message
            errorLabel.setText("Rows and cols be positive integers!");
            logger.warning("Rows and cols be positive integers!");
        }
    }

    /**
     * Retrieves and returns the scene for the Create dialog using the {@link SceneInterface}.
     * @return The loaded {@link Scene} for the Create dialog.
     */
    public static Scene getScene() {
        return SceneInterface.getScene(CreateDialog.class, "create_dialog.fxml"); // Load the Create dialog scene
    }
}
