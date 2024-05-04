package ija.project.robot.maps;

import com.google.gson.Gson;
import ija.project.robot.logic.room.Room;

import java.io.*;
import java.util.Objects;

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

    public void loadMap(File jsonFile) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(jsonFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JsonRoom room = null;
        try {
            String json = new String(Objects.requireNonNull(inputStream).readAllBytes());
            Gson gson = new Gson();
            room = gson.fromJson(json, JsonRoom.class);
            Room.getInstance().clear();
            Room.getInstance().fromJsonRoom(room);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadPredefinedMap1() { // todo link to menu
        // get resource from predefined map 1
        File mapJson = new File(Objects.requireNonNull(getClass().getResource("map_1.json")).getFile());
        loadMap(mapJson);
    }

    public void loadPredefinedMap2() { // todo link to menu
        File mapJson = new File(Objects.requireNonNull(getClass().getResource("map_2.json")).getFile());
        loadMap(mapJson);
    }

    public void loadPredefinedMap3() { // todo link to menu
        File mapJson = new File(Objects.requireNonNull(getClass().getResource("map_3.json")).getFile());
        loadMap(mapJson);
    }
}
