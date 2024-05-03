package ija.project.robot.gui.visualbuilder;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.robots.AbstractRobot;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public abstract class AbstractVisualRobot {
    protected AbstractRobot realRobot;

    protected int x, y;
    protected Playground controller;
    protected TranslateTransition transition;

    protected Circle robotBody;
    protected Circle directionIndicator;
    protected StackPane visual; // Using StackPane to group the robot and its direction indicator

    public AbstractVisualRobot(Playground controller, AbstractRobot Robot) {
        this.realRobot = Robot;
        this.controller = controller;
        this.visual = new StackPane();
    }

    protected abstract void createRobotBody();

    protected void initializeTransition() {
        this.transition = new TranslateTransition(Duration.seconds(1), this.visual);
        this.transition.setCycleCount(1);
        this.transition.setAutoReverse(false);
    }

    protected void updatePosition() {
        this.x = realRobot.getPosition().x();
        this.y = realRobot.getPosition().y();
    }

    protected void updateVisual() {
        if (realRobot != null) {
            updatePosition();

            int viewAngle = realRobot.getViewAngle();
            double angleRad = Math.toRadians(viewAngle + 90);
            double radius = robotBody.getRadius();

            // Calculate the new position of the direction indicator
            directionIndicator.setTranslateX(radius * Math.cos(angleRad));
            directionIndicator.setTranslateY(radius * Math.sin(angleRad));

            playTransition();
        }
    }

    protected void playTransition() {
        transition.setToX(this.x * Playground.gridWidth);
        transition.setToY(this.y * Playground.gridWidth);
        transition.playFromStart();
    }

    public StackPane getVisual() {
        return visual;
    }
}
