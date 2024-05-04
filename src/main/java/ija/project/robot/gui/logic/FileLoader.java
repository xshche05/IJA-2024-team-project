package ija.project.robot.gui.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.gui.controllers.Start;
import ija.project.robot.logic.room.Room;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;

import static ija.project.robot.RobotApp.logger;

public class FileLoader {
    private final FileChooser fileChooser = new FileChooser();

    public FileLoader initialize() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON File", "*.json"));
        fileChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
        return this;
    }


    public void FileLoad(AnchorPane anchorPane) {
        logger.info("Opening file chooser");
        File file = fileChooser.showOpenDialog(anchorPane.getScene().getWindow());
        if (file != null) {
            logger.info("File selected: " + file.getAbsolutePath());
            try {
                FileInputStream fileInputStream = new FileInputStream(file);

                Room.getInstance().clear();
                Room.getInstance().loadRoomConfiguration(fileInputStream);

                Scene playGround = Playground.getScene();
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                stage.setScene(playGround);

                logger.info("Room configuration loaded successfully from " + file.getName());
            } catch (Exception e) {
                logger.warning("Error loading configuration: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            logger.info("No file selected");
        }
    }



    public void Preset1(AnchorPane AnchorPane) {
        logger.info("Creating new room from preset 1");


        try {
            // Assuming room1.json is directly under resources folder
            InputStream inputStream = getClass().getResourceAsStream("room1.json");


            Room.getInstance().clear();
            Room.getInstance().loadRoomConfiguration(inputStream); // Adjust this method to accept RoomConfiguration
            Scene playGround = Playground.getScene();
            Stage stage = (Stage) AnchorPane.getScene().getWindow();
            stage.setScene(playGround);

        } catch (Exception e) {
            logger.warning("Error while loading preset 1: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
