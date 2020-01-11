package fmi.ai.kmeans.core.helpers;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Point {

    private static final double DELTA = 0.001;

    public double x = 0.0;
    public double y = 0.0;

    @Contract(pure = true)
    public Point() {

    }

    @Contract(pure = true)
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Contract(pure = true)
    public Point(@NotNull Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(x).append(", ").append(y).append(")");
        return sb.toString();
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Math.abs(point.x - this.x) <= DELTA &&
                Math.abs(point.y - this.y) <= DELTA;

    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
