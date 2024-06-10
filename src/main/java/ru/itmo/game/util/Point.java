package ru.itmo.game.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

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

    public Point add(Point other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public boolean equals(Point other) {
        return (other.x == this.x && other.y == this.y);
    }

    public static void main(String[] args) {
        Point a = Point.of(1, 2);
        assert (a.equals(Point.of(1, 2)));
    }

    public static List<Point> getOffsetsInRadius(Point center, int radius) {
        List<Point> offsets = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int distanceSquared = dx * dx + dy * dy;
                if (distanceSquared <= radius * radius) {
                    offsets.add(new Point(center.x + dx, center.y + dy));
                }
            }
        }
        return offsets;
    }

}
