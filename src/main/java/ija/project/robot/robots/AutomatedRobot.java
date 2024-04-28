package ija.project.robot.robots;

import ija.project.robot.common.Position;
import ija.project.robot.room.Room;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class AutomatedRobot extends AbstractRobot {

    private int view_distance = 1;
    private final System.Logger logger;
    private boolean moving = false;
    // Queue of commands
    private final Queue<String> path = new LinkedList<>();

    private final Semaphore semaphore = new Semaphore(0);


    public AutomatedRobot(Position pos) {
        super(pos);
        logger = System.getLogger("AutomatedRobot id: " + this.id);
        logger.log(System.Logger.Level.INFO,
                "AutomatedRobot created on " + pos + " with view distance " + view_distance);
    }

    public Queue<String> getPath() {
        return path;
    }

    public void setView_distance(int view_distance) {
        this.view_distance = view_distance;
        logger.log(System.Logger.Level.INFO,
                "AutomatedRobot view distance set to " + view_distance);
    }

    public Position _canMoveFrom(Position pos) {
        int y = pos.y();
        int x = pos.x();
        switch (view_angle) {
            case 0: y--; break;          // Up
            case 45: x++; y--; break;
            case 90: x++; break;          // Right
            case 135: x++; y++; break;
            case 180: y++; break;         // Down
            case 225: x--; y++; break;
            case 270: x--; break;         // Left
            case 315: x--; y--; break;
            default: return null;           // TODO: throw exception
        }
        Position position = new Position(x, y);
        if (Room.getInstance().isPositionFree(position) && _checkDiagonals(position)) {
            return position;
        }
        return null;
    }

    @Override
    public Position canMove() {
        Position startPos = this.pos;
        Position dstPos = _canMoveFrom(startPos);
        for (int i = 0; i < view_distance-1; i++) {
            Position newPos = _canMoveFrom(startPos);
            if (newPos != null) {
                startPos = newPos;
            } else {
                return null;
            }
        }
        return dstPos;
    }

    public void rotate() {
        super.rotate(rotate_angle);
        logger.log(System.Logger.Level.INFO,
                "AutomatedRobot (" + this.id + ") rotated");
        path.add("rotate " + this.rotate_angle);
    }
    @Override
    public boolean move() {
        Position newPos = canMove();
        if (newPos == null) {
            logger.log(System.Logger.Level.WARNING,
                    "AutomatedRobot cannot move forward, there is an obstacle in the way");
            return false;
        }
        this.pos = newPos;
        path.add("move");
        logger.log(System.Logger.Level.INFO,
                "AutomatedRobot (" + this.id + ") moved to " + newPos);
        return true;
    }

    public void tick() {
        semaphore.release();
    }

    public void startMoving(){
        moving = true;
        logger.log(System.Logger.Level.INFO,
                "AutomatedRobot started moving");
    }

    public void stopMoving(){
        moving = false;
        logger.log(System.Logger.Level.INFO,
                "AutomatedRobot stopped moving");
    }

    public void run() throws InterruptedException {
        while (moving) {
            semaphore.acquire();
            if (!moving) break;
            for (int i = 0; i < speed; i++) {
                if (!move()) {
                    rotate();
                }
            }
        }
    }
}
