package ija.project.robot.logic.room;

import ija.project.robot.logic.common.AbstractRoomObject;
import ija.project.robot.logic.common.Position;

public class Obstacle extends AbstractRoomObject {

    private final System.Logger logger;

    public Obstacle(Position pos) {
        super(pos);
        logger = System.getLogger("Obstacle id: " + this.id);
        logger.log(System.Logger.Level.INFO,
                "Obstacle created on " + pos);
    }

}
