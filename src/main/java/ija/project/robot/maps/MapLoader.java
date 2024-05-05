package ija.project.robot.maps;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ija.project.robot.logic.room.Room;

import java.io.*;
import java.util.Objects;

import static ija.project.robot.RobotApp.logger;

public class MapLoader {

    private static MapLoader instance = null;

    private MapLoader() {
    }

    public static MapLoader getInstance() {
        if (instance == null) {
            instance = new MapLoader();
        }
        return instance;
    }

    public boolean loadMap(File jsonFile) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(jsonFile);
        } catch (FileNotFoundException e) {
            logger.warning("File not found: " + jsonFile.getAbsolutePath());
            return false;
        }
        JsonRoom room;
        try {
            String json = new String(Objects.requireNonNull(inputStream).readAllBytes());
            Gson gson = new Gson();
            room = gson.fromJson(json, JsonRoom.class);
            Room.getInstance().clear();
            Room.getInstance().fromJsonRoom(room);
            return true;
        } catch (IOException e) {
            logger.warning("Error reading file: " + jsonFile.getAbsolutePath());
        } catch (JsonSyntaxException e) {
            logger.warning("Error parsing JSON: " + jsonFile.getAbsolutePath());
        }
        return false;
    }

    public void loadPredefinedMap(InputStream jsonFile) {
        JsonRoom room;
        try {
            String json = new String(Objects.requireNonNull(jsonFile).readAllBytes());
            Gson gson = new Gson();
            room = gson.fromJson(json, JsonRoom.class);
            Room.getInstance().clear();
            Room.getInstance().fromJsonRoom(room);
        } catch (IOException e) {
            logger.warning("Error reading file: " + jsonFile.toString());
        } catch (JsonSyntaxException e) {
            logger.warning("Error parsing JSON: " + jsonFile.toString());
        }
    }

    public void loadPredefinedMap1() { // todo link to menu
        // get resource from predefined map 1;
        InputStream inputStream = getClass().getResourceAsStream("map_1.json");
        loadPredefinedMap(inputStream);
    }

    public void loadPredefinedMap2() { // todo link to menu
        InputStream inputStream = getClass().getResourceAsStream("map_2.json");
        loadPredefinedMap(inputStream);
    }

    public void loadPredefinedMap3() { // todo link to menu
        InputStream inputStream = getClass().getResourceAsStream("map_3.json");
        loadPredefinedMap(inputStream);
    }
}
