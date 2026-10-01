package se233.chapter5part2.model;

import javafx.geometry.Point2D;

public enum Direction {
    RIGHT(new Point2D(1, 0)),
    LEFT(new Point2D(-1, 0)),
    UP(new Point2D(0,-1)),
    DOWN(new Point2D(0, 1));
    public final Point2D current;
    Direction(Point2D current) { this.current = current; }

    /** The direction that would make the snake turn back on itself. */
    public Direction opposite() { //ประกาศเมธอดหาทิศทางตรงกันข้ามของทิศปัจจุบัน
        return switch (this) { //ใช้ switch-expression จับคู่ทิศตรงข้าม:
            case RIGHT -> LEFT; //ถ้าเป็น RIGHT ทิศตรงข้ามคือ LEFT
            case LEFT -> RIGHT;
            case UP -> DOWN; //ถ้าเป็น UP ทิศตรงข้ามคือ DOWN
            case DOWN -> UP;
        };
    }
}
