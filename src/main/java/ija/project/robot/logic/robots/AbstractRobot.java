/*
 * Author: Kirill Shchetiniuk (xshche05), Artur Sultanov (xsulta01)
 * Description: This file provides the logic for the abstract robot class in the application.
 * It defines the basic functionalities for moving and rotating robots in a simulated environment.
 */
package ija.project.robot.logic.robots;

import ija.project.robot.logic.common.AbstractRoomObject;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.room.Room;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import static ija.project.robot.gui.controllers.Playground.playSemaphore;

/**
 * Represents an abstract robot with basic functionalities for moving and rotating in a simulated environment.
 * This class provides a framework for defining robots' movement strategies, animation transitions, and handling their playback.
 */
public abstract class AbstractRobot extends AbstractRoomObject {
    /** The current angle of the robot in degrees. */
    protected int currentAngle;
    /** The speed at which the robot moves. */
    protected int speed = 1;
    /** The angle of each rotation step. */
    protected int stepAngle = 45;
    /** The stack of transitions for playback animations. */
    protected Stack<Transition> play_back_transition = new Stack<>();
    /** The position to return to after playback. */
    protected Position playBackPosition;
    /** The angle to return to after playback. */
    protected int playBackAngle;
    /** The semaphore for controlling access to shared resources. */
    protected final Semaphore resourceSemaphore = new Semaphore(1);
    private boolean backPlaying = false;
    private final int backPlaySped = 4;

    /**
     * Constructs a new AbstractRobot at a given position with a default angle of zero degrees.
     * @param pos The initial position of the robot.
     */
    public AbstractRobot(Position pos) {
        super(pos);
        this.currentAngle = 0;
        playBackPosition = pos;
        playBackAngle = currentAngle;
    }

    /**
     * Sets the movement speed of the robot.
     * @param speed The speed at which the robot moves.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Sets the angle for each step of rotation.
     * @param rotate_angle The degree of each rotation step.
     */
    public void setStepAngle(int rotate_angle) {
        this.stepAngle = rotate_angle;
    }

    /**
     * Returns the current rotation angle of the robot.
     * @return The current angle in degrees.
     */
    public int getCurrentAngle() {
        return this.currentAngle;
    }

    /**
     * Checks if the robot can move diagonally based on its current angle.
     * @param newPos The intended new position to move to.
     * @return true if the move is allowed, false otherwise.
     */
    protected boolean _checkDiagonals(Position newPos) {
        if (this.currentAngle % 90 == 0) {
            return true;
        }
        Position p1 = new Position(this.pos.x(), newPos.y());
        Position p2 = new Position(newPos.x(), this.pos.y());
        return Room.getInstance().isPositionFree(p1) && Room.getInstance().isPositionFree(p2);
    }

    /**
     * Rotates the robot by a specified angle.
     * @param angle The angle to rotate by, in degrees.
     */
    protected void rotate(int angle) {
        this.currentAngle = (this.currentAngle + angle) % 360;
        if (this.currentAngle < 0) {
            this.currentAngle += 360;
        }
    }

    /**
     * Determines if the robot can move to a new position.
     * @return The new potential position if the robot can move, null otherwise.
     */
    public abstract Position canMove();

    /**
     * Moves the robot to a new position if possible.
     * @return true if the move was successful, false otherwise.
     */
    public abstract boolean move();

    /**
     * Adds a transition to the stack of playback animations for the robot.
     * @param transition The transition to add.
     */
    public void addToBackTransition(Transition transition) {
        resourceSemaphore.acquireUninterruptibly();
        int duration = (int) transition.getCycleDuration().toMillis() / backPlaySped;
        if (transition instanceof RotateTransition rt) {
            RotateTransition back_rt = new RotateTransition();
            back_rt.setByAngle(-rt.getByAngle());
            back_rt.setCycleCount(1);
            back_rt.setAutoReverse(true);
            back_rt.setInterpolator(Interpolator.LINEAR);
            back_rt.setDuration(Duration.millis(duration));
            back_rt.setNode(getSelfImageView());
            play_back_transition.push(back_rt);
        } else if (transition instanceof TranslateTransition tt) {
            TranslateTransition back_tt = new TranslateTransition();
            back_tt.setByX(-tt.getByX());
            back_tt.setByY(-tt.getByY());
            back_tt.setCycleCount(1);
            back_tt.setAutoReverse(true);
            back_tt.setInterpolator(Interpolator.LINEAR);
            back_tt.setDuration(Duration.millis(duration));
            back_tt.setNode(getSelfImageView());
            play_back_transition.push(back_tt);
        } else {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        resourceSemaphore.release();
    }

    /**
     * Initiates playback of the robot's stored transitions in reverse order.
     */
    public void playBackTransition() {
        if (backPlaying) {
            return;
        }
        backPlaying = true;
        new Thread(() -> {
            Semaphore semaphore = new Semaphore(1);
            while (!play_back_transition.isEmpty()) {
                semaphore.acquireUninterruptibly(); // wait for the previous transition to finish
                Transition transition = play_back_transition.pop(); // get the next transition
                transition.setAutoReverse(true);
                playSemaphore.acquireUninterruptibly();
                transition.play(); // play the transition in reverse
                playSemaphore.release();
                transition.setOnFinished(event -> semaphore.release()); // release the semaphore after the transition finishes
            }
            backPlaying = false;
            pos = playBackPosition; // reset the position
            setStartAngle(playBackAngle); // reset the angle
        }).start();
    }

    /**
     * Sets the starting angle for the robot and updates the image view accordingly.
     * @param startAngle The starting angle in degrees.
     */
    public void setStartAngle(int startAngle) {
        this.currentAngle = startAngle;
        this.playBackAngle = startAngle;
        imageView.setRotate(startAngle);
    }

    /**
     * Returns the speed of the robot.
     * @return The current speed.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Returns the step angle of the robot.
     * @return The current step angle.
     */
    public int getStepAngle() {
        return stepAngle;
    }

    /**
     * Method for implementing robot-specific actions per tick of the simulation.
     */
    abstract public void tick();

    /**
     * Method for getting the image view of the robot.
     * @return The image view of the robot.
     */
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
