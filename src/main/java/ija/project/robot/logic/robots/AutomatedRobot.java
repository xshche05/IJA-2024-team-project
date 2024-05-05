package ija.project.robot.logic.robots;

import ija.project.robot.gui.controllers.Playground;
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

import static ija.project.robot.RobotApp.logger;
import static ija.project.robot.gui.controllers.Playground.playSemaphore;

public class AutomatedRobot extends AbstractRobot {

    private int viewDistance = 1;
    private boolean moving = false;
    private final Semaphore transitionSemaphore = new Semaphore(1);
    private final Semaphore tickSemaphore = new Semaphore(1);

    public AutomatedRobot(Position pos) {
        super(pos);
        logger.info("AutomatedRobot (" + this.id + ") created at " + pos);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("auto_robot.png")));
        imageView = new ImageView(image);
        imageView.setFitHeight(Playground.gridWidth);
        imageView.setFitWidth(Playground.gridWidth);
    }

    @Override
    public ImageView getImageView() {
        // read image from resources
        imageView.setRotate(this.currentAngle);
        return imageView;
    }

    public Position _canMoveFrom(Position pos) {
        int y = pos.y();
        int x = pos.x();

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

        Position position = new Position(x, y);
        if (Room.getInstance().isPositionFree(position) && _checkDiagonals(position)) {
            return position;
        }
        return null;
    }

    public void setViewDistance(int newViewDistance) {
        this.viewDistance = newViewDistance;
        logger.info("AutomatedRobot (" + this.id + ") view distance set to " + newViewDistance);
    }

    @Override
    public Position canMove() {
        Position startPos = this.pos;
        Position dstPos = _canMoveFrom(startPos);
        startPos = dstPos;
        if (dstPos == null) {
            return null;
        }
        for (int i = 0; i < viewDistance - 1; i++) {
            Position newPos = _canMoveFrom(startPos);
            if (newPos != null) {
                startPos = newPos;
            } else {
                return null;
            }
        }
        return dstPos;
    }

    @Override
    public boolean move() {
        Position oldPos = this.pos;
        Position newPos = canMove();
        if (newPos == null) {
            logger.warning("AutomatedRobot (" + this.id + ") cannot move forward, there is an obstacle on the way");
            return false;
        }
        transitionSemaphore.acquireUninterruptibly();
        this.pos = newPos;
        logger.info("AutomatedRobot (" + this.id + ") moved to " + newPos);
        TranslateTransition tt = new TranslateTransition(Duration.millis((double) Playground.tickPeriod / speed), imageView);
        tt.setByX((newPos.x() - oldPos.x()) * Playground.gridWidth);
        tt.setByY((newPos.y() - oldPos.y()) * Playground.gridWidth);
        tt.setCycleCount(1);
        tt.setAutoReverse(true);
        tt.setInterpolator(Interpolator.LINEAR);
        tt.setOnFinished(event -> transitionSemaphore.release());
        playSemaphore.acquireUninterruptibly();
        addToBackTransition(tt);
        tt.play();
        playSemaphore.release();
        return true;
    }

    public void rotate() {
        transitionSemaphore.acquireUninterruptibly();
        super.rotate(stepAngle);
        logger.info("AutomatedRobot (" + this.id + ") rotated");
        RotateTransition rt = new RotateTransition(Duration.millis((double) Playground.tickPeriod / speed), imageView);
        rt.setByAngle(stepAngle);
        rt.setCycleCount(1);
        rt.setAutoReverse(true);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setOnFinished(event -> transitionSemaphore.release());
        playSemaphore.acquireUninterruptibly();
        addToBackTransition(rt);
        rt.play();
        playSemaphore.release();
    }

    public void tick() {
        logger.info("AutomatedRobot (" + this.id + ") ticked");
        tickSemaphore.acquireUninterruptibly();
        for (int i = 0; i < speed; i++) {
            if (!moving) {
                break;
            }
            if (!move()) {
                rotate();
            }
        }
        tickSemaphore.release();
    }

    public void startMoving() {
        moving = true;
        logger.info("AutomatedRobot (" + this.id + ") started moving");
    }

    public void stopMoving() {
        moving = false;
        logger.info("AutomatedRobot (" + this.id + ") stopped moving");
    }

    public int getViewDistance() {
        return viewDistance;
    }
}
