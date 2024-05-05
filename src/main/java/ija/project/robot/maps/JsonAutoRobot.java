package ija.project.robot.maps;

public class JsonAutoRobot {
    public JsonPosition position; // The position of the robot in the room
    public int speed; // The speed at which the robot moves
    public int start_angle; // The initial angle of the robot
    public int rotation_angle; // The angle of each rotation step
    public int view_distance; // The distance the robot can see
    public String rotation_direction; // The direction of rotation for the robot

    public JsonAutoRobot() {
    }
}
