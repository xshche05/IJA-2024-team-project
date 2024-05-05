/*
 * Author: Kirill Shchetiniuk (xshche05), Artur Sultanov (xsulta01)
 * Description: This file contains the implementation of the Playground controller. It handles the user interactions with the playground scene.
 * It allows users to add robots and obstacles to the room, start and pause the simulation, and control the movement of robots.
 * Initializes the UI components and prepares the simulation environment.
 */
package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.MenuInterface;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.gui.logic.ControlledRobot;
import ija.project.robot.gui.logic.Menu;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.robots.AbstractRobot;
import ija.project.robot.logic.robots.AutomatedRobot;
import ija.project.robot.logic.robots.ManualRobot;
import ija.project.robot.logic.room.Room;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import javafx.application.Platform;

import static ija.project.robot.RobotApp.logger;

/**
 * Controller for the playground scene of the application.
 * Implements the {@link MenuInterface} and {@link SceneInterface} interfaces.
 * This class handles the interactions of the user with the playground.
 * The playground allows users to add robots and obstacles to the room, start and pause the simulation,
 * and control the movement of robots.
 */
public class Playground implements MenuInterface, SceneInterface {
    @FXML public AnchorPane AnchorPane; // Main pane for the playground
    @FXML public MenuItem MenuFileSaveAs;
    @FXML public MenuItem MenuFileLoad;
    @FXML public MenuItem MenuNewFile;
    @FXML public HBox HBoxGrid;
    @FXML public HBox HBoxBttnDown;
    @FXML public HBox HBoxBttnUp;

    public static final int gridWidth = 32; // Width of each cell in the grid
    public static final int tickPeriod = 1000; // Milliseconds between each application tick
    public GridPane grid; // Grid layout for placing robots and obstacles
    private Thread tickThread; // Thread for running the simulation ticks
    public static String createRequest; // Tracks requests for creating new robots or obstacles
    private final List<Node> addGroup = new ArrayList<>(); // Group of buttons for adding objects
    private final List<Node> removeGroup = new ArrayList<>(); // Group of buttons for removing objects
    private final List<Node> startGroup = new ArrayList<>(); // Group of buttons for starting the simulation
    private final List<Node> pauseGroup = new ArrayList<>(); // Group of buttons for pausing the simulation
    private final List<Node> moveGroup = new ArrayList<>(); // Group of buttons for moving selected robot

    private String currentMode = "ADD"; // Current mode of the playground
    private String lastEditMode = "ADD"; // Last edit mode of the playground

    public static final Semaphore playSemaphore = new Semaphore(1); // Semaphore for managing playback control

    /**
     * Initializes the controller setup by configuring UI components and preparing the simulation environment.
     * This method sets up the grid, initializes interactive buttons, and configures the timeline for simulation ticks.
     */
    @FXML
    public void initialize() {
        createRequest = "NONE";
        GridPaneConstruct();
        setupInteractiveButtons();
        currentMode = "ADD";
        lastEditMode = "ADD";
    }

    @Override
    @FXML
    public void CreateNew() {
        new Menu().initialize().CreateNew(AnchorPane);
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
    public void LoadPredefinedMap1() {
        new Menu().initialize().LoadPredefinedMap1(AnchorPane);
    }

    @Override
    @FXML
    public void LoadPredefinedMap2() {
        new Menu().initialize().LoadPredefinedMap2(AnchorPane);
    }

    @Override
    @FXML
    public void LoadPredefinedMap3() {
        new Menu().initialize().LoadPredefinedMap3(AnchorPane);
    }

    @Override
    @FXML
    public void About() {
        new Menu().initialize().About(AnchorPane);
    }

    /**
     * Gets the scene for the playground.
     *
     * @return The scene for the playground.
     */
    public static Scene getScene() {
        logger.info("Getting playground scene");
        return SceneInterface.getScene(Playground.class, "playground.fxml"); // Load the playground scene
    }

    /**
     * Configures the interactive buttons and their initial settings.
     * The buttons are grouped together based on their functionality.
     * The buttons are styled to indicate their current state.
     */
    private void setupInteractiveButtons() {
        ToggleButton addOrRemove = new ToggleButton("ADD MODE"); // Toggle for switch between add and remove modes
        addOrRemove.setStyle("-fx-background-color: LightBlue;");
        addOrRemove.setOnAction(e -> AddOrRemoveAction());
        addGroup.add(addOrRemove);
        removeGroup.add(addOrRemove);

        ToggleGroup placeSelection = new ToggleGroup(); // Toggle group for selecting the type of object to add

        ToggleButton autoRobot = new ToggleButton("AUTO ROBOT"); // Toggle for adding automatic robots
        autoRobot.setOnAction(e -> AutoRobotAction());
        autoRobot.setToggleGroup(placeSelection);
        addGroup.add(autoRobot);

        ToggleButton manualRobot = new ToggleButton("MANUAL ROBOT"); // Toggle for adding manual robots
        manualRobot.setOnAction(e -> ManualRobotAction());
        manualRobot.setToggleGroup(placeSelection);
        addGroup.add(manualRobot);

        ToggleButton obstacle = new ToggleButton("OBSTACLE"); // Toggle for adding obstacles
        obstacle.setOnAction(e -> ObstacleAction());
        obstacle.setToggleGroup(placeSelection);
        addGroup.add(obstacle);


        ToggleButton startPause = new ToggleButton("START"); // Toggle for starting and pausing the simulation
        startPause.setOnAction(e -> StartPauseAction());
        startPause.setStyle("-fx-background-color: lightgreen;");
        startGroup.add(startPause);
        pauseGroup.add(startPause);

        Button left = new Button("LEFT"); // Button for turning the selected robot left
        left.setOnAction(e -> LeftAction());
        moveGroup.add(left);

        Button go = new Button("GO"); // Button for moving the selected robot forward
        go.setOnAction(e -> GoAction());
        moveGroup.add(go);

        Button right = new Button("RIGHT"); // Button for turning the selected robot right
        right.setOnAction(e -> RightAction());
        moveGroup.add(right);

        Button playback = new Button("PLAYBACK"); // Button for starting the playback of the simulation
        playback.setOnAction(e -> StartPlayBack());
        playback.setStyle("-fx-background-color: Orange;");
        pauseGroup.add(playback);

        Button removeAll = new Button("REMOVE ALL"); // Button for removing all objects from the room
        removeAll.setOnAction(e -> RemoveAllAction());
        removeGroup.add(removeAll);

        Button removeObstacles = new Button("REMOVE OBSTACLES"); // Button for removing all obstacles from the room
        removeObstacles.setOnAction(e -> RemoveObstaclesAction());
        removeGroup.add(removeObstacles);

        Button removeRobots = new Button("REMOVE ROBOTS"); // Button for removing all robots from the room
        removeRobots.setOnAction(e -> RemoveRobotsAction());
        removeGroup.add(removeRobots);

        // Set the minimum size of the buttons groups to ensure consistent layout
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
        for (Node node : moveGroup) {
            ((Region) node).setMinSize(100, 30);
        }

        // Add the buttons to the program interface
        HBoxBttnUp.getChildren().addAll(addGroup);
        HBoxBttnDown.getChildren().addAll(pauseGroup);
    }

    /**
     * Disables all user interface buttons during playback and re-enables them after playback finishes.
     * This method improves application responsiveness by not blocking the UI thread and provides clear feedback.
     */
    private void disableButtonsDuringPlayback() {
        // Helper method to toggle the disable state of buttons
        toggleButtonState(true);  // Disable all buttons
        // Asynchronously wait for playback to finish
        new Thread(() -> {
            while (!AbstractRobot.isPlayBackFinished()) {
                try {
                    Thread.sleep(100); // Reduce CPU usage
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupted status
                    logger.severe("Playback waiting thread was interrupted.");
                    return;
                }
            }
            // Re-enable buttons on the UI thread after playback finishes
            Platform.runLater(() -> toggleButtonState(false));
        }).start();
    }

    /**
     * Helper method to toggle the enable/disable state of UI button groups.
     * @param state true to disable buttons, false to enable them.
     */
    private void toggleButtonState(boolean state) {
        logger.info("Toggling button state: " + state);
        List<List<Node>> buttonGroups = List.of(addGroup, removeGroup, startGroup, pauseGroup, moveGroup);
        for (List<Node> group : buttonGroups) {
            for (Node node : group) {
                node.setDisable(state);
            }
        }
    }

    /**
     * Removes all objects from the current room.
     */
    public void RemoveAllAction() {
        logger.info("Remove all button pressed");
        Room room = Room.getInstance();
        room.removeAll(); // Remove all objects from the room
    }

    /**
     * Removes all obstacles from the current room.
     */
    public void RemoveObstaclesAction() {
        logger.info("Remove obstacles button pressed");
        Room room = Room.getInstance();
        room.removeObstacles(); // Remove all obstacles from the room
    }

    /**
     * Removes all robots from the current room.
     */
    public void RemoveRobotsAction() {
        logger.info("Remove robots button pressed");
        Room room = Room.getInstance(); // Remove all robots from the room
        room.removeRobots();
    }

    /**
     * Updates the state of the room and room objects on each tick of the simulation.
     * Calls the {@link Room#tick()} method to update the room state.
     */
    public void tick() {
        if (currentMode.equals("START")) {
            Room room = Room.getInstance();
            room.tick(); // Update the room state
        }
    }

    /**
     * Toggles the add/remove mode in the simulation.
     * When enabled, users can add robots or obstacles by clicking on the canvas.
     * When disabled, this method disables buttons, and users can remove robots or obstacles from the canvas.
     */
    public void AddOrRemoveAction() {
        ToggleButton addBttn = (ToggleButton) addGroup.get(0);
        if (addBttn.isSelected()){
            addBttn.setText("REMOVE MODE");
            currentMode = "REMOVE";
            lastEditMode = "REMOVE";
            addBttn.setStyle("-fx-background-color: LightPink;");
            logger.info("Playground REMOVE MODE activated");
            HBoxBttnUp.getChildren().clear(); // Clear the current button group
            HBoxBttnUp.getChildren().addAll(removeGroup); // Switch to remove button group
        } else {
            addBttn.setText("ADD MODE");
            currentMode = "ADD";
            lastEditMode = "ADD";
            addBttn.setStyle("-fx-background-color: LightBlue;");
            logger.info("Playground ADD MODE activated");
            HBoxBttnUp.getChildren().clear(); // Clear the current button group
            HBoxBttnUp.getChildren().addAll(addGroup); // Switch to add  button group
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
        ToggleButton strtbttn = (ToggleButton) startGroup.get(0);
        if (!strtbttn.isSelected()){
            currentMode = lastEditMode;
            tickThread.interrupt(); // Stop the simulation
            strtbttn.setText("START");
            Room.getInstance().stopSimulation(); // Stop the room simulation
            strtbttn.setStyle("-fx-background-color: lightgreen;");
            HBoxBttnDown.getChildren().clear();
            HBoxBttnUp.getChildren().clear();
            HBoxBttnDown.getChildren().addAll(pauseGroup);
            if (lastEditMode.equals("ADD"))
                HBoxBttnUp.getChildren().addAll(addGroup);
            else if (lastEditMode.equals("REMOVE"))
                HBoxBttnUp.getChildren().addAll(removeGroup);
        } else {
            if (!AbstractRobot.isPlayBackFinished()) {
                logger.warning("Cannot start simulation while playback is in progress");
                strtbttn.setSelected(false);
                return;
            }
            currentMode = "START";
            tickThread = new Thread(() -> {
                while (true) {
                    try {
                        tick(); // Update the room state
                        Thread.sleep((long) (tickPeriod * 0.95)); // Sleep for a short period
                    } catch (InterruptedException e) {
                        logger.warning("Tick thread interrupted");
                    }
                }
            });
            tickThread.start(); // Start the simulation
            Room.getInstance().startSimulation();
            strtbttn.setText("PAUSE");
            strtbttn.setStyle("-fx-background-color: yellow;");
            HBoxBttnDown.getChildren().clear();
            HBoxBttnUp.getChildren().clear();
            HBoxBttnDown.getChildren().addAll(startGroup);
            HBoxBttnUp.getChildren().addAll(moveGroup);
        }
    }

    /**
     * Turns the selected robot left.
     * This method is triggered when the user clicks the 'LEFT' button.
     * Calls the {@link ControlledRobot#turnLeft()} method to turn the selected robot left.
     */
    public void LeftAction(){
        ControlledRobot.getInstance().turnLeft();
    }

    /**
     * Moves the selected robot forward.
     * This method is triggered when the user clicks the 'GO' button.
     * Calls the {@link ControlledRobot#moveForward()} method to move the selected robot forward.
     */
    public void GoAction(){
        ControlledRobot.getInstance().moveForward();
    }

    /**
     * Turns the selected robot right.
     * This method is triggered when the user clicks the 'RIGHT' button.
     * Calls the {@link ControlledRobot#turnRight()} method to turn the selected robot right.
     */
    public void RightAction(){
        ControlledRobot.getInstance().turnRight();
    }

    /**
     * Handles the user clicking on the grid.
     * The method determines the cell that was clicked and the mouse button that was pressed,
     * and then calls the appropriate action based on the current mode and mouse button.
     *
     * @param e The mouse event that triggered the grid click.
     */
    public void GridClicked(MouseEvent e) {
        int x = (int) e.getX() / gridWidth;
        int y = (int) e.getY() / gridWidth;
        logger.info("Grid cell clicked: (" + x + ", " + y + ")");

        if (!AbstractRobot.isPlayBackFinished()) {
            logger.info("Playback is running, cannot handle mouse clicks");
            return;
        }

        if (e.getButton() == MouseButton.SECONDARY) {
            logger.info("Right mouse button clicked");
            HandleRightClick(x, y);
        }
        else if (e.getButton() == MouseButton.PRIMARY){
            logger.info("Left mouse button clicked");
            HandleLeftClick(x, y);
        }
        else {
            logger.warning("Unknown mouse button clicked");
        }

    }

    /**
     * Starts the playback of the simulation.
     * This method is triggered when the user clicks the 'PLAYBACK' button.
     * Calls the {@link Room#playBackTransition()} method to start the playback of the simulation.
     */
    public void StartPlayBack() {
        logger.info("Playback button pressed");
        Room room = Room.getInstance();
        room.playBackTransition();
        disableButtonsDuringPlayback();
    }

    /**
     * Constructs the grid layout for the playground.
     * The grid is used to display the room map and objects.
     * The grid is interactive, allowing users to add and remove objects by clicking on the cells.
     */
    private void GridPaneConstruct() {
        logger.info("Constructing GridPane");
        Room room = Room.getInstance();
        int width = room.getWidth();
        int height = room.getHeight();
        // Create a new GridPane
        grid = new GridPane();
        grid.setMinSize(width * gridWidth, height * gridWidth);
        grid.setMaxSize(width * gridWidth, height * gridWidth);
        grid.setStyle("-fx-background-color: #FFFFFF;");
        for (int i = 0; i < width; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(gridWidth));
        }
        for (int i = 0; i < height; i++) {
            grid.getRowConstraints().add(new RowConstraints(gridWidth));
        }
        grid.setOnMouseClicked(this::GridClicked);

        int user_max_width = (int) Screen.getPrimary().getBounds().getMaxX() - 100;
        int user_max_height = (int) Screen.getPrimary().getBounds().getMaxY() - 100;

        int anchor_min_width = height * gridWidth + 100;
        int anchor_min_height = width * gridWidth + 100;

        if (anchor_min_width > user_max_width || anchor_min_height > user_max_height) {
            // add scroll pane if the grid is too large
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(grid);
            HBoxGrid.getChildren().add(scrollPane);
            AnchorPane.setMinHeight(user_max_height);
            AnchorPane.setMinWidth(user_max_width);
        } else {
            HBoxGrid.getChildren().add(grid);
            AnchorPane.setMinHeight(height * gridWidth + 100);
            AnchorPane.setMinWidth(width * gridWidth + 100);
        }
        room.setGrid(grid);
        room.initGrid();
//        grid.setGridLinesVisible(true);
    }

    /**
     * Handles the user left mouse button clicking on the grid.
     * The method checks the current mode and then calls the appropriate action based on the mode:
     * <ul>
     *  <li>In 'START' mode, selects the robot at the clicked cell.
     *  <li>In 'ADD' mode, adds a robot or obstacle to the clicked cell.
     *  <li>In 'REMOVE' mode, removes the object at the clicked cell.
     * </ul>
     * @param x The x-coordinate of the cell that was clicked.
     * @param y The y-coordinate of the cell that was clicked.
     */
    private void HandleLeftClick(int x, int y){
        if (!AbstractRobot.isPlayBackFinished()) {
            logger.warning("Playback is running, cannot edit objects");
            return;
        }
        switch (currentMode) {
            case "START" -> {
                logger.info("Grid cell clicked in start mode, selecting robot");
                ControlledRobot.getInstance().setRobot(new Position(x, y));
            }
            case "ADD" -> {
                if (((ToggleButton) addGroup.get(1)).isSelected()) {
                    logger.info("Grid cell clicked in add mode, adding auto robot");
                    createRequest = "AUTO";
                    if (Room.getInstance().getObjectAt(new Position(x, y)) != null) {
                        logger.warning("Cannot add robot to occupied cell");
                        return;
                    }
                    OpenRobotDialog(); // Open the robot dialog, get the parameters
                    if (RobotDialog.validData) {
                        logger.info("Adding auto robot at position: (" + x + ", " + y + ")");
                        logger.info("Speed: " + RobotDialog.Speed + ", angle: " + RobotDialog.Angle + ", distance: " + RobotDialog.Distance);
                        AutomatedRobot robot = Room.getInstance().addAutoRobot(new Position(x, y));
                        robot.setSpeed(RobotDialog.Speed);
                        robot.setStepAngle(RobotDialog.Angle);
                        robot.setViewDistance(RobotDialog.Distance);
                    }
                }
                if (((ToggleButton) addGroup.get(2)).isSelected()) {
                    logger.info("Grid cell clicked in add mode, adding manual robot");
                    createRequest = "MANUAL";
                    if (Room.getInstance().getObjectAt(new Position(x, y)) != null) {
                        logger.warning("Cannot add robot to occupied cell");
                        return;
                    }
                    OpenRobotDialog();
                    if (RobotDialog.validData) {
                        logger.info("Adding manual robot at position: (" + x + ", " + y + ")");
                        logger.info("Speed: " + RobotDialog.Speed + ", angle: " + RobotDialog.Angle);
                        ManualRobot robot = Room.getInstance().addManualRobot(new Position(x, y));
                        robot.setSpeed(RobotDialog.Speed);
                        robot.setStepAngle(RobotDialog.Angle);
                    }
                }
                if (((ToggleButton) addGroup.get(3)).isSelected()) {
                    logger.info("Grid cell clicked in add mode, adding obstacle");
                    if (Room.getInstance().getObjectAt(new Position(x, y)) != null) {
                        logger.warning("Cannot add obstacle to occupied cell");
                        return;
                    }
                    logger.info("Adding obstacle at position: (" + x + ", " + y + ")");
                    Room.getInstance().addObstacle(new Position(x, y));
                }
            }
            case "REMOVE" -> {
                logger.info("Grid cell clicked in remove mode, removing object");
                Room.getInstance().removeFrom(new Position(x, y));
            }
            default -> logger.finest("Grid cell clicked in unknown mode!!!");
        }
    }

    /**
     * Handles the user right mouse button clicking on the grid.
     * The method checks the contents of the clicked cell and then calls the appropriate action based on the object:
     * <ul>
     *  <li>If an automatic or manual robot is clicked, opens the robot dialog to update its parameters.
     *  <li>If an empty cell or obstacle is clicked, performs no action.
     * </ul>
     * @param x The x-coordinate of the cell that was clicked.
     * @param y The y-coordinate of the cell that was clicked.
     */
    private void HandleRightClick(int x, int y){
        if (!AbstractRobot.isPlayBackFinished()) {
            logger.warning("Playback is running, cannot edit objects");
            return;
        }

        ToggleButton strtbttn = (ToggleButton) startGroup.get(0);
        if(strtbttn.isSelected()) {
            logger.info("Cannot edit objects in 'START' mode");
            return;
        }

        var object = Room.getInstance().getObjectAt(new Position(x, y));
        if (object instanceof AutomatedRobot robot) {
            logger.info("Right clicked on auto robot");
            createRequest = "AUTO";
            OpenRobotDialog(); // Open the robot dialog, get the parameters
            if (RobotDialog.validData) {
                logger.info("Adding auto robot at position: (" + x + ", " + y + ")");
                logger.info("Speed: " + RobotDialog.Speed + ", angle: " + RobotDialog.Angle + ", distance: " + RobotDialog.Distance);
                robot.setSpeed(RobotDialog.Speed);
                robot.setStepAngle(RobotDialog.Angle);
                robot.setViewDistance(RobotDialog.Distance);
            }
        } else if (object instanceof ManualRobot robot) {
            logger.info("Right clicked on manual robot");
            createRequest = "MANUAL";
            OpenRobotDialog(); // Open the robot dialog, get the parameters
            if (RobotDialog.validData) {
                logger.info("Adding manual robot at position: (" + x + ", " + y + ")");
                logger.info("Speed: " + RobotDialog.Speed + ", angle: " + RobotDialog.Angle);
                robot.setSpeed(RobotDialog.Speed);
                robot.setStepAngle(RobotDialog.Angle);
            }
        }
    }

    /**
     * Opens the robot dialog for updating the parameters of a robot.
     * This method is triggered when the user clicks mouse right button on a robot in 'PAUSE' mode.
     */
    private void OpenRobotDialog() {
        logger.info("Opening robot dialog");
        Stage dialog = new Stage();
        dialog.initOwner(AnchorPane.getScene().getWindow());
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setTitle("Robot parameters");
        Scene robotDialog = RobotDialog.getScene();
        dialog.setScene(robotDialog);
        dialog.setResizable(false);
        dialog.showAndWait(); // Show the dialog and wait for user input
    }
}
