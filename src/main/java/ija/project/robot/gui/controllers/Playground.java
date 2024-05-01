package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.MenuInterface;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.gui.logic.Menu;
import ija.project.robot.logic.room.Room;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import static ija.project.robot.RobotApp.logger;

public class Playground implements MenuInterface, SceneInterface {
    @FXML
    public AnchorPane AnchorPane; // fx:id="AnchorPane"
    @FXML
    public MenuItem MenuFileSaveAs;
    @FXML
    public MenuItem MenuFileLoad;

    private static final int gridWidth = 25;
    @FXML
    public HBox HBoxCanvas;

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

        Canvas Canvas = new Canvas();

        Canvas.setWidth(width * gridWidth);
        Canvas.setHeight(height * gridWidth);
        // Color canvas
        Canvas.getGraphicsContext2D().setFill(javafx.scene.paint.Color.RED);
        Canvas.getGraphicsContext2D().fillRect(0, 0, width * gridWidth, height * gridWidth);
        AnchorPane.setMinHeight(height * gridWidth + 100);
        AnchorPane.setMinWidth(width * gridWidth + 100);
        HBoxCanvas.getChildren().add(Canvas);
        Canvas.setOnMouseClicked(this::CanvasClicked);
    }

    public void CanvasClicked(MouseEvent e) {
        logger.info("Canvas clicked");
        logger.info("X: " + e.getX() + " Y: " + e.getY());
        int x = (int) e.getX() / gridWidth;
        int y = (int) e.getY() / gridWidth;
        logger.info("X: " + x + " Y: " + y);
    }

    public static Scene getScene() {
        logger.info("Getting playground scene");
        return SceneInterface.getScene(Playground.class, "playground.fxml");
    }

    public void PressAdd(){

    }

    public void PressAutoRobot(){

    }

    public void PressManualRobot(){

    }

    public void PressObstacle(){

    }
}
