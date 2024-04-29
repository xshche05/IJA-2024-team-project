package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.MenuInterface;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.gui.logic.Menu;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Start implements MenuInterface, SceneInterface {
    public AnchorPane AnchorPane; // fx:id="AnchorPane"
    public MenuItem MenuFileLoad; // fx:id="MenuFileLoad"
    public MenuItem MenuFileSaveAs; // fx:id="MenuFileSaveAs"

    @Override
    public void FileLoad() {
        new Menu().initialize().FileLoad(AnchorPane);
    }

    @Override
    public void FileSaveAs() {
        new Menu().initialize().FileSaveAs(AnchorPane);
    }

    public static Scene getScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("start.fxml"));
        try {
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
