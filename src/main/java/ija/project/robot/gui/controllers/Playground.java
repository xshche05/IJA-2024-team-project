package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.MenuInterface;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.gui.logic.Menu;
import ija.project.robot.gui.visualbuilder.*;
import ija.project.robot.logic.room.Room;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

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

    public static final int gridWidth = 28;
    @FXML
    public HBox HBoxGrid;
    public HBox HBoxBttnDown;
    public HBox HBoxBttnUp;

    private List<Node> addGroup = new ArrayList<>();
    private List<Node> removeGroup = new ArrayList<>();
    private List<Node> startGroup = new ArrayList<>();
    private List<Node> pauseGroup = new ArrayList<>();

//    private List<AbstractVisualRobot> visualRobots = new ArrayList<>(); // List of robots at the playground
//    private VisualManualRobot selectedRobot = null; // Selected robot at the playground

    /**
     * Initializes the controller by setting up the UI components, canvas, and initial settings for the game mode.
     * This method constructs the canvas based on the room dimensions, initializes the toggle buttons,
     * and sets up the default settings for adding, moving, and controlling robots or obstacles.
     */
    @FXML
    public void initialize() {
        gridPaneConstruct();

        ToggleButton addOrRemove = new ToggleButton("ADD MODE");

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
        grid = new GridPane();
        grid.setMinSize(width * gridWidth, height * gridWidth);
        grid.setMaxSize(width * gridWidth, height * gridWidth);
        grid.setStyle("-fx-background-color: #FFFFFF;");
        grid.setGridLinesVisible(true);
        for (int i = 0; i < width; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(gridWidth));
        }
        for (int i = 0; i < height; i++) {
            grid.getRowConstraints().add(new RowConstraints(gridWidth));
        }
        grid.setOnMouseClicked(this::gridClicked);
        HBoxGrid.getChildren().add(grid);
        AnchorPane.setMinHeight(height * gridWidth + 100);
        AnchorPane.setMinWidth(width * gridWidth + 100);
        // todo  build room;
    }

    public void drawGridRoom() {
        Room room = Room.getInstance();
        String[][] cells = room.getRoomConfigurationArray();
        grid.getChildren().clear(); // Clear existing content

        for (int i = 0; i < room.getHeight(); i++) {
            for (int j = 0; j < room.getWidth(); j++) {
                switch (cells[i][j]) {
                    case "A":
//                        placeAutoRobot(x, y, 0);
                        break;
                    case "M":
//                        placeManualRobot(x, y, 0);
                        break;
                    case "O":
//                        placeObstacle(x, y);
                        break;
                    default:
                        // Optionally add a default look for empty cells or just leave them empty
//                        clearGridCell(x, y);
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
    public void PressAdd() {
        if (addBttn.isSelected()){
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
        String mode = autoBttn.isSelected() ? "ENABLED" : "DISABLED";
        logger.info("Auto robot adding is " + mode);
    }

    /**
     * Toggles the mode to add manual robots to the simulation.
     * This method updates the state to reflect whether manual robots can currently be added.
     */
    public void PressManualRobot(){
        String mode = manualButton.isSelected() ? "ENABLED" : "DISABLED";
        logger.info("Manual robot adding is " + mode);
    }

    /**
     * Toggles the mode to add obstacles to the simulation.
     * This method updates the state to reflect whether obstacles can currently be added.
     */
    public void PressObstacle(){
        String mode = obstacleBttn.isSelected() ? "ENABLED" : "DISABLED";
        logger.info("Obstacle adding is " + mode);
    }

    /**
     * Toggles the start/pause state of the simulation.
     * When the simulation is started, this method disables editing modes and enables robot movement controls.
     * When the simulation is paused, it allows editing and disables movement controls.
     */
    public void PressStartPause(){
        logger.info("Start button pressed");
        if (!strtbttn.isSelected()){
            strtbttn.setText("PAUSE");
            strtbttn.setStyle("-fx-background-color: yellow;");
            // Enable map editing buttons
            addBttn.setDisable(false);
            if(addBttn.isSelected()){
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

    public void gridClicked(MouseEvent e) {
        logger.info("Grid cell clicked px cords: (" + e.getX() + ", " + e.getY() + ")");
        int x = (int) e.getX() / gridWidth;
        int y = (int) e.getY() / gridWidth;
        logger.info("Grid cell clicked: (" + x + ", " + y + ")");
    }


    public void selectRobot(AbstractVisualRobot abstractVisualRobot) {
//        this.selectedRobot = abstractVisualRobot;
    }

    private void disableEdit() {

    }
}
