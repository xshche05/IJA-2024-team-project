package ija.project.robot.logic.common;

public record Position(int x, int y) {

    @Override
    public String toString() {
        return "Position {"+ x + "," + y + "}";
    }
}
