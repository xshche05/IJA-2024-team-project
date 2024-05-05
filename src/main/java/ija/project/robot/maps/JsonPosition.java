/*
 * Author: Kirill Shchetiniuk (xshche05), Artur Sultanov (xsulta01)
 * Description: This file the position representation that can be used as robots' and obstacles' property
 * within JSON configuration format.
 */
package ija.project.robot.maps;

/**
 * Represents a position in the simulation environment,
 * that can be used as robots' and obstacles' property in a JSON file.
 * This class provides the necessary properties to define the coordinates of a position.
 */
public class JsonPosition {
    /** The x coordinate of the position. */
    public int x;
    /** The y coordinate of the position. */
    public int y;

    /**
     * Constructor to initialize the position with coordinates.
     */
    public JsonPosition() {
    }
}
