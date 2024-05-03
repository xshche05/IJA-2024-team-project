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
            ManualRobot prev = robot;
            robot = (ManualRobot) obj;
            if (prev == robot) {
                robot = null;
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
        }
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
            logger.info("Controlled robot moved forward, current position: " + robot.getPosition());
        }).start();
    }

    public void turnLeft() {
        if (robot == null) {
            return;
        }
        logger.info("Robot controller got request to turn left");
        new Thread(() -> {
            robot.rotateLeft();
            logger.info("Controlled robot turned left, current angle: " + robot.getCurrentAngle());
        }).start();
    }

    public void turnRight() {
        if (robot == null) {
            return;
        }
        logger.info("Robot controller got request to turn right");
        new Thread(() -> {
            robot.rotateRight();
            logger.info("Controlled robot turned right, current angle: " + robot.getCurrentAngle());
        }).start();
    }
}
