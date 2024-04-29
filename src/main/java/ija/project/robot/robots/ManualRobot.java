package ija.project.robot.robots;

import ija.project.robot.common.Position;
import ija.project.robot.room.Room;

public class ManualRobot extends AbstractRobot {

    public ManualRobot(Position pos) {
        super(pos);
        logger.info("ManualRobot ("+this.id+") created at " + pos);
    }

    @Override
    public Position canMove() {
        int row = this.pos.y();
        int col = this.pos.x();

        switch (view_angle) {
            case 0: row-- ; break;          // Up
            case 45: col++; row--; break;
            case 90: col++; break;          // Right
            case 135: col++; row++; break;
            case 180: col++; break;         // Down
            case 225: col--; row++; break;
            case 270: col--; break;         // Left
            case 315: col--; row--; break;
            default: return null;           // TODO: throw exception
        }

        this.pos = new Position(row, col);
        if (Room.getInstance().isPositionFree(this.pos) && _checkDiagonals(this.pos)) {
            return this.pos;
        }
        return null;
    }

    public void rotateLeft() {
        rotate(this.rotate_angle);
        logger.info("ManualRobot ("+this.id+") rotated left");
    }

    public void rotateRight() {
        rotate(-this.rotate_angle);
        logger.info("ManualRobot ("+this.id+") rotated right");
    }

    @Override
    public boolean move() {
        Position newPos = canMove();
        if (newPos == null) {
            logger.warning("ManualRobot ("+this.id+") cannot move forward, there is an obstacle on the way");
            return false;
        }
        this.pos = newPos;
        logger.info("ManualRobot ("+this.id+") moved to " + this.pos);
        return true;
    }
}
