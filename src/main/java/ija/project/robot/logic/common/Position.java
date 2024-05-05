package ija.project.robot.logic.common;

/**
 * Immutable record that represents a position in a 2D space using x and y coordinates.
 * This record is primarily used to define locations in the simulation environment of the robot.
 */
public record Position(int x, int y) {

    /**
     * Provides a string representation of the position in the format:
     * Position {x,y}
     *
     * @return A string that represents the position.
     */
    @Override
    public String toString() {
        return "Position {"+ x + "," + y + "}";
    }
}
