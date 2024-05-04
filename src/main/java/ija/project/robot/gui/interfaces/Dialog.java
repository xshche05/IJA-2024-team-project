package ija.project.robot.gui.interfaces;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import static ija.project.robot.RobotApp.logger;

/**
 * Abstract base class for dialog windows within the application.
 *
 * <p>Subclasses are expected to implement the {@link #Ok()} method to define specific
 * behaviors when the button is pressed, depending on the dialog's purpose.</p>
 */
public abstract class Dialog implements SceneInterface {
    @FXML
    public Button okButton;  // Button to confirm the dialog's action
    @FXML
    public Button cancelButton;  // Button to cancel the dialog and close the window
    @FXML
    public Label errorLabel;  // Label to display error messages

    /**
     * Abstract method that must be implemented by subclasses to handle the action
     * when the OK button is pressed.
     */
    abstract public void Ok();

    /**
     * Handles the cancellation of the dialog. This method is bound to the Cancel button's
     * action in the UI and should close the dialog window without performing any action.
     */
    public void Cancel() {
        logger.info("Dialog window closed");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();  // Closes the dialog stage, effectively dismissing the dialog
    }
}
