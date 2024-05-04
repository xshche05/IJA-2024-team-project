package ija.project.robot.logic.room;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.common.AbstractRoomObject;
import ija.project.robot.logic.common.Position;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

import static ija.project.robot.RobotApp.logger;

public class Obstacle extends AbstractRoomObject {

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
