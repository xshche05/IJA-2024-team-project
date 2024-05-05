package ija.project.robot.logic.robots;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.room.Room;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import static ija.project.robot.RobotApp.logger;
import static ija.project.robot.gui.controllers.Playground.playSemaphore;

/**
 * Represents a manually controlled robot within the simulation.
 * This robot can be controlled to move in various directions or perform no action based on commands queued in its action queue.
 */
public class ManualRobot extends AbstractRobot {
    private final Semaphore semaphore = new Semaphore(1);

    private final Queue<String> queue = new LinkedList<>();

    private final Semaphore tickSemaphore = new Semaphore(1);

    private boolean running = false;

    /**
     * Constructs a ManualRobot at a specified position with a predefined image.
     * @param pos The initial position of the robot.
     */
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
        return imageView;
    }

    /**
     * Sets the robot as the controlled robot in the simulation, changing its image to indicate it's under control.
     */
    public void setControlled() {
        Image image = new Image(Objects.requireNonNull(ManualRobot.class.getResourceAsStream("selected_robot.png")));
        imageView.setImage(image);
    }

    /**
     * Unsets the robot as the controlled robot in the simulation, changing its image back to the default robot image.
     */
    public void unsetControlled() {
        Image image = new Image(Objects.requireNonNull(ManualRobot.class.getResourceAsStream("robot.png")));
        imageView.setImage(image);
    }

    @Override
    public Position canMove() {
        int y = this.pos.y();
        int x = this.pos.x();

        switch (currentAngle) {
            case 0: y--; break;             // Up
            case 45: x++; y--; break;
            case 90: x++; break;          // Right
            case 135: x++; y++; break;
            case 180: y++; break;         // Down
            case 225: x--; y++; break;
            case 270: x--; break;         // Left
            case 315: x--; y--; break;
            default: return null;         // TODO: Handle other angles, we only support 45-degree increments
        }

        Position pos = new Position(x, y);
        if (Room.getInstance().isPositionFree(pos) && _checkDiagonals(pos)) {
            return pos;
        }
        return null;
    }

    /**
     * Moves the robot forward.
     */
    public void Go() {
        for (int i = 0; i < speed; i++) {
            semaphore.acquireUninterruptibly();
            Position prevPos = this.pos;
            Position newPos = canMove();
            if (newPos == null) {
                logger.warning("ManualRobot (" + this.id + ") cannot move forward, there is an obstacle on the way");
                semaphore.release();
                return;
            }
            this.pos = newPos;
            // Make translate transition
            TranslateTransition tt = new TranslateTransition(Duration.millis((double) Playground.tickPeriod / speed), getImageView());
            tt.setByX((newPos.x() - prevPos.x()) * Playground.gridWidth);
            tt.setByY((newPos.y() - prevPos.y()) * Playground.gridWidth);
            tt.setCycleCount(1);
            tt.setAutoReverse(true);
            tt.setInterpolator(Interpolator.LINEAR);
            playSemaphore.acquireUninterruptibly();
            tt.play();
            playSemaphore.release();
            tt.setOnFinished(event -> {
                semaphore.release();
                logger.info("ManualRobot (" + this.id + ") moved to " + this.pos);
                addToBackTransition(tt);
            });
        }
    }


    public void Left() {
        semaphore.acquireUninterruptibly();
        rotate(-this.stepAngle);
        RotateTransition rt = new RotateTransition(Duration.millis((double) Playground.tickPeriod / speed), getImageView());
        rt.setByAngle(-this.stepAngle);
        rt.setCycleCount(1);
        rt.setAutoReverse(true);
        rt.setInterpolator(Interpolator.LINEAR);
        playSemaphore.acquireUninterruptibly();
        rt.play();
        playSemaphore.release();
        rt.setOnFinished(event -> {
            semaphore.release();
            logger.info("ManualRobot ("+this.id+") rotated left");
        });
    }

    private void Right() {
        semaphore.acquireUninterruptibly();
        rotate(this.stepAngle);
        RotateTransition rt = new RotateTransition(Duration.millis((double) Playground.tickPeriod / speed), getImageView());
        rt.setByAngle(this.stepAngle);
        rt.setCycleCount(1);
        rt.setAutoReverse(true);
        rt.setInterpolator(Interpolator.LINEAR);
        playSemaphore.acquireUninterruptibly();
        rt.play();
        playSemaphore.release();
        rt.setOnFinished(event ->
        {
            semaphore.release();
            logger.info("ManualRobot ("+this.id+") rotated right");
            addToBackTransition(rt);
        });
    }

    private void Nothing() {
        semaphore.acquireUninterruptibly();
        Transition tt = new Transition() {
            {
                setCycleDuration(Duration.millis((double) Playground.tickPeriod / speed));
            }
            @Override
            protected void interpolate(double frac) {
                // do nothing
            }
        };
        try {
            Thread.sleep((long) Playground.tickPeriod / speed);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        semaphore.release();
        logger.info("ManualRobot ("+this.id+") did nothing");
        addToBackTransition(tt);
    }

    @Override
    public boolean move() {
        queue.add("Go");
        logger.info("ManualRobot ("+this.id+") got request to move forward ADDED TO QUEUE");
        return true;
    }

    @Override
    public void tick() {
        tickSemaphore.acquireUninterruptibly();
        String action = queue.poll();
        if (!running) {
            tickSemaphore.release();
            return;
        }
        if (action == null) {
            Nothing();
            logger.info("ManualRobot ("+this.id+") did nothing SKIP");
        } else {
            switch (action) {
                case "Go":
                    Go();
                    break;
                case "Left":
                    Left();
                    break;
                case "Right":
                    Right();
                    break;
                default:
                    throw new RuntimeException("Unknown action: " + action);
            }
        }
        tickSemaphore.release();
    }

    public void start() {
        running = true;
        logger.info("ManualRobot ("+this.id+") got request to start");
    }

    public void pause() {
        queue.clear();
        running = false;
        logger.info("ManualRobot ("+this.id+") got request to pause CLEARED QUEUE");
    }

    public void rotateLeft() {
        queue.add("Left");
        logger.info("ManualRobot ("+this.id+") got request to rotate left ADDED TO QUEUE");
    }

    public void rotateRight() {
        queue.add("Right");
        logger.info("ManualRobot ("+this.id+") got request to rotate right ADDED TO QUEUE");
    }
}
