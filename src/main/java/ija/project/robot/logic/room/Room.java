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
            if (robot instanceof AutomatedRobot) {
                ((AutomatedRobot) robot).tick();
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
    public void runAutomatedRobots() throws InterruptedException {
        for (AbstractRobot robot : robots) {
            if (robot instanceof AutomatedRobot) {
                ((AutomatedRobot) robot).startMoving();
                // run robot in separate thread
                new Thread(() -> {
                    try {
                        ((AutomatedRobot) robot).run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
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
        List<ImageView> obstaclesImageViews = new ArrayList<>();
        List<Position> obstaclesPositions = new ArrayList<>();
        for (Obstacle obstacle : obstacles) {
            obstaclesPositions.add(obstacle.getPosition());
        }
        for (int i = 0; i < grid.getChildren().size(); i++) {
            if (grid.getChildren().get(i) instanceof ImageView) {
                obstaclesImageViews.add((ImageView) grid.getChildren().get(i));
            }
        }
        for (ImageView iv : obstaclesImageViews) {
            int x = GridPane.getColumnIndex(iv);
            int y = GridPane.getRowIndex(iv);
            Position pos = new Position(x, y);
            if (obstaclesPositions.contains(pos)) {
                grid.getChildren().remove(iv);
            }
        }
        obstacles.clear();
    }

    public void removeRobots() {
        logger.log(System.Logger.Level.INFO,
                "Robots removed from room");
        List<ImageView> robotImageViews = new ArrayList<>();
        List<Position> robotPositions = new ArrayList<>();
        for (AbstractRobot robot : robots) {
            robotPositions.add(robot.getPosition());
            if (robot instanceof ManualRobot) {
                ControlledRobot.getInstance().notifyRemovedRobot((ManualRobot) robot);
            }
        }
        for (int i = 0; i < grid.getChildren().size(); i++) {
            if (grid.getChildren().get(i) instanceof ImageView) {
                robotImageViews.add((ImageView) grid.getChildren().get(i));
            }
        }
        for (ImageView iv : robotImageViews) {
            int x = GridPane.getColumnIndex(iv);
            int y = GridPane.getRowIndex(iv);
            Position pos = new Position(x, y);
            if (robotPositions.contains(pos)) {
                grid.getChildren().remove(iv);
            }
        }
        robots.clear();
    }

    public void removeFrom(Position pos) {
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
        for (AbstractRobot robot : robots) {
            if (robot.getPosition().equals(pos)) {
                robots.remove(robot);
                if (robot instanceof ManualRobot) {
                    ControlledRobot.getInstance().notifyRemovedRobot((ManualRobot) robot);
                }
                return;
            }
        }
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPosition().equals(pos)) {
                obstacles.remove(obstacle);
                return;
            }
        }
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
    public List<AbstractRobot> getRobots() {
        return robots;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Position pos = new Position(j, i);
                if (isPositionFree(pos)) {
                    sb.append("*");
                } else {
                    // if obstacle is on position print O
                    for (Obstacle obstacle : obstacles) {
                        if (obstacle.getPosition().equals(pos)) {
                            sb.append("O");
                            break;
                        }
                    }
                    // if robot is on position print R
                    for (AbstractRobot robot : robots) {
                        if (robot.getPosition().equals(pos)) {
                            if (robot instanceof ManualRobot) {
                                sb.append("M"); // todo
                            } else {
                                sb.append("A"); // todo
                            }
                        }
                    }
                }
                if (j != width - 1) sb.append(" ");
            }
            if (i != height - 1) sb.append("\n");
        }
        return sb.toString();
    }

    public void loadRoomConfiguration(String configuration) {
        List<String[]> linesList = new ArrayList<>();
        String[] lines = configuration.split("\n");
        int len_x = -1;
        for (int i = 0; i < lines.length; i++) {
            linesList.add(lines[i].split(" "));
            if (len_x == -1) {
                len_x = linesList.get(i).length;
            } else if (len_x != linesList.get(i).length) {
                throw new IllegalArgumentException("Invalid room configuration");
            }
        }
        int x_dim = linesList.get(0).length;
        int y_dim = linesList.size();
        setDimensions(x_dim, y_dim);

        for (int i = 0; i < y_dim; i++) {
            for (int j = 0; j < x_dim; j++) {
                switch (linesList.get(i)[j]) {
                    case "O" -> addObstacle(new Position(j, i));
                    case "M" -> addManualRobot(new Position(j, i));
                    case "A" -> addAutoRobot(new Position(j, i));
                    case "*" -> {}
                    default -> throw new IllegalArgumentException("Invalid room configuration");
                }
            }
        }
    }

    public void loadRoomConfiguration(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            loadRoomConfiguration(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public String[][] getRoomConfigurationArray() {
        String[][] room = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Position pos = new Position(j, i);
                if (isPositionFree(pos)) {
                    room[i][j] = "*";
                } else {
                    // if obstacle is on position print O
                    for (Obstacle obstacle : obstacles) {
                        if (obstacle.getPosition().equals(pos)) {
                            room[i][j] = "O";
                            break;
                        }
                    }
                    // if robot is on position print R
                    for (AbstractRobot robot : robots) {
                        if (robot.getPosition().equals(pos)) {
                            if (robot instanceof ManualRobot) {
                                room[i][j] = "M"; // todo
                            } else {
                                room[i][j] = "A"; // todo
                            }
                        }
                    }
                }
            }
        }
        return room;
    }
}
