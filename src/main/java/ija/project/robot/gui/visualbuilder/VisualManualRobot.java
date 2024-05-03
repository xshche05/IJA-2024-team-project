package ija.project.robot.gui.visualbuilder;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.robots.AbstractRobot;
import ija.project.robot.logic.robots.ManualRobot;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class VisualManualRobot extends AbstractVisualRobot{

    private ManualRobot manualRobot; // Dedicated reference for ManualRobot

    public VisualManualRobot(Playground controller, AbstractRobot robot) {
        super(controller, robot);
        if (!(robot instanceof ManualRobot)) {
            throw new IllegalArgumentException("VisualManualRobot requires a ManualRobot instance");
        }
        this.manualRobot = (ManualRobot) robot;
        this.color = Color.CORNFLOWERBLUE;

        this.visual.setOnMouseClicked(e -> handleRobotClick());
        updateVisual();
    }

    protected void handleRobotClick() {
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

    public void move() {
        realRobot.move();
        updatePosition();
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
