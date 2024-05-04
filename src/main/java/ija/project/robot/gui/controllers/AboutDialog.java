package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.Dialog;
import ija.project.robot.gui.interfaces.SceneInterface;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;



public class AboutDialog extends Dialog {

    @FXML
    public Button cancelButton;

    @Override
    public void Ok() {
        Cancel();
    }

    public static Scene getScene() {
        return SceneInterface.getScene(AboutDialog.class, "about_dialog.fxml");
    }
}
