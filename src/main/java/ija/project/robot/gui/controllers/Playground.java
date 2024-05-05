package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.MenuInterface;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.gui.logic.ControlledRobot;
import ija.project.robot.gui.logic.Menu;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.robots.AutomatedRobot;
import ija.project.robot.logic.robots.ManualRobot;
import ija.project.robot.logic.room.Room;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

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
    @FXML
    public MenuItem MenuNewFile;
    @FXML
    public HBox HBoxGrid;
    public HBox HBoxBttnDown;
    public HBox HBoxBttnUp;

    public static final int gridWidth = 28;
    public static final int tickPeriod = 1000; // in milliseconds
    private Timeline timeline;
    public GridPane grid;

    public static String createRequest;
    private final List<Node> addGroup = new ArrayList<>();
    private final List<Node> removeGroup = new ArrayList<>();
    private final List<Node> startGroup = new ArrayList<>();
    private final List<Node> pauseGroup = new ArrayList<>();
    private final List<Node> moveGroup = new ArrayList<>();

    private String currentMode = "ADD";
    private String lastEditMode = "ADD";

    public static final Semaphore playSemaphore = new Semaphore(1);



    /**
     * Initializes the controller by setting up the UI components, canvas, and initial settings for the game mode.
     * This method constructs the canvas based on the room dimensions, initializes the toggle buttons,
     * and sets up the default settings for adding, moving, and controlling robots or obstacles.
     */
    @FXML
    public void initialize() {
        createRequest = "NONE";
        GridPaneConstruct();
        ToggleButton addOrRemove = new ToggleButton("ADD MODE");
        addOrRemove.setStyle("-fx-background-color: LightBlue;");
        addOrRemove.setOnAction(e -> AddOrRemoveAction());
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


        ToggleButton startPause = new ToggleButton("START");
        startPause.setOnAction(e -> StartPauseAction());
        startPause.setStyle("-fx-background-color: lightgreen;");
        startGroup.add(startPause);
        pauseGroup.add(startPause);
        Button left = new Button("LEFT");
        left.setOnAction(e -> LeftAction());
        moveGroup.add(left);
        Button go = new Button("GO");
        go.setOnAction(e -> GoAction());
        moveGroup.add(go);
        Button right = new Button("RIGHT");
        right.setOnAction(e -> RightAction());
        moveGroup.add(right);

        Button playback = new Button("PLAYBACK");
        playback.setOnAction(e -> StartPlayBack());
        playback.setStyle("-fx-background-color: Orange;"); //
        pauseGroup.add(playback);

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
        for (Node node : moveGroup) {
            ((Region) node).setMinSize(100, 30);
        }

        HBoxBttnUp.getChildren().addAll(addGroup);
        currentMode = "ADD";
        lastEditMode = "ADD";
        HBoxBttnDown.getChildren().addAll(pauseGroup);

        setupTimeline();
    }

    /**
     * Initiates the manual creation of a new room map by user.
     * This method is triggered when the user clicks the 'Create New' button.
     */
    @FXML
    public void CreateNew() {
        new Menu().initialize().CreateNew(AnchorPane);
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

    @FXML
    public void LoadPredefinedMap1() {
        new Menu().initialize().LoadPredefinedMap1(AnchorPane);
    }

    @FXML
    public void LoadPredefinedMap2() {
        new Menu().initialize().LoadPredefinedMap2(AnchorPane);
    }

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
        return SceneInterface.getScene(Playground.class, "playground.fxml");
    }

    public void RemoveAllAction() {
        logger.info("Remove all button pressed");
        Room room = Room.getInstance();
        room.removeAll();
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

    private void setupTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(tickPeriod), e -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
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
            HBoxBttnUp.getChildren().clear();
            HBoxBttnUp.getChildren().addAll(removeGroup);
        } else {
            addBttn.setText("ADD MODE");
            currentMode = "ADD";
            lastEditMode = "ADD";
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
        ToggleButton strtbttn = (ToggleButton) startGroup.get(0);
        if (!strtbttn.isSelected()){
            timeline.stop();
            currentMode = lastEditMode;
            strtbttn.setText("START");
            Room.getInstance().stopSimulation();
            strtbttn.setStyle("-fx-background-color: lightgreen;");
            HBoxBttnDown.getChildren().clear();
            HBoxBttnUp.getChildren().clear();
            HBoxBttnDown.getChildren().addAll(pauseGroup);
            if (lastEditMode.equals("ADD"))
                HBoxBttnUp.getChildren().addAll(addGroup);
            else if (lastEditMode.equals("REMOVE"))
                HBoxBttnUp.getChildren().addAll(removeGroup);
        } else {
            timeline.play();
            currentMode = "START";
            Room.getInstance().startSimulation();
            strtbttn.setText("PAUSE");
            strtbttn.setStyle("-fx-background-color: yellow;");
            HBoxBttnDown.getChildren().clear();
            HBoxBttnUp.getChildren().clear();
            HBoxBttnDown.getChildren().addAll(startGroup);
            HBoxBttnUp.getChildren().addAll(moveGroup);
        }
    }

    public void LeftAction(){
        ControlledRobot.getInstance().turnLeft();
    }

    public void GoAction(){
        ControlledRobot.getInstance().moveForward();
    }

    public void RightAction(){
        logger.info("Right button pressed");
        ControlledRobot.getInstance().turnRight();
    }

    public void GridClicked(MouseEvent e) {
        int x = (int) e.getX() / gridWidth;
        int y = (int) e.getY() / gridWidth;
        logger.info("Grid cell clicked: (" + x + ", " + y + ")");
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

    public void StartPlayBack() {
        logger.info("Playback button pressed");
        Room room = Room.getInstance();
        room.playBackTransition();
    }

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

    private void HandleLeftClick(int x, int y){
        if (currentMode.equals("START")) {
            logger.info("Grid cell clicked in start mode, selecting robot");
            ControlledRobot.getInstance().setRobot(new Position(x, y));
        } else if (currentMode.equals("ADD")) {
            if (((ToggleButton)addGroup.get(1)).isSelected()) {
                logger.info("Grid cell clicked in add mode, adding auto robot");
                createRequest = "AUTO";
                if (Room.getInstance().getObjectAt(new Position(x, y)) != null) {
                    logger.warning("Cannot add robot to occupied cell");
                    return;
                }
                OpenRobotDialog();
                if (RobotDialog.validData) {
                    logger.info("Adding auto robot at position: (" + x + ", " + y + ")");
                    logger.info("Speed: " + RobotDialog.Speed + ", angle: " + RobotDialog.Angle + ", distance: " + RobotDialog.Distance);
                    AutomatedRobot robot = Room.getInstance().addAutoRobot(new Position(x, y));
                    robot.setSpeed(RobotDialog.Speed);
                    robot.setStepAngle(RobotDialog.Angle);
                    robot.setViewDistance(RobotDialog.Distance);
                }
            }
            if (((ToggleButton)addGroup.get(2)).isSelected()) {
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
            if (((ToggleButton)addGroup.get(3)).isSelected()) {
                logger.info("Grid cell clicked in add mode, adding obstacle");
                if (Room.getInstance().getObjectAt(new Position(x, y)) != null) {
                    logger.warning("Cannot add obstacle to occupied cell");
                    return;
                }
                logger.info("Adding obstacle at position: (" + x + ", " + y + ")");
                Room.getInstance().addObstacle(new Position(x, y));
            }
        } else if (currentMode.equals("REMOVE")) {
            logger.info("Grid cell clicked in remove mode, removing object");
            Room.getInstance().removeFrom(new Position(x, y));
        } else {
            logger.finest("Grid cell clicked in unknown mode!!!!!!!!!!!!!");
        }
    }

    private void HandleRightClick(int x, int y){
        var object = Room.getInstance().getObjectAt(new Position(x, y));
        if (object instanceof AutomatedRobot robot) {
            logger.info("Right clicked on auto robot");
            createRequest = "AUTO";
            OpenRobotDialog();
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
            OpenRobotDialog();
            if (RobotDialog.validData) {
                logger.info("Adding manual robot at position: (" + x + ", " + y + ")");
                logger.info("Speed: " + RobotDialog.Speed + ", angle: " + RobotDialog.Angle);
                robot.setSpeed(RobotDialog.Speed);
                robot.setStepAngle(RobotDialog.Angle);
            }
        }
    }

    private void OpenRobotDialog() {
        logger.info("Opening robot dialog");
        Stage dialog = new Stage();
        dialog.initOwner(AnchorPane.getScene().getWindow());
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setTitle("Robot parameters");
        Scene robotDialog = RobotDialog.getScene();
        dialog.setScene(robotDialog);
        dialog.setResizable(false);
        dialog.showAndWait();
    }

    public void tick() {
        Room room = Room.getInstance();
        room.tick();
    }
}
