package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.MenuInterface;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.gui.logic.Menu;
import ija.project.robot.logic.room.Room;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static ija.project.robot.RobotApp.logger;

public class Playground implements MenuInterface, SceneInterface {
    @FXML
    public AnchorPane AnchorPane; // fx:id="AnchorPane"
    @FXML
    public MenuItem MenuFileSaveAs;
    @FXML
    public MenuItem MenuFileLoad;
    @FXML
    public Canvas Canvas;

    private static final int gridWidth = 25;

    @FXML
    public void initialize() {
        canvasConstruct();
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

    private void canvasConstruct() {
        // add canvas
        logger.info("Constructing canvas");
        Room room = Room.getInstance();
        int width = room.getWidth();
        int height = room.getHeight();
        Canvas.setWidth(width * gridWidth);
        Canvas.setHeight(height * gridWidth);
        // Color canvas
        Canvas.getGraphicsContext2D().setFill(javafx.scene.paint.Color.RED);
        Canvas.getGraphicsContext2D().fillRect(0, 0, width * gridWidth, height * gridWidth);
        AnchorPane.setMinHeight(height * gridWidth + 200);
        AnchorPane.setMinWidth(width * gridWidth + 200);
    }

    public static Scene getScene() {
        logger.info("Getting playground scene");
        return SceneInterface.getScene(Playground.class, "playground.fxml");
    }
}
