package ija.project.robot.room;

import ija.project.robot.common.Position;
import ija.project.robot.robots.AbstractRobot;
import ija.project.robot.robots.AutomatedRobot;
import ija.project.robot.robots.ManualRobot;

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

    public void clearRoom() {
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
                                sb.append(robot.getId()); // todo
                            } else {
                                sb.append(robot.getId()); // todo
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
}
