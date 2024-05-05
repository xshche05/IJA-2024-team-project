package ija.project.robot.maps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a room configuration within the simulation environment that can be saved to a JSON file.
 * Contains manual and automatic robots, as well as obstacles.
 * This class provides the necessary properties to define the behavior of a room.
 */
public class JsonRoom {
    public int rows; // The number of rows in the room
    public int cols; // The number of columns in the room
    public List<JsonManualRobot> manual_robots; // The list of manual robots in the room
    public List<JsonAutoRobot> auto_robots; // The list of automatic robots in the room
    public List<JsonObstacle> obstacles;    // The list of obstacles in the room

    /**
     * Constructor to initialize the room with the number of rows and columns.
     */
    public JsonRoom() {
        manual_robots = new ArrayList<>();
        auto_robots = new ArrayList<>();
        obstacles = new ArrayList<>();
    }
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // pretty printing
        return gson.toJson(this);
    }
}
