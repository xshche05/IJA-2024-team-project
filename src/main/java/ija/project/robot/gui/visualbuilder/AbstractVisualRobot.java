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

    protected Circle robotVisual;
    protected Circle directionIndicator;
    protected StackPane visual; // Using StackPane to group the robot and its direction indicator
    protected Color color;

    public AbstractVisualRobot(Playground controller, AbstractRobot Robot) {
        this.realRobot = Robot;
        this.controller = controller;


        this.visual = new StackPane();

        this.robotVisual = new Circle((int) (controller.gridWidth / 2)); // Assuming gridWidth is a static or global value for grid cell size
        robotVisual.setStroke(Color.BLACK);
        robotVisual.setStrokeWidth(1);

        this.directionIndicator = new Circle((int) (controller.gridWidth / 4), Color.BLACK);

        this.visual.getChildren().addAll(robotVisual, directionIndicator);

        // Initialize the TranslateTransition for smooth movement
        updatePosition();
        initializeTransition();
    }

    private void initializeTransition() {
        this.transition = new TranslateTransition(Duration.seconds(1), this.visual);
        this.transition.setCycleCount(1);
        this.transition.setAutoReverse(false);
    }


    protected void updatePosition() {
        this.x = realRobot.getPosition().x();
        this.y = realRobot.getPosition().y();
    }

    public StackPane getVisual() {
        return visual;
    }

    public AbstractRobot getRealRobot() {
        return realRobot;
    }

    protected void updateVisual() {
        if (realRobot != null) {
            int viewAngle = realRobot.getViewAngle();
            double angleRad = Math.toRadians(viewAngle - 90);
            double radius = robotVisual.getRadius() * 0.75;

            // Calculate the new position of the direction indicator
            directionIndicator.setTranslateX(radius * Math.cos(angleRad));
            directionIndicator.setTranslateY(radius * Math.sin(angleRad));

            playTransition();
        }
    }

    protected void playTransition() {
        transition.setToX(this.x * controller.gridWidth);
        transition.setToY(this.y * controller.gridWidth);
        transition.playFromStart();
    }

}
