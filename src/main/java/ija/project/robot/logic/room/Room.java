package ija.project.robot.logic.room;

import ija.project.robot.gui.logic.ControlledRobot;
import ija.project.robot.logic.common.AbstractRoomObject;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.robots.AbstractRobot;
import ija.project.robot.logic.robots.AutomatedRobot;
import ija.project.robot.logic.robots.ManualRobot;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Room {

    private int width;
    private int height;
    private final List<AbstractRobot> robots = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();
    private final System.Logger logger;
    private static Room instance = null;
    private GridPane grid;

    private Room() {
        logger = System.getLogger("Room");
        logger.log(System.Logger.Level.INFO,
                "Room created");
    }

    public static Room getInstance() {
        if (instance == null) {
            instance = new Room();
        }
        return instance;
    }

    public void setDimensions(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Room dimensions must be positive");
        }
        this.width = width;
        this.height = height;
        logger.log(System.Logger.Level.INFO,
                "Room dimensions set to " + width + "x" + height);
    }

    public void setGrid(GridPane grid) {
        this.grid = grid;
    }

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

    public void clear() {
        robots.clear();
        obstacles.clear();
        logger.log(System.Logger.Level.INFO,
                "Room cleared");
    }

    public void tick() {
        for (AbstractRobot robot : robots) {
            if (robot instanceof AutomatedRobot automatedRobot) {
                new Thread(automatedRobot::tick).start();
                logger.log(System.Logger.Level.INFO,
                        "Send tick to " + robot.getId());
            }
        }
    }

    public boolean isPositionInRoom(Position pos) {
        return pos.x() >= 0 && pos.x() < width && pos.y() >= 0 && pos.y() < height;
    }
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
    public void runAutomatedRobots() {
        for (AbstractRobot robot : robots) {
            if (robot instanceof AutomatedRobot) {
                ((AutomatedRobot) robot).startMoving();
            }
        }
    }
    public AutomatedRobot addAutoRobot(Position pos) {
        if (!isPositionFree(pos)) {
            throw new IllegalArgumentException("Position is not free");
        }
        AutomatedRobot robot = new AutomatedRobot(pos);
        robots.add(robot);
        grid.add(robot.getImageView(), pos.x(), pos.y());
        return robot;
    }
    public ManualRobot addManualRobot(Position pos) {
        if (!isPositionFree(pos)) {
            throw new IllegalArgumentException("Position is not free");
        }
        ManualRobot robot = new ManualRobot(pos);
        robots.add(robot);
        grid.add(robot.getImageView(), pos.x(), pos.y());
        return robot;
    }
    public void removeAll() {
        removeObstacles();
        removeRobots();
    }
    public void removeObstacles() {
        logger.log(System.Logger.Level.INFO,
                "Obstacles removed from room");
        for (Obstacle obstacle : obstacles) {
            grid.getChildren().remove(obstacle.removeImageView());
        }
        obstacles.clear();
    }
    public void stopSimulation() {
        for (AbstractRobot robot : robots) {
            if (robot instanceof AutomatedRobot) {
                ((AutomatedRobot) robot).stopMoving();
            }
        }
    }
    public void startSimulation() {
        runAutomatedRobots();
    }
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
    public void addObstacle(Position pos){
        if (!isPositionFree(pos)) {
            throw new IllegalArgumentException("Position is not free");
        }
        Obstacle obstacle = new Obstacle(pos);
        obstacles.add(obstacle);
        grid.add(obstacle.getImageView(), pos.x(), pos.y());
        logger.log(System.Logger.Level.INFO,
                "Obstacle added to room");
    }
    @Override
    public String toString() {
        // todo
        return "Room configuration";
    }
    public void loadRoomConfiguration(File file) {
        // todo
        logger.log(System.Logger.Level.INFO, "Room configuration loaded from file " + file.getName());
    }
    public String getRoomConfiguration() {
        return toString();
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public void updateViewAt(Position pos) {
        AbstractRoomObject obj = getObjectAt(pos);
        if (obj == null) {
            return;
        }
        ImageView imageView = obj.getImageView();
        List<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < grid.getChildren().size(); i++) {
            if (grid.getChildren().get(i) instanceof ImageView) {
                imageViews.add((ImageView) grid.getChildren().get(i));
            }
        }
        for (ImageView iv : imageViews) {
            int x = GridPane.getColumnIndex(iv);
            int y = GridPane.getRowIndex(iv);
            if (x == pos.x() && y == pos.y()) {
                grid.getChildren().remove(iv);
                break;
            }
        }
        grid.add(imageView, pos.x(), pos.y());
    }
    public void playBackTransition() {
        for (AbstractRobot robot : robots) {
            robot.playBackTransition();
        }
    }
}
