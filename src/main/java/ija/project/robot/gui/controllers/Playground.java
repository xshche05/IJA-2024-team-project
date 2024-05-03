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
import javafx.scene.control.ToggleGroup;
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
    public GridPane grid;
    @FXML
    public HBox HBoxGrid;
    public HBox HBoxBttnDown;
    public HBox HBoxBttnUp;

    private List<Node> addGroup = new ArrayList<>();
    private List<Node> removeGroup = new ArrayList<>();
    private List<Node> startGroup = new ArrayList<>();
    private List<Node> pauseGroup = new ArrayList<>();

    /**
     * Initializes the controller by setting up the UI components, canvas, and initial settings for the game mode.
     * This method constructs the canvas based on the room dimensions, initializes the toggle buttons,
     * and sets up the default settings for adding, moving, and controlling robots or obstacles.
     */
    @FXML
    public void initialize() {
        gridPaneConstruct();

        ToggleButton addOrRemove = new ToggleButton("ADD MODE");
        addOrRemove.setStyle("-fx-background-color: LightBlue;");
        addOrRemove.setOnAction(e -> AddOreRemoveAction());
        addGroup.add(addOrRemove);
        removeGroup.add(addOrRemove);
        ToggleGroup placeSelection = new ToggleGroup();
        ToggleButton autoRobot = new ToggleButton("AUTO ROBOT");
        autoRobot.setOnAction(e -> AutoRobotAction());
        autoRobot.setToggleGroup(placeSelection);
        addGroup.add(autoRobot);
        ToggleButton manualRobot = new ToggleButton("MANUAL ROBOT");
        manualRobot.setOnAction(e -> ManualRobotAction());
        manualRobot.setToggleGroup(placeSelection);
        addGroup.add(manualRobot);
        ToggleButton obstacle = new ToggleButton("OBSTACLE");
        obstacle.setOnAction(e -> ObstacleAction());
        obstacle.setToggleGroup(placeSelection);
        addGroup.add(obstacle);


        ToggleButton startPause = new ToggleButton("PAUSE");
        startPause.setOnAction(e -> StartPauseAction());
        startPause.setStyle("-fx-background-color: yellow;");
        startGroup.add(startPause);
        pauseGroup.add(startPause);
        Button left = new Button("LEFT");
        left.setOnAction(e -> LeftAction());
        startGroup.add(left);
        Button go = new Button("GO");
        go.setOnAction(e -> GoAction());
        startGroup.add(go);
        Button right = new Button("RIGHT");
        right.setOnAction(e -> RightAction());
        startGroup.add(right);

        Button removeAll = new Button("REMOVE ALL");
        removeAll.setOnAction(e -> RemoveAllAction());
        removeGroup.add(removeAll);
        Button removeObstacles = new Button("REMOVE OBSTACLES");
        removeObstacles.setOnAction(e -> RemoveObstaclesAction());
        removeGroup.add(removeObstacles);
        Button removeRobots = new Button("REMOVE ROBOTS");
        removeRobots.setOnAction(e -> RemoveRobotsAction());
        removeGroup.add(removeRobots);

        for (Node node : addGroup) {
            ((Region) node).setMinSize(100, 30);
        }
        for (Node node : removeGroup) {
            ((Region) node).setMinSize(120, 30);
        }
        for (Node node : startGroup) {
            ((Region) node).setMinSize(100, 30);
        }
        for (Node node : pauseGroup) {
            ((Region) node).setMinSize(100, 30);
        }

        HBoxBttnUp.getChildren().addAll(addGroup);
        HBoxBttnDown.getChildren().addAll(pauseGroup);
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
     * Gets the scene for the playground.
     *
     * @return The scene for the playground.
     */
    public static Scene getScene() {
        logger.info("Getting playground scene");
        return SceneInterface.getScene(Playground.class, "playground.fxml");
    }

    public void RemoveAllAction() {
        logger.info("Remove all button pressed");
        Room room = Room.getInstance();
        room.clear();
    }

    public void RemoveObstaclesAction() {
        logger.info("Remove obstacles button pressed");
        Room room = Room.getInstance();
        room.removeObstacles();
    }

    public void RemoveRobotsAction() {
        logger.info("Remove robots button pressed");
        Room room = Room.getInstance();
        room.removeRobots();
    }


    /**
     * Toggles the add/remove mode in the simulation.
     * When enabled, users can add robots or obstacles by clicking on the canvas.
     * When disabled, this method disables buttons, and users can remove robots or obstacles from the canvas.
     */
    public void AddOreRemoveAction() {
        ToggleButton addBttn = (ToggleButton) addGroup.get(0);
        if (addBttn.isSelected()){
            addBttn.setText("REMOVE MODE");
            addBttn.setStyle("-fx-background-color: LightPink;");
            logger.info("Playground REMOVE MODE activated");
            HBoxBttnUp.getChildren().clear();
            HBoxBttnUp.getChildren().addAll(removeGroup);
        } else {
            addBttn.setText("ADD MODE");
            addBttn.setStyle("-fx-background-color: LightBlue;");
            logger.info("Playground ADD MODE activated");
            HBoxBttnUp.getChildren().clear();
            HBoxBttnUp.getChildren().addAll(addGroup);
        }
    }

    /**
     * Toggles the mode to add automatic robots to the simulation.
     * This method updates the state to reflect whether automatic robots can currently be added.
     */
    public void AutoRobotAction(){
        ToggleButton autoBttn = (ToggleButton) addGroup.get(1);
        String mode = autoBttn.isSelected() ? "ENABLED" : "DISABLED";
        logger.info("Auto robot adding is " + mode);
    }

    /**
     * Toggles the mode to add manual robots to the simulation.
     * This method updates the state to reflect whether manual robots can currently be added.
     */
    public void ManualRobotAction(){
        ToggleButton manualButton = (ToggleButton) addGroup.get(2);
        String mode = manualButton.isSelected() ? "ENABLED" : "DISABLED";
        logger.info("Manual robot adding is " + mode);
    }

    /**
     * Toggles the mode to add obstacles to the simulation.
     * This method updates the state to reflect whether obstacles can currently be added.
     */
    public void ObstacleAction(){
        ToggleButton obstacleBttn = (ToggleButton) addGroup.get(3);
        String mode = obstacleBttn.isSelected() ? "ENABLED" : "DISABLED";
        logger.info("Obstacle adding is " + mode);
    }

    /**
     * Toggles the start/pause state of the simulation.
     * When the simulation is started, this method disables editing modes and enables robot movement controls.
     * When the simulation is paused, it allows editing and disables movement controls.
     */
    public void StartPauseAction(){
        logger.info("Start button pressed");
        ToggleButton strtbttn = (ToggleButton) startGroup.get(0);
        if (!strtbttn.isSelected()){
            strtbttn.setText("PAUSE");
            strtbttn.setStyle("-fx-background-color: yellow;");
            HBoxBttnDown.getChildren().clear();
            HBoxBttnDown.getChildren().addAll(pauseGroup);
            HBoxBttnUp.getChildren().addAll(addGroup);
        } else {
            strtbttn.setText("START");
            strtbttn.setStyle("-fx-background-color: lightgreen;");
            HBoxBttnDown.getChildren().clear();
            HBoxBttnDown.getChildren().addAll(startGroup);
            HBoxBttnUp.getChildren().clear();
        }
    }

    public void LeftAction(){
        logger.info("Left button pressed");
    }

    public void GoAction(){
        logger.info("GO button pressed");
    }

    public void RightAction(){
        logger.info("Right button pressed");
    }

    public void gridClicked(MouseEvent e) {
        logger.info("Grid cell clicked px cords: (" + e.getX() + ", " + e.getY() + ")");
        int x = (int) e.getX() / gridWidth;
        int y = (int) e.getY() / gridWidth;
        logger.info("Grid cell clicked: (" + x + ", " + y + ")");
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
}
