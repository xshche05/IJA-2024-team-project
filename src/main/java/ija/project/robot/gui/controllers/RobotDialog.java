/*
 * Author: Kirill Shchetiniuk (xshche05)
 * Description: This file provides the logic for the robot dialog.
 * It allows users to configure parameters for both manual and automatic robots.
 */
package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.Dialog;
import ija.project.robot.gui.interfaces.SceneInterface;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static ija.project.robot.RobotApp.logger;

/**
 * A dialog for setting robot parameters in a simulation environment, extending the {@link Dialog} class.
 * This dialog allows users to configure parameters for both manual and automatic robots,
 * such as speed, angle, and distance (for automatic robots only).
 * <p>
 * The dialog adapts based on the type of robot being configured: distance input is disabled for manual robots.
 */
public class RobotDialog extends Dialog {
    public TextField speedInput;
    public TextField angleInput1;
    public TextField distInput;

    public static boolean validData;
    public static int Angle;
    public static int Speed;
    public static int Distance;

    private String request; // "MANUAL" or "AUTOMATIC" robot context

    /**
     * Initializes the robot dialog, setting up default states and configurations
     * based on the robot type requested from the playground scene.
     */
    public void initialize() {
        validData = false;
        request = Playground.createRequest;
        // The distance input can be set only for automatic robots
        if (request.equals("MANUAL")) {
            distInput.setDisable(true);
        }
        errorLabel.setText("");
    }

    /**
     * Handles the OK action which attempts to parse and validate the robot parameters input by the user.
     * If parameters are valid, this method closes the dialog and records the entered values.
     * If any parameter is invalid, it sets an error message and logs a warning.
     */
    public void Ok() {
        try {
            Speed = Integer.parseInt(speedInput.getText());
            Angle = Integer.parseInt(angleInput1.getText());
            if (request.equals("MANUAL")) {
                Distance = 1;
            } else {
                Distance = Integer.parseInt(distInput.getText());
            }
            if (Speed < 0 || Distance < 1) {
                errorLabel.setText("Speed must be positive, angle must be between 0 and 360, distance must be positive!");
                logger.warning("Speed must be positive, angle must be between 0 and 360, distance must be positive!");
                return;
            }
            logger.info("Setting robot parameters to speed: " + Speed + ", angle: " + Angle + ", distance: " + Distance);
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            errorLabel.setText("Speed, angle and distance must be positive integers!");
            logger.warning("Speed, angle and distance must be positive integers!");
        }
        validData = true;
    }

    /**
     * Provides the Scene associated with this dialog, loading the layout from the corresponding FXML file.
     *
     * @return The Scene for the robot dialog.
     */
    public static Scene getScene() {
        return SceneInterface.getScene(RobotDialog.class, "robot_dialog.fxml");
    }
}
