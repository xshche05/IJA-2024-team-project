package ija.project.robot.robots;

import ija.project.robot.common.Position;

public class ManualRobot extends AbstractRobot{

    private final System.Logger logger;

    public ManualRobot(Position pos) {
        super(pos);
        logger = System.getLogger("ManualRobot id: " + this.id);
        logger.log(System.Logger.Level.INFO,
                "ManualRobot created on " + pos);
    }

    public void rotateLeft() {
        rotate(this.rotate_angle);
        logger.log(System.Logger.Level.INFO,
                "ManualRobot rotated left");
    }

    public void rotateRight() {
        rotate(-this.rotate_angle);
        logger.log(System.Logger.Level.INFO,
                "ManualRobot rotated right");
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean move() {
        if (!canMove()) {
            logger.log(System.Logger.Level.WARNING,
                    "ManualRobot cannot move forward, there is an obstacle in the way");
            return false;
        }
        return true;
    }

    @Override
    public void tick() {
        return;
    }
}
