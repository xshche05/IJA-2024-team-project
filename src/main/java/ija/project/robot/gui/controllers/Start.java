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
 * This class handles the initial interactions of the user with the application's main menu,
 * including loading files, saving files, accessing help, and creating new projects.
 */
public class Start implements MenuInterface, SceneInterface {
    @FXML
    public AnchorPane AnchorPane; // fx:id="AnchorPane"
    @FXML
    public MenuItem MenuFileLoad; // fx:id="MenuFileLoad"
    @FXML
    public MenuItem MenuFileSaveAs; // fx:id="MenuFileSaveAs"
    @FXML
    public Button CreateNew;
    @FXML
    public MenuItem MenuNewFile;

    /**
     * Loads a room state from file.txt contains room map into the application.
     * This method is triggered when the user selects the 'Load File' option from the menu.
     */
    @Override
    @FXML
    public void FileLoad() {
        new Menu().initialize().FileLoad(AnchorPane);
    }

    /**
     * Saves the current room state to a file.
     * This method is triggered when the user selects the 'Save As' option from the menu.
     */
    @Override
    @FXML
    public void FileSaveAs() {
        new Menu().initialize().FileSaveAs(AnchorPane);
    }

    /**
     * Displays help information related to the application.
     * This method is triggered when the user selects the 'Help' option from the menu.
     */
    @Override
    @FXML
    public void About() {
        new Menu().initialize().About(AnchorPane);
    }

    /**
     * Initiates the manual creation of a new room map by user.
     * This method is triggered when the user clicks the 'Create New' button.
     */
    @FXML
    public void CreateNew() {
        new Menu().initialize().CreateNew(AnchorPane);
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
