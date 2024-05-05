/*
 * Author: Kirill Shchetiniuk (xshche05), Artur Sultanov (xsulta01)
 * Description: This file provides the logic for the start scene of the application.
 * It handles the initial interactions of the user with the application's main menu, including loading files,
 * saving files, accessing help, and creating new projects.
 */

package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.MenuInterface;
import ija.project.robot.gui.interfaces.SceneInterface;
import ija.project.robot.gui.logic.Menu;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

import static ija.project.robot.RobotApp.logger;

/**
 * Controller for the start scene of the application.
 * Implements the {@link MenuInterface} and {@link SceneInterface} interfaces.
 * This class handles the initial interactions of the user with the application's main menu,
 * including loading files, saving files, accessing help, and creating new projects.
 */
public class Start implements MenuInterface, SceneInterface {
    /** Anchor pane for the start scene. */
    @FXML
    public AnchorPane AnchorPane;
    /** Load file menu item. */
    @FXML
    public MenuItem MenuFileLoad;
    /** Save as menu item. */
    @FXML
    public MenuItem MenuFileSaveAs;
    /** Create new button. */
    @FXML
    public Button CreateNew;
    /** Create new menu item. */
    @FXML
    public MenuItem MenuNewFile;

    /**
     * Loading the map configuration from the file.
     */
    @Override
    @FXML
    public void FileLoad() {
        new Menu().initialize().FileLoad(AnchorPane);
    }

    /**
     * Saving the map configuration to the file.
     */
    @Override
    @FXML
    public void FileSaveAs() {
        new Menu().initialize().FileSaveAs(AnchorPane);
    }

    /**
     * Displaying the About dialog.
     */
    @Override
    @FXML
    public void About() {
        new Menu().initialize().About(AnchorPane);
    }

    /**
     * Creating a new map configuration.
     */
    @Override
    @FXML
    public void CreateNew() {
        new Menu().initialize().CreateNew(AnchorPane);
    }

    /**
     * Loading the predefined map configuration 1.
     */
    @Override
    @FXML
    public void LoadPredefinedMap1() {
        new Menu().initialize().LoadPredefinedMap1(AnchorPane);
    }

    /**
     * Loading the predefined map configuration 2.
     */
    @Override
    @FXML
    public void LoadPredefinedMap2() {
        new Menu().initialize().LoadPredefinedMap2(AnchorPane);
    }

    /**
     * Loading the predefined map configuration 3.
     */
    @Override
    @FXML
    public void LoadPredefinedMap3() {
        new Menu().initialize().LoadPredefinedMap3(AnchorPane);
    }

    /**
     * Provides the scene for the start screen of the application.
     *
     * @return The loaded Scene object for the start screen.
     */
    public static Scene getScene() {
        logger.info("Getting start scene");
        return SceneInterface.getScene(Start.class,"start.fxml");
    }
}
