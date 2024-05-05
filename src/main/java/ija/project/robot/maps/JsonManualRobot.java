/*
 * Author: Kirill Shchetiniuk (xshche05), Artur Sultanov (xsulta01)
 * Description: This file contains the representation of manual robot properties and configuration
 * within JSON configuration format.
 */
package ija.project.robot.maps;

/**
 * Represents a manual robot configuration within the simulation environment that can be saved to a JSON file.
 * This class provides the necessary properties to define the behavior of a robot that is controlled manually.
 */
public class JsonManualRobot {
    public JsonPosition position; // The position of the robot in the room
    public int speed; // The speed at which the robot moves
    public int start_angle; // The initial angle of the robot
    public int rotation_angle; // The angle of each rotation step

    /**
     * Constructor to initialize the robot with a position.
     */
    public JsonManualRobot() {
    }
}

