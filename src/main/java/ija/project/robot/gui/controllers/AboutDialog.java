/*
 * Author: Artur Sultanov (xsulta01)
 * Description: About dialog controller file, that provides the logic for the About dialog.
 */

package ija.project.robot.gui.controllers;

import ija.project.robot.gui.interfaces.Dialog;
import ija.project.robot.gui.interfaces.SceneInterface;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;

/**
 * Represents the About dialog for the application.
 * This dialog provides information about the application such as version,
 * author details. It extends the {@link Dialog} class,
 * using its default cancel functionality to close the dialog.
 * <p>
 * This class handles the UI logic for the About dialog, including closing the dialog
 * when the Close button is pressed which in this context performs the same action as a cancel,
 * hence the re-use of the {@link #Cancel()} method.
 */
public class AboutDialog extends Dialog {
    /** The label displaying the about text. */
    @FXML public Label aboutLabel;

    /**
     * Initializes the About dialog.
     * This method is called automatically when the FXML file is loaded.
     * It sets the dialog title and the about text.
     */
    public void initialize() {
        // Set the dialog title
        String about = """
                Project within the university subject "IJA".


                Built on May 5, 2024
                For educational use only.


                Authors:
                Kirill Shchetiniuk (xshche05)
                Artur Sultanov (xsulta01)""";
        aboutLabel.setText(about);
    }

    /**
     * This method overloads the abstract {@code Ok} method from the {@link Dialog} class.
     */
    @Override
    public void Ok() {
        Cancel(); // Close the dialog
    }

    /**
     * Retrieves and returns the scene for the About dialog using the {@link SceneInterface}.
     * This method facilitates the loading of the appropriate FXML file for the About dialog and
     * is typically called to initialize the dialog in the UI.
     *
     * @return The loaded {@link Scene} for the About dialog.
     */
    public static Scene getScene() {
        return SceneInterface.getScene(AboutDialog.class, "about_dialog.fxml"); // Load the About dialog scene
    }
}
