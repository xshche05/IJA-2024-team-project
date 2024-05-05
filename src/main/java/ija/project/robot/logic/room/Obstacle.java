/*
 * Author: Kirill Shchetiniuk (xshche05)
 * Description: This file contains the logic for the obstacle in the simulation.
 */
package ija.project.robot.logic.room;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.common.AbstractRoomObject;
import ija.project.robot.logic.common.Position;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

import static ija.project.robot.RobotApp.logger;

/**
 * Represents an obstacle within the robot simulation environment.
 * Obstacles are immovable objects that can block the path of robots.
 */
public class Obstacle extends AbstractRoomObject {

    /**
     * Constructs an Obstacle at a specified position and initializes its graphical representation.
     *
     * @param pos The position in the grid where the obstacle is placed.
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
