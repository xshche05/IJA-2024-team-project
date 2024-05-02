package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.MenuInterface;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.gui.logic.Menu;
import ija.project.robot.gui.visualbuilder.VisualRobot;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.room.Room;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

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

    /**
     * Initializes the controller by setting up the UI components, canvas, and initial settings for the game mode.
     * This method constructs the canvas based on the room dimensions, initializes the toggle buttons,
     * and sets up the default settings for adding, moving, and controlling robots or obstacles.
     */
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


    /**
     * Loads the game configuration from a file into the current session.
     */
    @Override
    @FXML
    public void FileLoad() {
        new Menu().initialize().FileLoad(AnchorPane);
    }

    /**
     * Saves the current game configuration to a file.
     */
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

    /**
     * Constructs the canvas based on the room's dimensions and prepares it for drawing.
     * It sets the canvas size to fit the room, fills the background, and prepares the UI to receive mouse events.
     */
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

    /**
     * Handles mouse click events on the canvas to add, remove, or identify robots and obstacles.
     * Based on the current mode and whether the simulation is running, this method decides how to respond to clicks,
     * possibly adding or removing objects or making selections.
     *
     * @param e The MouseEvent that triggered this method.
     */
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

    /**
     * Gets the scene for the playground.
     *
     * @return The scene for the playground.
     */
    public static Scene getScene() {
        logger.info("Getting playground scene");
        return SceneInterface.getScene(Playground.class, "playground.fxml");
    }

    /**
     * Toggles the add/remove mode in the simulation.
     * When enabled, users can add robots or obstacles by clicking on the canvas.
     * When disabled, this method disables buttons, and users can remove robots or obstacles from the canvas.
     */
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

    /**
     * Toggles the mode to add automatic robots to the simulation.
     * This method updates the state to reflect whether automatic robots can currently be added.
     */
    public void PressAutoRobot(){
        autoRobot = autoBttn.isSelected();
        String mode = autoRobot ? "ENABLED" : "DISABLED";
        logger.info("Auto robot adding is " + mode);
        updatePlaceBttns();
    }

    /**
     * Toggles the mode to add manual robots to the simulation.
     * This method updates the state to reflect whether manual robots can currently be added.
     */
    public void PressManualRobot(){
        manualRobot = manualButton.isSelected();
        String mode = manualRobot ? "ENABLED" : "DISABLED";
        logger.info("Manual robot adding is " + mode);
        updatePlaceBttns();
    }

    /**
     * Toggles the mode to add obstacles to the simulation.
     * This method updates the state to reflect whether obstacles can currently be added.
     */
    public void PressObstacle(){
        obstacle = obstacleBttn.isSelected();
        String mode = obstacle ? "ENABLED" : "DISABLED";
        logger.info("Obstacle adding is " + mode);
        updatePlaceBttns();
    }

    /**
     * Synchronizes the toggle state of placement buttons with internal state variables.
     * This method ensures that the toggle states of the buttons for adding robots and obstacles
     * are consistent with the actual modes active in the simulation.
     */
    public void updatePlaceBttns(){
        add = !addBttn.isSelected();
        autoRobot = autoBttn.isSelected();
        manualRobot = manualButton.isSelected();
        obstacle = obstacleBttn.isSelected();
        start = strtbttn.isSelected();
    }

    /**
     * Toggles the start/pause state of the simulation.
     * When the simulation is started, this method disables editing modes and enables robot movement controls.
     * When the simulation is paused, it allows editing and disables movement controls.
     */
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

    public void PressLeft(){
        logger.info("Left button pressed");
    }

    public void PressGO(){
        logger.info("GO button pressed");
    }

    public void PressRight(){
        logger.info("Right button pressed");
    }

    /**
     * Clears a specific cell on the canvas, removing any visual elements from it.
     * This method is used to reset a cell to its default appearance, typically after a robot or obstacle has been removed.
     * The method also ensures that the cell removal is reflected in the Room's logical representation.
     *
     * @param x The x-coordinate of the cell to clear, based on grid coordinates.
     * @param y The y-coordinate of the cell to clear, based on grid coordinates.
     */
    public void clearCanvasCell(int x, int y) {
        Room.getInstance().removeFrom(new Position(x, y));
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(x * gridWidth, y * gridWidth, gridWidth, gridWidth);
        canvas.getGraphicsContext2D().setFill(Color.LIGHTGRAY);
        canvas.getGraphicsContext2D().strokeRect(x * gridWidth, y * gridWidth, gridWidth, gridWidth);
    }


    /**
     * Draws a ROBOT or OBSTACLE at a specified grid cell with an optional orientation indicator.
     * To draw a robot, provide a color and orientation. To draw an obstacle, provide a color and set orientation to null.
     * The robot is drawn as a circle, and if orientation is provided, a small dot indicates the direction the robot is facing.
     *
     * @param x The x-coordinate of the grid cell where the object will be placed.
     * @param y The y-coordinate of the grid cell where the object will be placed.
     * @param color The {@link Color} to use for the object's body.
     * @param robotOrientation The orientation of the robot in degrees, where 0 degrees indicates up. Set as null to draw an obstacle.
     */
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

    /**
     * Draws a grid on the canvas based on the dimensions of the room.
     *
     * Uses the singleton instance of {@link Room} to determine the grid dimensions.
     */
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

    /**
     * Draws the current state of the room on the canvas based on the room's configuration array.
     * Automatic robots are red, manual robots are cornflower blue, and obstacles are black.
     *
     * This method iterates through each cell in the room and draws
     * the appropriate visual representation on the canvas using {@link #placeCanvasCell}.
     *
     * Uses the singleton instance of {@link Room} to retrieve the current room configuration.
     */
    public void drawCanvasRoom() {
        Room room = Room.getInstance();
        String[][] cells = room.getRoomConfigurationArray();
        for (int i = 0; i < room.getWidth(); i++) {
            for (int j = 0; j < room.getHeight(); j++) {
                switch (cells[i][j]) {
                    case "A" -> placeCanvasCell(i, j, Color.RED, 0);
                    case "M" -> placeCanvasCell(i, j, Color.CORNFLOWERBLUE, 0);
                    case "O" -> placeCanvasCell(i, j, Color.BLACK, null);
                }
            }
        }
    }
}
