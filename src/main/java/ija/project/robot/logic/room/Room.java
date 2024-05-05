package ija.project.robot.logic.room;

import ija.project.robot.gui.logic.ControlledRobot;
import ija.project.robot.logic.common.AbstractRoomObject;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.robots.AbstractRobot;
import ija.project.robot.logic.robots.AutomatedRobot;
import ija.project.robot.logic.robots.ManualRobot;
import ija.project.robot.maps.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;


/**
 * Manages the simulation environment for robots and obstacles, including their placement,
 * movement, and interaction within a defined grid.
 */
public class Room {

    private int width;
    private int height;
    private final List<AbstractRobot> robots = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();
    private final System.Logger logger;
    private static Room instance = null;
    private GridPane grid;

    /**
     * Private constructor for singleton pattern, initializes the logger.
     */
    private Room() {
        logger = System.getLogger("Room");
        logger.log(System.Logger.Level.INFO,
                "Room created");
    }

    /**
     * Provides the single instance of this class.
     *
     * @return the single instance of Room.
     */
    public static Room getInstance() {
        if (instance == null) {
            instance = new Room();
        }
        return instance;
    }

    /**
     * Sets the dimensions of the room.
     *
     * @param width  the width of the room in number of cells.
     * @param height the height of the room in number of cells.
     * @throws IllegalArgumentException if the width or height is non-positive.
     */
    public void setDimensions(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Room dimensions must be positive");
        }
        this.width = width;
        this.height = height;
        logger.log(System.Logger.Level.INFO,
                "Room dimensions set to " + width + "x" + height);
    }

    /**
     * Sets the grid pane associated with this room.
     *
     * @param grid the GridPane to set.
     */
    public void setGrid(GridPane grid) {
        this.grid = grid;
    }

    /**
     * Initializes the grid by placing all room objects on it.
     */
    public void initGrid() {
        grid.getChildren().clear();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Position pos = new Position(j, i);
                if (isPositionFree(pos)) continue;
                logger.log(System.Logger.Level.INFO,
                        "Object added to grid at " + pos);
                AbstractRoomObject obj = getObjectAt(pos);
                ImageView imageView = obj.getImageView();
                grid.add(imageView, j, i);
            }
        }
    }

    /**
     * Clears all robots and obstacles from the room.
     */
    public void clear() {
        robots.clear();
        obstacles.clear();
        logger.log(System.Logger.Level.INFO,
                "Room cleared");
    }

    /**
     * Sends a 'tick' signal to all robots, used for movement and actions in time steps.
     */
    public void tick() {
        for (AbstractRobot robot : robots) {
            new Thread(robot::tick).start();
            logger.log(System.Logger.Level.INFO,
                    "Send tick to " + robot.getId());
        }
    }

    /**
     * Checks if a given position is within the boundaries of the room.
     *
     * @param pos the position to check.
     * @return true if the position is within the room, false otherwise.
     */
    public boolean isPositionInRoom(Position pos) {
        return pos.x() >= 0 && pos.x() < width && pos.y() >= 0 && pos.y() < height;
    }

    /**
     * Determines if a specified position is free (not occupied by any object).
     *
     * @param pos the position to check.
     * @return true if no object is at the specified position, false if it is occupied.
     */
    public boolean isPositionFree(Position pos) {
        for (AbstractRobot robot : robots) {
            if (robot.getPosition().equals(pos)) {
                return false;
            }
        }
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPosition().equals(pos)) {
                return false;
            }
        }
        return isPositionInRoom(pos);
    }

    /**
     * Adds an obstacle to the room at the specified position.
     *
     * @param pos the position to place the obstacle.
     * @throws IllegalArgumentException if the position is not free.
     */
    public void addObstacle(Position pos){
        if (!isPositionFree(pos)) {
            throw new IllegalArgumentException("Position is not free");
        }
        Obstacle obstacle = new Obstacle(pos);
        obstacles.add(obstacle);
        if (grid != null) grid.add(obstacle.getImageView(), pos.x(), pos.y());
        logger.log(System.Logger.Level.INFO,
                "Obstacle added to room");
    }

    /**
     * Adds an automated robot to the room at the specified position.
     *
     * @param pos the position to place the robot.
     * @return the robot that was added.
     * @throws IllegalArgumentException if the position is not free.
     */
    public AutomatedRobot addAutoRobot(Position pos) {
        if (!isPositionFree(pos)) {
            throw new IllegalArgumentException("Position is not free");
        }
        AutomatedRobot robot = new AutomatedRobot(pos);
        robots.add(robot);
        if (grid != null) grid.add(robot.getImageView(), pos.x(), pos.y());
        return robot;
    }

    /**
     * Adds a manual robot to the room at the specified position.
     *
     * @param pos the position to place the robot.
     * @return the robot that was added.
     * @throws IllegalArgumentException if the position is not free.
     */
    public ManualRobot addManualRobot(Position pos) {
        if (!isPositionFree(pos)) {
            throw new IllegalArgumentException("Position is not free");
        }
        ManualRobot robot = new ManualRobot(pos);
        robots.add(robot);
        if (grid != null) grid.add(robot.getImageView(), pos.x(), pos.y());
        return robot;
    }

    /**
     * Removes all object (robots and obstacle).
     *
     */
    public void removeAll() {
        removeObstacles();
        removeRobots();
    }

    /**
     * Removes all obstacles from the room.
     */
    public void removeObstacles() {
        logger.log(System.Logger.Level.INFO,
                "Obstacles removed from room");
        for (Obstacle obstacle : obstacles) {
            grid.getChildren().remove(obstacle.removeImageView());
        }
        obstacles.clear();
    }

    /**
     * Removes all robots from the room.
     */
    public void removeRobots() {
        logger.log(System.Logger.Level.INFO,
                "Robots removed from room");
        for (AbstractRobot robot : robots) {
            grid.getChildren().remove(robot.removeImageView());
            if (robot instanceof ManualRobot) {
                ControlledRobot.getInstance().notifyRemovedRobot((ManualRobot) robot);
            }
        }
        robots.clear();
    }

    /**
     * Removes an object from the room at the specified position.
     *
     * @param pos the position to remove the object from.
     */
    public void removeFrom(Position pos) {
        AbstractRoomObject obj = getObjectAt(pos);
        if (obj == null) {
            return;
        }
        if (obj instanceof AbstractRobot) {
            robots.remove(obj);
        } else if (obj instanceof Obstacle){
            obstacles.remove(obj);
        }
        grid.getChildren().remove(obj.getImageView());
    }

    /**
     * Stops all robots from moving.
     */
    public void stopSimulation() {
        for (AbstractRobot robot : robots) {
            if (robot instanceof AutomatedRobot) {
                ((AutomatedRobot) robot).stopMoving(); // send signal to stop moving
            } else if (robot instanceof ManualRobot) {
                ((ManualRobot) robot).pause(); // send signal to stop moving
            }
        }
    }

    /**
     * Starts all robots moving.
     */
    public void startSimulation() {
        for (AbstractRobot robot : robots) {
            if (robot instanceof AutomatedRobot) {
                ((AutomatedRobot) robot).startMoving(); // send signal to start moving
            } else if (robot instanceof ManualRobot) {
                ((ManualRobot) robot).start(); // send signal to start moving
            }
        }
    }

    /**
     * Gets the object at the specified position.
     *
     * @param pos the position to check.
     * @return the object at the specified position, or null if no object is present.
     */
    public AbstractRoomObject getObjectAt(Position pos) {
        for (AbstractRobot robot : robots) {
            if (robot.getPosition().equals(pos)) {
                return robot;
            }
        }
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPosition().equals(pos)) {
                return obstacle;
            }
        }
        return null;
    }

    /**
     * Gets the width of the room.
     *
     * @return the width of the room in number of cells.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the room.
     *
     * @return the height of the room in number of cells.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Plays back the transition of all robots.
     * Simulate the reverse movement of all robots.
     */
    public void playBackTransition() {
        ControlledRobot.getInstance().unselectRobot();
        for (AbstractRobot robot : robots) {
            robot.playBackTransition();
        }
    }

    /**
     * Converts the current room setup from a JSON representation and populates the room accordingly.
     *
     * @param room the JsonRoom object containing the definitions of robots and obstacles.
     */
    public void fromJsonRoom(JsonRoom room) {
        setDimensions(room.cols, room.rows);
        for (JsonObstacle jsonObstacle : room.obstacles) {
            Position pos = new Position(jsonObstacle.position.x, jsonObstacle.position.y);
            if (!isPositionFree(pos)) {
                logger.log(System.Logger.Level.WARNING,"Something at " + pos + " already exists");
                continue;
            }
            obstacles.add(new Obstacle(pos));
        }
        for (JsonManualRobot jsonRobot : room.manual_robots) {
            Position pos = new Position(jsonRobot.position.x, jsonRobot.position.y);
            if (!isPositionFree(pos)) {
                logger.log(System.Logger.Level.WARNING,"Something at " + pos + " already exists");
                continue;
            }
            ManualRobot mr = new ManualRobot(pos);
            mr.setSpeed(jsonRobot.speed);
            mr.setStartAngle(jsonRobot.start_angle);
            mr.setStepAngle(jsonRobot.rotation_angle);
            robots.add(mr);
        }
        for (JsonAutoRobot jsonRobot : room.auto_robots) {
            Position pos = new Position(jsonRobot.position.x, jsonRobot.position.y);
            if (!isPositionFree(pos)) {
                logger.log(System.Logger.Level.WARNING,"Something at " + pos + " already exists");
                continue;
            }
            AutomatedRobot ar = new AutomatedRobot(pos);
            ar.setSpeed(jsonRobot.speed);
            ar.setStartAngle(jsonRobot.start_angle);
            if (jsonRobot.rotation_direction.equals("cw")) {
                ar.setStepAngle(-jsonRobot.rotation_angle);
            } else {
                ar.setStepAngle(jsonRobot.rotation_angle);
            }
            ar.setViewDistance(jsonRobot.view_distance);
            robots.add(ar);
        }
    }

    /**
     * Creates a JSON representation of the current room state, including all robots and obstacles.
     *
     * @return A JsonRoom object representing the current state of the room.
     */
    public JsonRoom getJsonRoom() {
        JsonRoom jsonRoom = new JsonRoom();
        jsonRoom.rows = height;
        jsonRoom.cols = width;
        for (AbstractRobot robot : robots) {
            if (robot instanceof ManualRobot) {
                JsonManualRobot jsonRobot = new JsonManualRobot();
                jsonRobot.position = new JsonPosition();
                jsonRobot.position.x = robot.getPosition().x();
                jsonRobot.position.y = robot.getPosition().y();
                jsonRobot.speed = robot.getSpeed();
                jsonRobot.start_angle = robot.getCurrentAngle();
                jsonRobot.rotation_angle = robot.getStepAngle();
                jsonRoom.manual_robots.add(jsonRobot);
            } else if (robot instanceof AutomatedRobot) {
                JsonAutoRobot jsonRobot = new JsonAutoRobot();
                jsonRobot.position = new JsonPosition();
                jsonRobot.position.x = robot.getPosition().x();
                jsonRobot.position.y = robot.getPosition().y();
                jsonRobot.speed = robot.getSpeed();
                jsonRobot.start_angle = robot.getCurrentAngle();
                jsonRobot.rotation_angle = robot.getStepAngle();
                jsonRobot.view_distance = ((AutomatedRobot) robot).getViewDistance();
                if (robot.getStepAngle() < 0) {
                    jsonRobot.rotation_direction = "cw";
                } else {
                    jsonRobot.rotation_direction = "ccw";
                }
                jsonRoom.auto_robots.add(jsonRobot);
            }
        }
        for (Obstacle obstacle : obstacles) {
            JsonObstacle jsonObstacle = new JsonObstacle();
            jsonObstacle.position = new JsonPosition();
            jsonObstacle.position.x = obstacle.getPosition().x();
            jsonObstacle.position.y = obstacle.getPosition().y();
            jsonRoom.obstacles.add(jsonObstacle);
        }
        return jsonRoom;
    }

    /**
     * Converts the current room state to a JSON string.
     *
     * @return A string representation of the JSON room.
     */
    public String getJsonRoomString() {
        JsonRoom jsonRoom = getJsonRoom();
        return jsonRoom.toString();
    }

    /**
     * Returns a string representation of the room state formatted as JSON.
     *
     * @return JSON formatted string of the room.
     */
    @Override
    public String toString() {
        return getJsonRoomString();
    }

    /**
     * Returns a list of all robots in the room.
     *
     * @return A list of AbstractRobot instances.
     */
    public List<AbstractRobot> getRobots() {
        return robots;
    }
}
