/*
 * Author: Kirill Shchetiniuk (xshche05), Artur Sultanov (xsulta01)
 * Description: This file contains the representation of automatic robot properties and its configuration
 * within JSON configuration format.
 */
package ija.project.robot.maps;

/**
 * Represents an automatic robot configuration within the simulation environment that can be saved to a JSON file.
 * This class provides the necessary properties to define the behavior of a robot that moves autonomously.
 */
public class JsonAutoRobot {
    /** The position of the robot within the room. */
    public JsonPosition position;
    /** The speed at which the robot moves. */
    public int speed;
    /** The initial angle of the robot. */
    public int start_angle;
    /** The angle of each rotation step. */
    public int rotation_angle;
    /** The distance the robot can see. */
    public int view_distance;
    /** The direction of rotation for the robot. */
    public String rotation_direction;

    /**
     * Constructor to initialize the robot with a position.
     */
    public JsonAutoRobot() {
    }
}
