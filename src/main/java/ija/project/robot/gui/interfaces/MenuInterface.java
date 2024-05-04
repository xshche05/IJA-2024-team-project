package ija.project.robot.gui.interfaces;

/**
 * Defines the basic file handling and help functionalities for a user interface menu.
 */
public interface MenuInterface {
    /**
     * Initiates the process to load a .txt file with the room map.
     * Map contains the room dimensions and the initial positions of the robots and obstacles.
     */
    void FileLoad();

    /**
     * Initiates the process to save data or a file.
     * Save the map of the room to a .txt file.
     */
    void FileSaveAs();

    /**
     * Initiates the process to display help information related to the application.
     */
    void About();
}
