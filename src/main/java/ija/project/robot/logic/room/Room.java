package ija.project.robot.logic.room;

import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.robots.AbstractRobot;
import ija.project.robot.logic.robots.AutomatedRobot;
import ija.project.robot.logic.robots.ManualRobot;

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
        return robot;
    }
    public ManualRobot addManualRobot(Position pos) {
        if (!isPositionFree(pos)) {
            throw new IllegalArgumentException("Position is not free");
        }
        ManualRobot robot = new ManualRobot(pos);
        robots.add(robot);
        return robot;
    }
    public void addObstacle(Position pos){
        if (!isPositionFree(pos)) {
            throw new IllegalArgumentException("Position is not free");
        }
        obstacles.add(new Obstacle(pos));
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
