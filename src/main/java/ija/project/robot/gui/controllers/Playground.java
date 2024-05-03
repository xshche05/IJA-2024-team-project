package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.MenuInterface;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.gui.logic.Menu;
import ija.project.robot.gui.visualbuilder.*;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.room.Room;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static ija.project.robot.RobotApp.logger;

/**
 * Controller for the playground scene of the application.
 * This class handles the interactions of the user with the playground.
 * The playground allows users to add robots and obstacles to the room, start and pause the simulation,
 * and control the movement of robots.
 */
public class Playground implements MenuInterface, SceneInterface {
    @FXML
    public AnchorPane AnchorPane; // fx:id="AnchorPane"
    @FXML
    public MenuItem MenuFileSaveAs;
    @FXML
    public MenuItem MenuFileLoad;

    public static final int gridWidth = 20;
    @FXML
    public HBox HBoxCanvas;
    @FXML
    public ToggleButton addBttn;
    public ToggleButton autoBttn;
    public ToggleButton manualButton;
    public ToggleButton obstacleBttn;
    public ToggleButton strtbttn;

    // public Canvas canvas;
    //public GraphicsContext gc;

    public GridPane grid;
    public Button leftbttn;
    public Button gobttn;
    public Button rghtbttn;

    boolean add;
    boolean autoRobot;
    boolean manualRobot;
    boolean obstacle;
    boolean start;

    private List<AbstractVisualRobot> visualRobots = new ArrayList<>(); // List of robots at the playground
    private VisualManualRobot selectedRobot = null; // Selected robot at the playground

    /**
     * Initializes the controller by setting up the UI components, canvas, and initial settings for the game mode.
     * This method constructs the canvas based on the room dimensions, initializes the toggle buttons,
     * and sets up the default settings for adding, moving, and controlling robots or obstacles.
     */
    @FXML
    public void initialize() {

        gridPaneConstruct();

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


    private void gridPaneConstruct() {
        logger.info("Constructing GridPane");
        Room room = Room.getInstance();
        int width = room.getWidth();
        int height = room.getHeight();

        // Create a new GridPane
        GridPane grid = new GridPane();
        grid.setPrefSize(width * gridWidth, height * gridWidth);
        grid.setGridLinesVisible(true);  // Optionally make grid lines visible

        // Initialize the grid cells with default empty cells (could be empty Rectangles or similar)
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Rectangle rect = new Rectangle(gridWidth, gridWidth);
                GridPane.setConstraints(rect, i, j);
                grid.getChildren().add(rect);
            }
        }


        // Set minimum size for the AnchorPane
        AnchorPane.setMinHeight(height * gridWidth + 100);
        AnchorPane.setMinWidth(width * gridWidth + 100);

        // Add the grid to your layout container
        HBoxCanvas.getChildren().add(grid);


        // Draw the room and update the grid based on current room state
        drawGridRoom();
    }

    public void drawGridRoom() {
        Room room = Room.getInstance();
        String[][] cells = room.getRoomConfigurationArray();
        grid.getChildren().clear(); // Clear existing content

        for (int i = 0; i < room.getHeight(); i++) {
            for (int j = 0; j < room.getWidth(); j++) {
                switch (cells[i][j]) {
                    case "A":
                        placeAutoRobot(x, y, 0);
                        break;
                    case "M":
                        placeManualRobot(x, y, 0);
                        break;
                    case "O":
                        placeObstacle(x, y);
                        break;
                    default:
                        // Optionally add a default look for empty cells or just leave them empty
                        clearGridCell(x, y);
                        break;
                }
            }
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

    public void dridCellClicked(MouseEvent e) {

        int x = (int) e.getX() / gridWidth;
        int y = (int) e.getY() / gridWidth;
        logger.info("Grid cell clicked: (" + x + ", " + y + ")");


        if (!start) { // If the simulation at the PAUSE mode
            if (add) {
                if (autoRobot) {
                    Room.getInstance().addAutoRobot(new Position(x, y));
                    logger.info("Auto robot added at " + x + " " + y);

                    placeAutoRobot(x, y, 0);

                } else if (manualRobot) {
                    Room.getInstance().addManualRobot(new Position(x, y));
                    logger.info("Manual robot added at " + x + " " + y);
                    //VisualRobot robot = new VisualRobot(x, y, gridWidth, this);
                    // visualRobots.add(robot);
                    placeManualRobot(x, y, 0);
                } else if (obstacle) {
                    Room.getInstance().addObstacle(new Position(x, y));
                    logger.info("Obstacle added at " + x + " " + y);
                    placeObstacle(x, y);
                }
            }
            else {
                if (Room.getInstance().isPositionFree(new Position(x, y))) {
                    logger.info("Position is free");
                } else {
                    logger.info("Position is not free");
                    clearGridCell(x, y);
                }
            }
        }else {
            logger.info("Simulation is running");
        }
    }


    public void selectRobot(AbstractVisualRobot abstractVisualRobot) {
        this.selectedRobot = abstractVisualRobot;
    }
}
