package ija.project.robot.gui.logic;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.gui.controllers.Start;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.logic.room.Room;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class Menu {
    private final FileChooser fileChooser = new FileChooser();

    // Initialize file chooser
    public Menu initialize() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
        return this;
    }

    // Load room configuration from file
    public void FileLoad(AnchorPane AnchorPane) {
        System.Logger logger = System.getLogger(Start.class.getName());
        logger.log(System.Logger.Level.INFO, "File Opened");
        File file = fileChooser.showOpenDialog(AnchorPane.getScene().getWindow());
        if (file != null) {
            logger.log(System.Logger.Level.INFO, "File selected: " + file.getAbsolutePath());
        } else {
            logger.log(System.Logger.Level.INFO, "No file selected");
            return;
        }
        Room.getInstance().clear();
        Room.getInstance().loadRoomConfiguration(file);
        Scene playGround = Playground.getScene();
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        stage.setScene(playGround);
    }

    // Save room configuration to file
    public void FileSaveAs(AnchorPane AnchorPane) {
        System.Logger logger = System.getLogger(Start.class.getName());
        logger.log(System.Logger.Level.INFO, "File save as window opened");
        File file = fileChooser.showSaveDialog(AnchorPane.getScene().getWindow());
        if (file != null) {
            logger.log(System.Logger.Level.INFO, "File path: " + file.getAbsolutePath());
        } else {
            logger.log(System.Logger.Level.INFO, "No file selected");
            return;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(file));
            writer.write(Room.getInstance().getRoomConfiguration());
            writer.close();
            logger.log(System.Logger.Level.INFO, "ROOM CONFIGURATION File saved");
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error saving file: " + e.getMessage());
        }
    }
}
