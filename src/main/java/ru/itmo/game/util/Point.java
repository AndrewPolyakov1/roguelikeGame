package ru.itmo.game.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Point {
    public int x;
    public int y;

    public Point(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y) {
        this.x = x;
        this.y = y;
    }

    public static Point of(int x, int y) {
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Point && ((Point) other).x == this.x && ((Point) other).y == this.y);
    }

    public boolean equals(Point other) {
        return (other.x == this.x && other.y == this.y);
    }

    public static void main(String[] args) {
        Point a = Point.of(1, 2);
        assert (a.equals(Point.of(1, 2)));
    }
}
