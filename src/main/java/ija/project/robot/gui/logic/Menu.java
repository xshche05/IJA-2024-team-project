package ija.project.robot.gui.logic;

import ija.project.robot.gui.controllers.AboutDialog;
import ija.project.robot.gui.controllers.CreateDialog;
import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.gui.controllers.Start;
import ija.project.robot.logic.room.Room;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import static ija.project.robot.RobotApp.logger;


public class Menu {
    private final FileChooser fileChooser = new FileChooser();

    public Menu initialize() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
        return this;
    }

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

    public void CreateNew(AnchorPane AnchorPane) {
        logger.info("Creating new room");
        Room.getInstance().clear();
        Stage dialog = new Stage();
        dialog.initOwner(AnchorPane.getScene().getWindow());
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setTitle("New room");
        Scene dialogScene = CreateDialog.getScene();
        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.show();
    }

    public void About(AnchorPane AnchorPane) {
        logger.info("Help opened");
        Stage dialog = new Stage();
        dialog.initOwner(AnchorPane.getScene().getWindow());
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setTitle("About");
        Scene dialogScene = AboutDialog.getScene();
        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.show();
    }
}
