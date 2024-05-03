package ija.project.robot.gui.visualbuilder;

import ija.project.robot.gui.controllers.Playground;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class VisualManualRobot {
    int x, y;
    private Circle visual;
    private Playground controller;
    private TranslateTransition transition;


    public VisualManualRobot(int x, int y, int size, Playground controller) {
        this.x = x;
        this.y = y;
        this.controller = controller;
        this.visual = new Circle();
        this.visual.setCenterX(x);
        this.visual.setCenterY(y);
        this.visual.setRadius((int)(size/2));
        this.visual.setFill(Color.WHITE);
        this.controller.AnchorPane.getChildren().add(this.visual);
        this.visual.setStroke(javafx.scene.paint.Color.BLACK);

        this.transition = new TranslateTransition(Duration.seconds(1), this.visual); // 1 second for the transition
        this.transition.setCycleCount(1);
        this.transition.setAutoReverse(false);

        this.visual.setOnMouseClicked(e -> selectRobot());
    }

    public void selectRobot() {
        this.visual.setFill(Color.RED);
    }

    public Circle getVisual() {
        return visual;
    }

}
