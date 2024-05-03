package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.Dialog;
import ija.project.robot.gui.interfaces.SceneInterface;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static ija.project.robot.RobotApp.logger;

public class RobotDialog extends Dialog {
    public TextField speedInput;
    public TextField angleInput1;
    public TextField distInput;

    public static boolean validData;
    public static int Angle;
    public static int Speed;
    public static int Distance;
    private String request;

    public void initialize() {
        validData = false;
        request = Playground.createRequest;
        if (request.equals("MANUAL")) {
            distInput.setDisable(true);
        }
        errorLabel.setText("");
    }

    public void Ok() {
        try {
            Speed = Integer.parseInt(speedInput.getText());
            Angle = Integer.parseInt(angleInput1.getText());
            if (request.equals("MANUAL")) {
                Distance = 1;
            } else {
                Distance = Integer.parseInt(distInput.getText());
            }
            if (Speed < 1 || Angle < 0 || Angle > 360 || Distance < 1) {
                errorLabel.setText("Speed must be positive, angle must be between 0 and 360, distance must be positive!");
                logger.warning("Speed must be positive, angle must be between 0 and 360, distance must be positive!");
                return;
            }
            logger.info("Setting robot parameters to speed: " + Speed + ", angle: " + Angle + ", distance: " + Distance);
            validData = true;
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            errorLabel.setText("Speed, angle and distance must be positive integers!");
            logger.warning("Speed, angle and distance must be positive integers!");
        }
    }

    public static Scene getScene() {
        return SceneInterface.getScene(RobotDialog.class, "robot_dialog.fxml");
    }
}
