package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.MenuInterface;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.gui.logic.Menu;
import ija.project.robot.logic.room.Room;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class Playground implements MenuInterface, SceneInterface {

    @FXML
    public AnchorPane AnchorPane; // fx:id="AnchorPane"
    @FXML
    public MenuItem MenuFileSaveAs;
    @FXML
    public MenuItem MenuFileLoad;
    @FXML
    public Label playGroundConfig;

    @FXML
    public void initialize() {
        playGroundConfig.setText(Room.getInstance().toString());
    }


    @Override
    @FXML
    public void FileLoad() {
        new Menu().initialize().FileLoad(AnchorPane);
    }

    @Override
    @FXML
    public void FileSaveAs() {
        new Menu().initialize().FileSaveAs(AnchorPane);
    }

    public static Scene getScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Playground.class.getResource("playground.fxml"));
        try {
            return new Scene(fxmlLoader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
