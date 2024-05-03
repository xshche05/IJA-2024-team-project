package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.Dialog;
import ija.project.robot.logic.room.Room;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static ija.project.robot.RobotApp.logger;

public class CreateDialog implements Dialog {
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

    public void Cancel() {
        logger.info("Room creation cancelled");
        // close dialog
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
