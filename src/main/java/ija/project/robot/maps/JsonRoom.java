package ija.project.robot.maps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class JsonRoom {
    public int rows;
    public int cols;
    public List<JsonManualRobot> manual_robots;
    public List<JsonAutoRobot> auto_robots;
    public List<JsonObstacle> obstacles;
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
