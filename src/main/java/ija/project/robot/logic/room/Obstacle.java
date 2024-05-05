package ija.project.robot.logic.room;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.common.AbstractRoomObject;
import ija.project.robot.logic.common.Position;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

import static ija.project.robot.RobotApp.logger;

/**
 * Represents an obstacle within the room that robots must avoid.
 * Obstacles are static and cannot be moved or interacted with by robots.
 */
public class Obstacle extends AbstractRoomObject {

    /**
     * Initializes a new obstacle at the specified position.
     */
    public Obstacle(Position pos) {
        super(pos);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("wall.png")));
        imageView = new ImageView(image);
        imageView.setFitHeight(Playground.gridWidth);
        imageView.setFitWidth(Playground.gridWidth);
        logger.info("Obstacle (" + this.id + ") created at " + pos);
    }

    @Override
    public ImageView getImageView() {
        return imageView;
    }
}
