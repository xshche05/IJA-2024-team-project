package ija.project.robot.gui.logic;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.common.AbstractRoomObject;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.robots.ManualRobot;
import ija.project.robot.logic.room.Room;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

import static ija.project.robot.RobotApp.logger;

/**
 * Singleton class that manages the currently controlled robot within the simulation.
 * This class provides methods to set, move, and interact with a manually controlled robot.
 * It encapsulates all the logic needed to update the robot's state and visuals in response to user actions.
 */
public class ControlledRobot {

    private ManualRobot robot; // The robot currently being controlled
    private static ControlledRobot instance; // Singleton instance of the class
    private ImageView imageView; // The image view of the robot

    private ControlledRobot() {
        logger.info("Robot controller instance was created");
    }

    /**
     * Provides access to the singleton instance of {@code ControlledRobot}.
     * If no instance exists, a new one is created.
     *
     * @return The singleton instance of {@link ControlledRobot}.
     */
    public static ControlledRobot getInstance() {
        if (instance == null) {
            instance = new ControlledRobot();
        }
        return instance;
    }

    /**
     * Sets the robot to be controlled based on the specified position within the room.
     * If a robot is already being controlled, it checks if the robot can be safely switched.
     *
     * @param pos The position in the room where the robot to be controlled is located.
     */
    public void setRobot(Position pos) {
        AbstractRoomObject obj = Room.getInstance().getObjectAt(pos);

        if (obj instanceof ManualRobot) { // Only manual robots can be controlled
            ManualRobot prev = robot;
            if (prev != null) {
                if (prev.getSemaphore().availablePermits() == 0) {
                    logger.warning("Robot is currently moving, cannot be changed to another robot");
                    return;
                }
            }
            robot = (ManualRobot) obj;
            if (prev == robot) {
                robot = null;
                imageView.setImage(null);
                Room.getInstance().updateViewAt(pos);
                logger.info("Robot controller is now unlinked from robot (" + prev.getId() + ")");
                return;
            }
            if (prev != null) {
                Room.getInstance().updateViewAt(prev.getPosition());
                logger.info("Robot controller is now unlinked from robot (" + prev.getId() + ")");
            }
            if (imageView != null) {
                imageView.setImage(null);
            }

            Image image = new Image(Objects.requireNonNull(ManualRobot.class.getResourceAsStream("selected_robot.png")));
            imageView = new ImageView(image);
            imageView.setFitHeight(Playground.gridWidth);
            imageView.setFitWidth(Playground.gridWidth);
            imageView.setRotate(robot.getCurrentAngle());
            logger.info("Robot controller is now linked to robot (" + robot.getId() + ")");
            Room.getInstance().updateViewAt(pos);
        } else {
            logger.info("No manual robot found at position (" + pos + ")");
        }
    }

    /**
     * Returns the image view of the currently controlled robot.
     *
     * @return The {@link ImageView} of the currently controlled robot, or {@code null} if no robot is controlled.
     */
    public ImageView getImageView() {
        if (robot == null) {
            return null;
        }
        return imageView;
    }

    /**
     * Returns the currently controlled robot.
     *
     * @return The currently controlled {@link ManualRobot}, or {@code null} if no robot is controlled.
     */
    public ManualRobot getRobot() {
        return robot;
    }

    /**
     * Notifies the controller that a robot is no longer linked or has been removed.
     * If the specified robot is the currently controlled one, it unlinks it.
     *
     * @param robot The {@link ManualRobot} that was removed.
     */
    public void notifyRemovedRobot(ManualRobot robot) {
        if (this.robot == robot) {
            this.robot = null;
            imageView.setImage(null);
            imageView = null;
            logger.info("Robot controller is now unlinked from robot (" + robot.getId() + ")");
        }
    }

    /**
     * Moves the currently controlled robot forward.
     * If no robot is controlled, this method does nothing.
     */
    public void moveForward() {
        if (robot == null) {
            return;
        }
        logger.info("Robot controller got request to move forward");
        new Thread(() -> {
            robot.move();
        }).start();
    }

    /**
     * Moves the currently controlled robot backward.
     * If no robot is controlled, this method does nothing.
     */
    public void turnLeft() {
        if (robot == null) {
            return;
        }
        logger.info("Robot controller got request to turn left");
        new Thread(() -> {
            robot.rotateLeft();
        }).start();
    }

    /**
     * Moves the currently controlled robot backward.
     * If no robot is controlled, this method does nothing.
     */
    public void turnRight() {
        if (robot == null) {
            return;
        }
        logger.info("Robot controller got request to turn right");
        new Thread(() -> {
            robot.rotateRight();
        }).start();
    }
}
