package ija.project.robot.common;

public abstract class AbstractRoomObject {
    protected Position pos;
    protected int id;
    private static int id_counter = 0;


    public AbstractRoomObject(Position pos) {
        this.pos = pos;
        this.id = id_counter++;
    }

    public int getId() {
        return this.id;
    }

    public Position getPosition() {
        return this.pos;
    }

    public void setPosition(Position pos) {
        this.pos = pos;
    }
}
