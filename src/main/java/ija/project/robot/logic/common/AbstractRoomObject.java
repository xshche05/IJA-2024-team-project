package ija.project.robot.logic.common;

import javafx.scene.image.ImageView;

import java.util.logging.Logger;

public abstract class AbstractRoomObject {
    protected Position pos;
    protected int id;
    protected ImageView imageView;
    private static int id_counter = 0;

    public AbstractRoomObject(Position pos) {
        this.pos = pos;
        this.id = id_counter++;
    }

    public int getId() {
        return this.id;
    }

    public Position getPosition() {
        return this.pos;
    }

    public ImageView removeImageView() {
        ImageView iv = this.imageView;
        if (this.imageView != null) {
            this.imageView.setImage(null);
        }
        this.imageView = null;
        return iv;
    }

    abstract public ImageView getImageView();
}
