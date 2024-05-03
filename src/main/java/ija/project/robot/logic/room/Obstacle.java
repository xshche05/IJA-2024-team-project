package ija.project.robot.logic.room;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.common.AbstractRoomObject;
import ija.project.robot.logic.common.Position;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Obstacle extends AbstractRoomObject {

    private final System.Logger logger;

    public Obstacle(Position pos) {
        super(pos);
        logger = System.getLogger("Obstacle id: " + this.id);
        logger.log(System.Logger.Level.INFO,
                "Obstacle created on " + pos);
    }

    @Override
    public ImageView getImageView() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("wall.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(Playground.gridWidth);
        imageView.setFitWidth(Playground.gridWidth);
        return imageView;
    }
}
