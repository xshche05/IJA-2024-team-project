package ija.project.robot.common;

public record Position(int x, int y) {

    @Override
    public String toString() {
        return "Position {"+ x + "," + y + "}";
    }
}
