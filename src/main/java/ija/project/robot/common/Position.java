package ija.project.robot.common;

public record Position(int x, int y) {

    public Position(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Position coordinates must be non-negative.");
        }
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position {"+ x + "," + y + "}";
    }
}
