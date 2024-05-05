package ija.project.robot.maps;

/**
 * Represents an automatic robot configuration within the simulation environment that can be saved to a JSON file.
 * This class provides the necessary properties to define the behavior of a robot that moves autonomously.
 */
public class JsonAutoRobot {
    public JsonPosition position; // The position of the robot in the room
    public int speed; // The speed at which the robot moves
    public int start_angle; // The initial angle of the robot
    public int rotation_angle; // The angle of each rotation step
    public int view_distance; // The distance the robot can see
    public String rotation_direction; // The direction of rotation for the robot

    /**
     * Constructor to initialize the robot with a position.
     */
    public JsonAutoRobot() {
    }
}
