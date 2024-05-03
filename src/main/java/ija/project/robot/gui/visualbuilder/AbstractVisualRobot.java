package ija.project.robot.gui.visualbuilder;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.robots.AbstractRobot;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public abstract class AbstractVisualRobot {
    private AbstractRobot realRobot;

    int x, y;
    private Playground controller;
    private TranslateTransition transition;

    private Circle robotVisual;
    private Circle directionIndicator;
    private StackPane visual; // Using StackPane to group the robot and its direction indicator
    private int viewAngle;

    public AbstractVisualRobot(int x, int y, Playground controller) {
        this.controller = controller;
        this.visual = new StackPane();

        // Initialize visual components
        this.robotVisual = new Circle(controller.gridWidth / 2); // Assuming gridWidth is a static or global value for grid cell size
        this.directionIndicator = new Circle(controller.gridWidth / 4, Color.BLACK);
        this.visual.getChildren().addAll(robotVisual, directionIndicator);

        // Initialize the TranslateTransition for smooth movement
        this.transition = new TranslateTransition(Duration.seconds(1), this.visual);
        this.transition.setCycleCount(1);
        this.transition.setAutoReverse(false);

        this.visual.setOnMouseClicked(e -> handleRobotClick());

        // updateVisual();
    }

    private void handleRobotClick() {
        // Inform the controller that this robot has been selected
        controller.selectRobot(this);
    }

    public void selectRobot() {
        // Set the stroke color to red to indicate selection
        robotVisual.setStroke(Color.RED);
        robotVisual.setStrokeWidth(3);
    }

    public void deselectRobot() {
        // Reset the stroke color to black or whatever the default is
        robotVisual.setStroke(Color.BLACK);
        robotVisual.setStrokeWidth(1);
    }

    public StackPane getVisual() {
        return visual;
    }

    public AbstractRobot getRealRobot() {
        return realRobot;
    }

    public void setRealRobot(AbstractRobot realRobot) {
        this.realRobot = realRobot;
        updateVisual(); // Update visual whenever the real robot changes
    }

    public void updateVisual() {
        if (realRobot != null) {
            updateDirectionIndicator();
        }
    }

    protected void updateDirectionIndicator() {
        if (realRobot != null) {
            int viewAngle = realRobot.getViewAngle();
            double angleRad = Math.toRadians(viewAngle - 90);
            double radius = robotVisual.getRadius() * 0.75;

            // Calculate the new position of the direction indicator
            directionIndicator.setTranslateX(radius * Math.cos(angleRad));
            directionIndicator.setTranslateY(radius * Math.sin(angleRad));
        }
    }


}
