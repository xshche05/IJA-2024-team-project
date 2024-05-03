package ija.project.robot.gui.logic;

import ija.project.robot.logic.common.AbstractRoomObject;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.robots.ManualRobot;
import ija.project.robot.logic.room.Room;

import java.util.List;

public class ControlledRobot {

    private ManualRobot robot;

    private static ControlledRobot instance;

    private ControlledRobot() {
    }

    public static ControlledRobot getInstance() {
        if (instance == null) {
            instance = new ControlledRobot();
        }
        return instance;
    }

    public boolean setRobot(Position pos) {
        AbstractRoomObject obj = Room.getInstance().getObjectAt(pos);
        if (obj instanceof ManualRobot) {
            robot = (ManualRobot) obj;
            return true;
        }
        return false;
    }

    public ManualRobot getRobot() {
        return robot;
    }

    public void moveForward() {
        robot.move();
    }

    public void turnLeft() {
        robot.rotateLeft();
    }

    public void turnRight() {
        robot.rotateRight();
    }

}
