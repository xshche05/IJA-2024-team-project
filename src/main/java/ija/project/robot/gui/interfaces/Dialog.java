package ija.project.robot.gui.interfaces;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static ija.project.robot.RobotApp.logger;

public abstract class Dialog implements SceneInterface{
    @FXML
    public Button okButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Label errorLabel;
    abstract public void Ok();
    public void Cancel() {
        logger.info("Dialog window closed");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
