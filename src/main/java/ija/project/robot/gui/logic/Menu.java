/*
 * Author: Kirill Shchetiniuk (xshche05), Artur Sultanov (xsulta01)
 * Description: This file contains the logic of the program's buttons menus.
 * It handles the menu buttons operations including file loading, saving, and other configurations' management.
 */
package ija.project.robot.gui.logic;

import ija.project.robot.gui.controllers.AboutDialog;
import ija.project.robot.gui.controllers.CreateDialog;
import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.gui.controllers.Start;
import ija.project.robot.logic.room.Room;
import ija.project.robot.maps.MapLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import static ija.project.robot.RobotApp.logger;

/**
 * Handles the main menu operations including file loading, saving, and other configurations' management.
 * This class utilizes JavaFX components to interact with the user through graphical menus and dialogs.
 */
public class Menu {
    private final FileChooser fileChooser = new FileChooser();

    /**
     * Initializes file chooser settings including setting up expected file extensions and the initial directory.
     * @return {@link Menu} instance for chaining purposes.
     */
    public Menu initialize() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
        return this;
    }

    /**
     * Opens a file chooser to load a room configuration from a {@code JSON} file.
     * After loading, it updates the room configuration and switches to the playground scene.
     *
     * @param AnchorPane the anchor pane where the file dialog will be displayed.
     */
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
        if (MapLoader.getInstance().loadMap(file)) {
            Scene playGround = Playground.getScene();
            Stage stage = (Stage) AnchorPane.getScene().getWindow();
            stage.setScene(playGround);
            logger.log(System.Logger.Level.INFO, "ROOM CONFIGURATION loaded");
        } else {
            logger.log(System.Logger.Level.ERROR, "Error loading file");
        }
    }

    /**
     * Opens a file chooser to save the current room configuration to a {@code JSON} file.
     *
     * @param AnchorPane the anchor pane where the save dialog will be displayed.
     */
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
            writer.write(Room.getInstance().toString());
            writer.close();
            logger.log(System.Logger.Level.INFO, "ROOM CONFIGURATION File saved");
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, "Error saving file: " + e.getMessage());
        }
    }

    /**
     * Creates a new room configuration, clearing any existing data.
     *
     * @param AnchorPane the anchor pane where the creation dialog will be displayed.
     */
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

    /**
     * Loads the first predefined room configuration.
     * This method sets the playground scene with the predefined configuration.
     *
     * @param AnchorPane the anchor pane where the loading operation is initiated.
     */
    public void LoadPredefinedMap1(AnchorPane AnchorPane){
        System.Logger logger = System.getLogger(Start.class.getName());
        try {
            MapLoader.getInstance().loadPredefinedMap1();
            Scene playGround = Playground.getScene();
            Stage stage = (Stage) AnchorPane.getScene().getWindow();
            stage.setScene(playGround);
            logger.log(System.Logger.Level.INFO, "ROOM CONFIGURATION loaded");
        }
        catch (Exception e){
            logger.log(System.Logger.Level.ERROR, "Error loading file");
        }

    }

    /**
     * Loads the second predefined room configuration.
     * This method sets the playground scene with the predefined configuration.
     *
     * @param AnchorPane the anchor pane where the loading operation is initiated.
     */
    public void LoadPredefinedMap2(AnchorPane AnchorPane){
        System.Logger logger = System.getLogger(Start.class.getName());
        try {
            MapLoader.getInstance().loadPredefinedMap2();
            Scene playGround = Playground.getScene();
            Stage stage = (Stage) AnchorPane.getScene().getWindow();
            stage.setScene(playGround);
            logger.log(System.Logger.Level.INFO, "ROOM CONFIGURATION loaded");
        }
        catch (Exception e){
            logger.log(System.Logger.Level.ERROR, "Error loading file");
        }

    }

    /**
     * Loads the third predefined room configuration.
     * This method sets the playground scene with the predefined configuration.
     *
     * @param AnchorPane the anchor pane where the loading operation is initiated.
     */
    public void LoadPredefinedMap3(AnchorPane AnchorPane){
        System.Logger logger = System.getLogger(Start.class.getName());
        try {
            MapLoader.getInstance().loadPredefinedMap3();
            Scene playGround = Playground.getScene();
            Stage stage = (Stage) AnchorPane.getScene().getWindow();
            stage.setScene(playGround);
            logger.log(System.Logger.Level.INFO, "ROOM CONFIGURATION loaded");
        }
        catch (Exception e){
            logger.log(System.Logger.Level.ERROR, "Error loading file");
        }

    }

    /**
     * Displays the 'about' information related to the application.
     * This method triggers the display of the 'about' dialog.
     *
     * @param AnchorPane the anchor pane where the 'about' dialog will be displayed.
     */
    public void About(AnchorPane AnchorPane) {
        logger.info("About opened");
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
