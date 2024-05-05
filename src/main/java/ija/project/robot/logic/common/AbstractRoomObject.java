package ija.project.robot.logic.common;

import javafx.scene.image.ImageView;

/**
 * Abstract class representing a room object in the robot simulation.
 * This class provides a common structure for room objects, including robots and obstacles,
 * by defining basic properties and behaviors shared among them.
 */
public abstract class AbstractRoomObject {
    protected Position pos;
    protected int id;
    protected ImageView imageView;
    private static int id_counter = 0;

    /**
     * Constructor to initialize the room object with a position.
     * Automatically assigns a unique ID to the object.
     *
     * @param pos The position of the object within the room.
     */
    public AbstractRoomObject(Position pos) {
        this.pos = pos;
        this.id = id_counter++;
    }

    /**
     * Gets the unique ID of the room object.
     *
     * @return The unique identifier for this object.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the position of the room object.
     *
     * @return The position of the object within the room grid.
     */
    public Position getPosition() {
        return this.pos;
    }

    /**
     * Removes the ImageView associated with this room object.
     * This method is typically called when the object is removed from the room,
     * ensuring that the associated ImageView is properly cleaned up.
     *
     * @return The ImageView that was removed, allowing for additional operations such as removal from a GUI component.
     */
    public ImageView removeImageView() {
        ImageView iv = this.imageView;
        if (this.imageView != null) {
            this.imageView.setImage(null);
        }
        this.imageView = null;
        return iv;
    }

    /**
     * Abstract method to be implemented by subclasses to return their specific ImageView.
     * This method should handle the creation and configuration of the ImageView as needed by the specific room object type.
     *
     * @return The ImageView representing the object in the UI.
     */
    abstract public ImageView getImageView();

    /**
     * Gets the ImageView currently set for this room object.
     * This can be used to retrieve the ImageView without altering its state,
     * as opposed to {@link #getImageView()} which may instantiate or configure the ImageView.
     *
     * @return The current ImageView, or null if no ImageView has been set.
     */
    public ImageView getSelfImageView() {
        return this.imageView;
    }
}
