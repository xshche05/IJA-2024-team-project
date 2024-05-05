package ija.project.robot.logic.robots;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.common.AbstractRoomObject;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.room.Room;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import static ija.project.robot.gui.controllers.Playground.playSemaphore;

public abstract class AbstractRobot extends AbstractRoomObject {
    protected int currentAngle;
    protected int speed = 1;
    protected int stepAngle = 45;
    protected Stack<Transition> play_back_transition = new Stack<>();
    protected Position playBackPosition;
    protected int playBackAngle;
    protected final Semaphore resourceSemaphore = new Semaphore(1);
    public boolean backPlaying = false;
    public AbstractRobot(Position pos) {
        super(pos);
        this.currentAngle = 0;
        playBackPosition = pos;
        playBackAngle = currentAngle;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void setStepAngle(int rotate_angle) {
        this.stepAngle = rotate_angle;
    }
    public int getCurrentAngle() {
        return this.currentAngle;
    }
    protected boolean _checkDiagonals(Position newPos) {
        if (this.currentAngle % 90 == 0) {
            return true;
        }
        Position p1 = new Position(this.pos.x(), newPos.y());
        Position p2 = new Position(newPos.x(), this.pos.y());
        return Room.getInstance().isPositionFree(p1) && Room.getInstance().isPositionFree(p2);
    }
    protected void rotate(int angle) {
        this.currentAngle = (this.currentAngle + angle) % 360;
        if (this.currentAngle < 0) {
            this.currentAngle += 360;
        }
    }
    public abstract Position canMove();
    public abstract boolean move();
    public void addToBackTransition(Transition transition) {
        resourceSemaphore.acquireUninterruptibly();
        int duration = (int) transition.getCycleDuration().toMillis() / 4;
        if (transition instanceof RotateTransition rt) {
            RotateTransition back_rt = new RotateTransition();
            back_rt.setByAngle(-rt.getByAngle());
            back_rt.setCycleCount(1);
            back_rt.setAutoReverse(true);
            back_rt.setInterpolator(Interpolator.LINEAR);
            back_rt.setDuration(Duration.millis(duration));
            back_rt.setNode(getSelfImageView()); // todo
            play_back_transition.push(back_rt);
        } else if (transition instanceof TranslateTransition tt) {
            TranslateTransition back_tt = new TranslateTransition();
            back_tt.setByX(-tt.getByX());
            back_tt.setByY(-tt.getByY());
            back_tt.setCycleCount(1);
            back_tt.setAutoReverse(true);
            back_tt.setInterpolator(Interpolator.LINEAR);
            back_tt.setDuration(Duration.millis(duration));
            back_tt.setNode(getSelfImageView()); // todo
            play_back_transition.push(back_tt);
        } else {
            Transition back_transition = new Transition() {
                {
                    setCycleDuration(Duration.millis(duration));
                }
                @Override
                protected void interpolate(double v) {
                }
            };
            play_back_transition.push(back_transition);
        }
        resourceSemaphore.release();
    }
    public void playBackTransition() {
        if (backPlaying) {
            return;
        }
        backPlaying = true;
        new Thread(() -> {
            Semaphore semaphore = new Semaphore(1);
            while (!play_back_transition.isEmpty()) {
                semaphore.acquireUninterruptibly();
                Transition transition = play_back_transition.pop();
                playSemaphore.acquireUninterruptibly();
                transition.setAutoReverse(true);
                transition.play();
                transition.setOnFinished(event -> semaphore.release());
                playSemaphore.release();
            }
            backPlaying = false;
            pos = playBackPosition;
            setStartAngle(playBackAngle);
        }).start();
    }

    public void setStartAngle(int startAngle) {
        this.currentAngle = startAngle;
        this.playBackAngle = startAngle;
        imageView.setRotate(startAngle);
    }

    public int getSpeed() {
        return speed;
    }

    public int getStepAngle() {
        return stepAngle;
    }

    abstract public void tick();

    static public boolean isPlayBackFinished() {
        List<AbstractRobot> robots = Room.getInstance().getRobots();
        for (AbstractRobot robot : robots) {
            if (robot.backPlaying) {
                return false;
            }
        }
        return true;
    }
}
