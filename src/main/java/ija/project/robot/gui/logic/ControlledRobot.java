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

public class ControlledRobot {

    private ManualRobot robot;

    private static ControlledRobot instance;

    private ImageView imageView;

    private ControlledRobot() {
        logger.info("Robot controller instance was created");
    }

    public static ControlledRobot getInstance() {
        if (instance == null) {
            instance = new ControlledRobot();
        }
        return instance;
    }

    public void setRobot(Position pos) {
        AbstractRoomObject obj = Room.getInstance().getObjectAt(pos);
        if (obj instanceof ManualRobot) {
            ManualRobot new_robot = (ManualRobot) obj;
            ManualRobot old_robot = this.robot;
            if (old_robot != null) {
                old_robot.unsetControlled();
            }
            if (new_robot == old_robot) {
                this.robot = null;
                this.imageView = null;
                logger.info("Robot controller is now unlinked from robot (" + old_robot.getId() + ")");
                return;
            }
            new_robot.setControlled();
            this.robot = new_robot;
            this.imageView = robot.getImageView();
            logger.info("Robot controller is now linked to robot (" + new_robot.getId() + ")");
        } else {
            logger.info("No manual robot found at position (" + pos + ")");
        }
    }

    public void unselectRobot() {
        if (robot == null) {
            return;
        }
        setRobot(robot.getPosition());
    }

    public ImageView getImageView() {
        if (robot == null) {
            return null;
        }
        return imageView;
    }

    public ManualRobot getRobot() {
        return robot;
    }

    public void notifyRemovedRobot(ManualRobot robot) {
        if (this.robot == robot) {
            this.robot = null;
            imageView.setImage(null);
            imageView = null;
            logger.info("Robot controller is now unlinked from robot (" + robot.getId() + ")");
        }
    }

    public void moveForward() {
        if (robot == null) {
            return;
        }
        logger.info("Robot controller got request to move forward");
        new Thread(() -> {
            robot.move();
        }).start();
    }

    public void turnLeft() {
        if (robot == null) {
            return;
        }
        logger.info("Robot controller got request to turn left");
        new Thread(() -> {
            robot.rotateLeft();
        }).start();
    }

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
