package ija.project.robot.control;

import ija.project.robot.robots.AbstractRobot;
import ija.project.robot.robots.ManualRobot;
import ija.project.robot.room.Room;

import java.util.List;

public class ControlPlate {
    private int robot_id = -1;
    private ManualRobot robot = null;

    private final System.Logger logger = System.getLogger("ControlPlate");

    private static ControlPlate instance = null;

    private ControlPlate() {
    }

    public static ControlPlate getInstance() {
        if (instance == null) {
            instance = new ControlPlate();
        }
        return instance;
    }

    public void setRobot_id(int robot_id) {
        this.robot_id = robot_id;
        List<AbstractRobot> robots = Room.getInstance().getRobots();
        for (AbstractRobot robot : robots) {
            if (robot.getId() == robot_id) {
                this.robot = (ManualRobot) robot;
                logger.log(System.Logger.Level.INFO,
                        "Robot with id " + robot_id + " selected");
                return;
            }
        }
    }

    public void rotateRobotLeft(){
        if(this.robot == null){
            return;
        }
        robot.rotateLeft();
    }

    public void rotateRobotRight(){
        if(this.robot == null){
            return;
        }
        robot.rotateRight();
    }

    public void moveRobot(){
        if(this.robot == null){
            return;
        }
        robot.move();
    }
}

