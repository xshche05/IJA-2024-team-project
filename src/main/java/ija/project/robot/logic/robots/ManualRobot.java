package ija.project.robot.logic.robots;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.gui.logic.ControlledRobot;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.room.Room;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;
import java.util.concurrent.Semaphore;

public class ManualRobot extends AbstractRobot {

    private final ImageView imageView;

    private final Semaphore rotateSemaphore = new Semaphore(1);

    public ManualRobot(Position pos) {
        super(pos);
        logger.info("ManualRobot ("+this.id+") created at " + pos);
        Image image = new Image(Objects.requireNonNull(ManualRobot.class.getResourceAsStream("robot.png")));
        imageView = new ImageView(image);
        imageView.setFitHeight(Playground.gridWidth);
        imageView.setFitWidth(Playground.gridWidth);
    }

    @Override
    public ImageView getImageView() {
        if (ControlledRobot.getInstance().getRobot() == this) {
            return ControlledRobot.getInstance().getImageView();
        }
        imageView.setRotate(this.currentAngle);
        return imageView;
    }

    @Override
    public Position canMove() {
        int y = this.pos.y();
        int x = this.pos.x();

        switch (currentAngle) {
            case 0: y--; break;          // Up
            case 45: x++; y--; break;
            case 90: x++; break;          // Right
            case 135: x++; y++; break;
            case 180: y++; break;         // Down
            case 225: x--; y++; break;
            case 270: x--; break;         // Left
            case 315: x--; y--; break;
            default: return null;         // TODO: throw exception
        }

        Position pos = new Position(x, y); // todo
        if (Room.getInstance().isPositionFree(pos) && _checkDiagonals(pos)) {
            return pos;
        }
        return null;
    }

    public void rotateLeft() {
        rotateSemaphore.acquireUninterruptibly();
        rotate(-this.stepAngle);
        RotateTransition rt = new RotateTransition(Duration.millis((double) Playground.tickPeriod / speed), getImageView());
        rt.setByAngle(-this.stepAngle);
        rt.setCycleCount(1);
        rt.setAutoReverse(true);
        rt.play();
        rt.setOnFinished(event -> {
            rotateSemaphore.release();
            logger.info("ManualRobot ("+this.id+") rotated left");
        });
    }

    public void rotateRight() {
        rotateSemaphore.acquireUninterruptibly();
        rotate(this.stepAngle);
        RotateTransition rt = new RotateTransition(Duration.millis((double) Playground.tickPeriod / speed), getImageView());
        rt.setByAngle(this.stepAngle);
        rt.setCycleCount(1);
        rt.setAutoReverse(true);
        rt.play();
        rt.setOnFinished(event ->
        {
            rotateSemaphore.release();
            logger.info("ManualRobot ("+this.id+") rotated right");
        });
    }

    @Override
    public boolean move() {
        for (int i = 0; i < speed; i++) {
            rotateSemaphore.acquireUninterruptibly();
            Position prevPos = this.pos;
            Position newPos = canMove();
            if (newPos == null) {
                logger.warning("ManualRobot ("+this.id+") cannot move forward, there is an obstacle on the way");
                rotateSemaphore.release();
                return false;
            }
            this.pos = newPos;
            // Make translate transition
            TranslateTransition tt = new TranslateTransition(Duration.millis((double) Playground.tickPeriod / speed), getImageView());
            tt.setByX((newPos.x() - prevPos.x()) * Playground.gridWidth);
            tt.setByY((newPos.y() - prevPos.y()) * Playground.gridWidth);
            tt.setCycleCount(1);
            tt.setAutoReverse(true);
            tt.setInterpolator(Interpolator.LINEAR);
            tt.play();
            tt.setOnFinished(event -> {
                rotateSemaphore.release();
                logger.info("ManualRobot ("+this.id+") moved to " + this.pos);
            });
        }
        return true;
    }
}
