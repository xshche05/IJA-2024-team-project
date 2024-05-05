package ija.project.robot.maps;

/**
 * Represents a position in the simulation environment,
 * that can be used as robots' and obstacles' property in a JSON file.
 * This class provides the necessary properties to define the coordinates of a position.
 */
public class JsonPosition {
    public int x; // The x coordinate of the position
    public int y; // The y coordinate of the position

    /**
     * Constructor to initialize the position with coordinates.
     */
    public JsonPosition() {
    }
}
