package ija.project.robot.logic.robots;

import ija.project.robot.logic.common.AbstractRoomObject;
import ija.project.robot.logic.common.Position;
import ija.project.robot.logic.room.Room;
import javafx.scene.image.ImageView;

public abstract class AbstractRobot extends AbstractRoomObject {
    protected int currentAngle;
    protected int speed = 1;
    protected int stepAngle = 45;
    public AbstractRobot(Position pos) {
        super(pos);
        this.currentAngle = 0;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void setStepAngle(int rotate_angle) {
        this.stepAngle = rotate_angle;
    }
    public int getCurrentAngle() {
        return this.currentAngle;
    }
    protected boolean _checkDiagonals(Position newPos) {
        if (this.currentAngle % 90 == 0) {
            return true;
        }
        Position p1 = new Position(this.pos.x(), newPos.y());
        Position p2 = new Position(newPos.x(), this.pos.y());
        return Room.getInstance().isPositionFree(p1) && Room.getInstance().isPositionFree(p2);
    }
    protected void rotate(int angle) {
        this.currentAngle = (this.currentAngle + angle) % 360;
        if (this.currentAngle < 0) {
            this.currentAngle += 360;
        }
    }
    public abstract Position canMove();
    public abstract boolean move();
}
