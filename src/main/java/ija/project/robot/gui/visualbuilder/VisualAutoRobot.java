package ija.project.robot.gui.visualbuilder;

import ija.project.robot.gui.controllers.Playground;
import ija.project.robot.logic.robots.AbstractRobot;
import javafx.scene.paint.Color;

public class VisualAutoRobot extends AbstractVisualRobot{


    public VisualAutoRobot(int x, int y, Playground controller, AbstractRobot Robot) {
        super(controller, Robot);
        color = Color.LIGHTCORAL;
    }
}
