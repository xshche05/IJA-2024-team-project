/*
 * Author: Kirill Shchetiniuk (xshche05), Artur Sultanov (xsulta01)
 * Description: This file contains the interface for the top menu functionalities in the application.
 */
package ija.project.robot.gui.interfaces;

/**
 * Defines the basic file handling and help functionalities for a user interface menu.
 */
public interface MenuInterface {
    /**
     * Loads a room state from JSON file contains room map into the application.
     * This method is triggered when the user selects the 'Load File' option from the menu.
     */
    void FileLoad();

    /**
     * Saves the current room state to a JSON file.
     * This method is triggered when the user selects the 'Save As' option from the menu.
     */
    void FileSaveAs();

    /**
     * Displays help information about the application.
     * This method is triggered when the user selects the 'About' option from the 'Help' menu.
     */
    void About();

    /**
     * Initiates the manual creation of a new room map by user.
     * This method is triggered when the user clicks the 'Create New' button.
     */
    void CreateNew();

    /**
     * Loads a 'Map 1' predefined configuration of the room.
     */
    void LoadPredefinedMap1();

    /**
     * Loads a 'Map 2' predefined configuration of the room.
     */
    void LoadPredefinedMap2();

    /**
     * Loads a 'Map 3' predefined configuration of the room.
     */
    void LoadPredefinedMap3();
}
