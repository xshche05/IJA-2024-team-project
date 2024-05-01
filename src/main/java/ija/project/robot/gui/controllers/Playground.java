package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.MenuInterface;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.gui.logic.Menu;
import ija.project.robot.logic.room.Room;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class Playground implements MenuInterface, SceneInterface {

    @FXML
    public AnchorPane AnchorPane; // fx:id="AnchorPane"
    @FXML
    public MenuItem MenuFileSaveAs;
    @FXML
    public MenuItem MenuFileLoad;

    @FXML
    public void initialize() {
        gridConstruct();
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

    @Override
    @FXML
    public void Help() {
        new Menu().initialize().Help(AnchorPane);
    }

    private void gridConstruct() {
        String[][] room = Room.getInstance().getRoomConfigurationArray();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        // enable grid lines
        grid.setGridLinesVisible(true);
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[i].length; j++) {
                Label label = new Label(room[i][j]);
                grid.add(label, j, i);
            }
        }
        AnchorPane.getChildren().add(grid);
    }

    public static Scene getScene() {
        return SceneInterface.getScene("playground.fxml");
    }
}