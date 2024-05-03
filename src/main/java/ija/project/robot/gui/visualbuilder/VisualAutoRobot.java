package ija.project.robot.gui.visualbuilder;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.robots.AbstractRobot;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class VisualAutoRobot extends AbstractVisualRobot{


    public VisualAutoRobot(int x, int y, Playground controller, AbstractRobot Robot) {
        super(controller, Robot);
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

}
