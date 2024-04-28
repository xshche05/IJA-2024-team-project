package ija.project.robot.robots;

import ija.project.robot.common.Position;

public class AutomatedRobot extends AbstractRobot {

    private int view_distance = 1;
    private final System.Logger logger;
    private boolean moving = false;
    private int tick_counter = 0;


    public AutomatedRobot(Position pos) {
        super(pos);
        logger = System.getLogger("AutomatedRobot id: " + this.id);
        logger.log(System.Logger.Level.INFO,
                "AutomatedRobot created on " + pos + " with view distance " + view_distance);
    }

    public void setView_distance(int view_distance) {
        this.view_distance = view_distance;
        logger.log(System.Logger.Level.INFO,
                "AutomatedRobot view distance set to " + view_distance);
    }

    public void rotate() {
        rotate(this.rotate_angle);
        logger.log(System.Logger.Level.INFO,
                "AutomatedRobot rotated");
    }

    @Override
    public boolean canMove() {
        int checkX = this.pos.x();
        int checkY = this.pos.y();

        for(int i = 0; i < this.view_distance; i++){
            switch (this.view_angle) {
                case 0:     checkRow--; break;                // Up
                case 45:    checkRow--; checkCol ++; break;
                case 90:    checkCol ++; break;               // Right
                case 135:   checkRow ++; checkCol ++; break;
                case 180:   checkRow ++; break;               // Down
                case 225:   checkRow ++; checkCol --; break;
                case 270:   checkCol --; break;               // Left
                case 315:   checkRow --; checkCol --; break;
                default:    return false;                       // Unknown angle
            }

            Position checkPos = new Position(checkRow, checkCol);
            // if position does not exist         or  object at the position   or   objects at the diagonals to the movement direction    then  false
            if(!this.env.containsPosition(checkPos) || this.env.objectAt(checkPos) || !(this.angle % 90 == 0 || !this._checkDiagonals(checkPos))){return false;}
        }

        return true;
    }

    @Override
    public boolean move() {

        if(!canMove()){
            return false;
        }

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
            default: return false;          // TODO: Exception
        }
        // New position
        this.pos = new Position(row, col);
        return true;
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

    public void tick() {
        tick_counter = (tick_counter + 1) % 20;
        if (tick_counter > speed) {
            return;
        }
        if(moving) {
            if(!move()){
                rotate();
            }
        }
    }
}
