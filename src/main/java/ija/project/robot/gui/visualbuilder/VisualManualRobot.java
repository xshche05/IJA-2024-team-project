package ija.project.robot.gui.visualbuilder;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.robots.AbstractRobot;
import ija.project.robot.logic.robots.ManualRobot;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class VisualManualRobot extends AbstractVisualRobot{

    private final ManualRobot manualRobot; // Dedicated reference for ManualRobot

    public VisualManualRobot(Playground controller, AbstractRobot robot) {
        super(controller, robot);
        if (!(robot instanceof ManualRobot)) {
            throw new IllegalArgumentException("VisualManualRobot requires a ManualRobot instance");
        }
        this.manualRobot = (ManualRobot) robot;

        updatePosition();
        createRobotBody();
        initializeTransition();

        this.visual.setOnMouseClicked(e -> handleRobotClick());
        updateVisual();
    }

    @Override
    protected void createRobotBody() {
        // Create the robot body
        this.robotBody = new Circle((int) (Playground.gridWidth / 2), Color.CORNFLOWERBLUE);
        robotBody.setStroke(Color.BLACK);
        robotBody.setStrokeWidth(1);
        // Create the direction indicator
        this.directionIndicator = new Circle((int) (Playground.gridWidth / 4), Color.BLACK);
        this.visual.getChildren().addAll(robotBody, directionIndicator);
    }

    protected void handleRobotClick() {
        // Inform the controller that this robot has been selected
        controller.selectRobot(this);
    }

    public void selectRobot() {
        // Set the stroke color to red to indicate selection
        robotBody.setStroke(Color.RED);
        robotBody.setStrokeWidth(2);
    }

    public void deselectRobot() {
        // Reset the stroke color to black or whatever the default is
        robotBody.setStroke(Color.BLACK);
        robotBody.setStrokeWidth(1);
    }

    public void move() {
        realRobot.move();
        updateVisual();
    }

    public void turnLeft() {
        manualRobot.rotateLeft();
        updateVisual();
    }

    public void turnRight() {
        manualRobot.rotateRight();
        updateVisual();
    }
}
