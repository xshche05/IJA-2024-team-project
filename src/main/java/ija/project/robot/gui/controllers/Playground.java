package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.MenuInterface;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.gui.logic.Menu;
import ija.project.robot.gui.visualbuilder.VisualRobot;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.room.Room;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    public ToggleButton addBttn;
    public ToggleButton autoBttn;
    public ToggleButton manualButton;
    public ToggleButton obstacleBttn;
    public ToggleButton strtbttn;

    public Canvas canvas;
    public GraphicsContext gc;
    public Button leftbttn;
    public Button gobttn;
    public Button rghtbttn;

    boolean add;
    boolean autoRobot;
    boolean manualRobot;
    boolean obstacle;
    boolean start;

    private List<VisualRobot> visualRobots = new ArrayList<>(); // List of robots at the playground
    private VisualRobot selectedRobot = null; // Selected robot at the playground

    @FXML
    public void initialize() {
        canvasConstruct();
        gc = canvas.getGraphicsContext2D();

        addBttn.setText("ADD MODE");
        addBttn.setStyle("-fx-background-color: LightBlue;");
        add = !addBttn.isSelected();
        autoRobot = autoBttn.isSelected();
        manualRobot = manualButton.isSelected();
        obstacle = obstacleBttn.isSelected();

        start = strtbttn.isSelected();
        strtbttn.setText("PAUSE");
        strtbttn.setStyle("-fx-background-color: yellow;");

        leftbttn.setDisable(true);
        gobttn.setDisable(true);
        rghtbttn.setDisable(true);
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
        int max_x = (int) Screen.getPrimary().getBounds().getMaxX();
        int max_y = (int) Screen.getPrimary().getBounds().getMaxY();
        logger.info("Max X: " + max_x + " Max Y: " + max_y);
        canvas = new Canvas();
        canvas.setWidth(width * gridWidth);
        canvas.setHeight(height * gridWidth);
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0, 0, width * gridWidth, height * gridWidth);
        AnchorPane.setMinHeight(height * gridWidth+100);
        AnchorPane.setMinWidth(width * gridWidth+100);
        HBoxCanvas.getChildren().add(canvas);
        canvas.setOnMouseClicked(this::CanvasClicked);
        drawCanvasRoom();
        updateCanvasGrid();
    }

    public void CanvasClicked(MouseEvent e) {
        logger.info("Canvas clicked");
        logger.info("X: " + e.getX() + " Y: " + e.getY());
        int x = (int) e.getX() / gridWidth;
        int y = (int) e.getY() / gridWidth;
        logger.info("X: " + x + " Y: " + y);
        // fill cell with green color
        if (!start) { // If the simulation at the PAUSE mode
            if (add) {
                clearCanvasCell(x, y);
                if (autoRobot) {
                    Room.getInstance().addAutoRobot(new Position(x, y));
                    logger.info("Auto robot added at " + x + " " + y);
                    placeCanvasCell(x, y, Color.RED, 0);

                } else if (manualRobot) {
                    Room.getInstance().addManualRobot(new Position(x, y));
                    logger.info("Manual robot added at " + x + " " + y);
                    //VisualRobot robot = new VisualRobot(x, y, gridWidth, this);
                    // visualRobots.add(robot);
                    placeCanvasCell(x, y, Color.CORNFLOWERBLUE, 0);
                } else if (obstacle) {
                    Room.getInstance().addObstacle(new Position(x, y));
                    logger.info("Obstacle added at " + x + " " + y);
                    placeCanvasCell(x, y, Color.BLACK, null);
                }
            }
            else {
                if (Room.getInstance().isPositionFree(new Position(x, y))) {
                    logger.info("Position is free");
                } else {
                    logger.info("Position is not free");
                    clearCanvasCell(x, y);
                }
            }
            updateCanvasGrid();
        }else {
            logger.info("Simulation is running");
        }

    }

    public static Scene getScene() {
        logger.info("Getting playground scene");
        return SceneInterface.getScene(Playground.class, "playground.fxml");
    }


    // First buttons row
    public void PressAdd(){
        add = !addBttn.isSelected();
        if (!add){
            addBttn.setText("REMOVE MODE");
            addBttn.setStyle("-fx-background-color: LightPink;");
            logger.info("Playground REMOVE MODE activated");
            obstacleBttn.setDisable(true);
            autoBttn.setDisable(true);
            manualButton.setDisable(true);
        } else {
            addBttn.setText("ADD MODE");
            addBttn.setStyle("-fx-background-color: LightBlue;");
            logger.info("Playground ADD MODE activated");
            autoBttn.setDisable(false);
            manualButton.setDisable(false);
            obstacleBttn.setDisable(false);
        }
    }

    public void PressAutoRobot(){
        autoRobot = autoBttn.isSelected();
        String mode = autoRobot ? "ENABLED" : "DISABLED";
        logger.info("Auto robot adding is " + mode);
        updatePlaceBttns();
    }

    public void PressManualRobot(){
        manualRobot = manualButton.isSelected();
        String mode = manualRobot ? "ENABLED" : "DISABLED";
        logger.info("Manual robot adding is " + mode);
        updatePlaceBttns();
    }

    public void PressObstacle(){
        obstacle = obstacleBttn.isSelected();
        String mode = obstacle ? "ENABLED" : "DISABLED";
        logger.info("Obstacle adding is " + mode);
        updatePlaceBttns();
    }

    public void updatePlaceBttns(){
        add = !addBttn.isSelected();
        autoRobot = autoBttn.isSelected();
        manualRobot = manualButton.isSelected();
        obstacle = obstacleBttn.isSelected();
        start = strtbttn.isSelected();
    }

    // Second buttons row

    public void PressStartPause(){
        logger.info("Start button pressed");
        start = strtbttn.isSelected();
        if (!start){
            strtbttn.setText("PAUSE");
            strtbttn.setStyle("-fx-background-color: yellow;");
            // Enable map editing buttons
            addBttn.setDisable(false);
            add = !addBttn.isSelected();
            if(!add){
                obstacleBttn.setDisable(true);
                autoBttn.setDisable(true);
                manualButton.setDisable(true);
            } else {
                obstacleBttn.setDisable(false);
                autoBttn.setDisable(false);
                manualButton.setDisable(false);
            }

            // Disable robot movement buttons
            leftbttn.setDisable(true);
            gobttn.setDisable(true);
            rghtbttn.setDisable(true);

        } else {
            strtbttn.setText("START");
            strtbttn.setStyle("-fx-background-color: lightgreen;");
            // Disable map editing buttons
            addBttn.setDisable(true);
            autoBttn.setDisable(true);
            manualButton.setDisable(true);
            obstacleBttn.setDisable(true);
            // Enable robot movement buttons
            leftbttn.setDisable(false);
            gobttn.setDisable(false);
            rghtbttn.setDisable(false);
        }
    }

    public void PressLeftRemoveAll(){
        logger.info("Pause button pressed");
    }

    public void PressGO(){
        logger.info("Pause button pressed");
    }

    public void PressRight(){
        logger.info("Restart button pressed");
    }





    public void clearCanvasCell(int x, int y) {
        Room.getInstance().removeFrom(new Position(x, y));
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(x * gridWidth, y * gridWidth, gridWidth, gridWidth);
        canvas.getGraphicsContext2D().setFill(Color.LIGHTGRAY);
        canvas.getGraphicsContext2D().strokeRect(x * gridWidth, y * gridWidth, gridWidth, gridWidth);
    }

//    public void placeCanvasCell(int x, int y, Color color) {
//        canvas.getGraphicsContext2D().setFill(color);
//        canvas.getGraphicsContext2D().fillRect(x * gridWidth, y * gridWidth, gridWidth, gridWidth);
//    }

    public void placeCanvasCell(int x, int y, Color color, Integer robotOrientation) {
        double centerX = x * gridWidth + gridWidth / 2.0;
        double centerY = y * gridWidth + gridWidth / 2.0;
        double radius = gridWidth / 3.0;

        gc.setFill(color);

        if (robotOrientation != null) {
            // Draw robot as circle
            gc.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

            // Drawing the forward direction indicator (a small dot)
            double dotRadius = gridWidth / 10.0;
            double orientationRadians = Math.toRadians(robotOrientation) + Math.PI / 2.0;

            // Calculate the position of the dot
            double forwardX = centerX + (radius - dotRadius) * Math.cos(orientationRadians);
            double forwardY = centerY - (radius - dotRadius) * Math.sin(orientationRadians); // Negative because Y is positive downwards

            gc.setFill(Color.BLACK); // Color for direction dot
            gc.fillOval(forwardX - dotRadius, forwardY - dotRadius, 2 * dotRadius, 2 * dotRadius);

        } else {
            // Draw obstacle as square
            gc.fillRect(x * gridWidth, y * gridWidth, gridWidth, gridWidth);
        }
    }


    public void updateCanvasGrid() {
        Room room = Room.getInstance();
        int width = room.getWidth();
        int height = room.getHeight();
        canvas.getGraphicsContext2D().setFill(Color.LIGHTGRAY);
        for (int i = 0; i < width; i++) {
            canvas.getGraphicsContext2D().strokeLine(i * gridWidth, 0, i * gridWidth, height * gridWidth);
        }
        for (int i = 0; i < height; i++) {
            canvas.getGraphicsContext2D().strokeLine(0, i * gridWidth, width * gridWidth, i * gridWidth);
        }
        canvas.getGraphicsContext2D().strokeRect(0, 0, width * gridWidth, height * gridWidth);
    }

    public void drawCanvasRoom() {
        Room room = Room.getInstance();
        String[][] cells = room.getRoomConfigurationArray();
        for (int i = 0; i < room.getWidth(); i++) {
            for (int j = 0; j < room.getHeight(); j++) {
                if (cells[i][j].equals("A")) {
                    placeCanvasCell(i, j, Color.RED, 0);
                } else if (cells[i][j].equals("M")) {
                    placeCanvasCell(i, j, Color.CORNFLOWERBLUE, 0);
                } else if (cells[i][j].equals("O")) {
                    placeCanvasCell(i, j, Color.BLACK, null);
                }
            }
        }
    }
}
