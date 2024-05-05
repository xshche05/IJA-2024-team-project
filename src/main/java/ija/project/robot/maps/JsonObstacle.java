/*
 * Author: Kirill Shchetiniuk (xshche05), Artur Sultanov (xsulta01)
 * Description: This file contains the representation of obstacle properties and its configuration
 * within JSON configuration format.
 */
package ija.project.robot.maps;

/**
 * Represents an obstacle configuration within the simulation environment that can be saved to a JSON file.
 * This class provides the necessary properties to define the behavior of an obstacle in the room.
 */
public class JsonObstacle {

    /** The position of the obstacle within the room. */
    public JsonPosition position;

    /**
     * Constructor to initialize the obstacle with a position.
     */
    public JsonObstacle() {
    }
}
