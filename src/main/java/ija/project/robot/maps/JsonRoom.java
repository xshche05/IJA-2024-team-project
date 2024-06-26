/*
 * Author: Kirill Shchetiniuk (xshche05), Artur Sultanov (xsulta01)
 * Description: This file contains the representation of room properties and its configuration
 * within JSON configuration format.
 */
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

    /** The number of rows in the room. */
    public int rows;
    /** The number of columns in the room. */
    public int cols;
    /** List of obstacles in the room. */
    public List<JsonManualRobot> manual_robots;
    /** List of automatic robots in the room. */
    public List<JsonAutoRobot> auto_robots;
    /** List of obstacles in the room. */
    public List<JsonObstacle> obstacles;

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
