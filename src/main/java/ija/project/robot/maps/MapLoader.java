package ija.project.robot.maps;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ija.project.robot.logic.room.Room;

import java.io.*;
import java.util.Objects;

import static ija.project.robot.RobotApp.logger;

/**
 * A singleton class responsible for loading map configurations from JSON files into the Room.
 * This class provides methods to load maps from specific files as well as predefined maps embedded in the application.
 */
public class MapLoader {

    private static MapLoader instance = null;

    /**
     * Private constructor to prevent instantiation outside of {@link #getInstance()}.
     */
    private MapLoader() {
    }

    /**
     * Returns a singleton instance of MapLoader.
     *
     * @return The singleton instance of the MapLoader.
     */
    public static MapLoader getInstance() {
        if (instance == null) {
            instance = new MapLoader();
        }
        return instance;
    }

    /**
     * Loads a room configuration from a JSON file.
     *
     * @param jsonFile The file object representing the JSON file containing the room configuration.
     * @return True if the map was successfully loaded and false if there were any errors during file reading or JSON parsing.
     */
    public boolean loadMap(File jsonFile) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(jsonFile); // get resource from file
        } catch (FileNotFoundException e) {
            logger.warning("File not found: " + jsonFile.getAbsolutePath());
            return false;
        }
        JsonRoom room;
        try {
            String json = new String(Objects.requireNonNull(inputStream).readAllBytes());
            Gson gson = new Gson();
            room = gson.fromJson(json, JsonRoom.class); // parse JSON to object
            Room.getInstance().clear(); // clear the room
            Room.getInstance().fromJsonRoom(room); // load the room from JSON
            return true;
        } catch (IOException e) {
            logger.warning("Error reading file: " + jsonFile.getAbsolutePath());
        } catch (JsonSyntaxException e) {
            logger.warning("Error parsing JSON: " + jsonFile.getAbsolutePath());
        }
        return false;
    }

    /**
     * Loads a predefined map configuration from a JSON file.
     *
     * @param jsonFile The input stream representing the JSON file containing the room configuration.
     */
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

    /**
     * Loads the first predefined map configuration from a JSON file.
     */
    public void loadPredefinedMap1() {
        // get resource from predefined map 1;
        InputStream inputStream = getClass().getResourceAsStream("map_1.json");
        loadPredefinedMap(inputStream);
    }

    /**
     * Loads the second predefined map configuration from a JSON file.
     */
    public void loadPredefinedMap2() {
        InputStream inputStream = getClass().getResourceAsStream("map_2.json");
        loadPredefinedMap(inputStream);
    }

    /**
     * Loads the third predefined map configuration from a JSON file.
     */
    public void loadPredefinedMap3() {
        InputStream inputStream = getClass().getResourceAsStream("map_3.json");
        loadPredefinedMap(inputStream);
    }
}
