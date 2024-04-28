package ija.project.robot.robots;

import ija.project.robot.common.AbstractRoomObject;
import ija.project.robot.common.Position;
import ija.project.robot.room.Room;

public abstract class AbstractRobot extends AbstractRoomObject {
    protected int view_angle;
    protected int speed = 1;
    protected  int rotate_angle = 45;
    public AbstractRobot(Position pos) {
        super(pos);
        this.view_angle = 0;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setRotate_angle(int rotate_angle) {
        this.rotate_angle = rotate_angle;
    }

    protected boolean _checkDiagonals(Position newPos) {
        Position p1 = new Position(this.pos.y(), newPos.x());
        Position p2 = new Position(newPos.y(), this.pos.x());
        return Room.getInstance().isPositionFree(p1) || Room.getInstance().isPositionFree(p2);
    }

    protected void rotate(int angle) {
        this.view_angle = (this.view_angle + angle) % 360;
    }
    public int getViewAngle() {
        return this.view_angle;
    }
    public abstract boolean canMove();
    public abstract boolean move();
    public abstract void tick();
}
