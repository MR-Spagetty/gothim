package ecs.engr302.team14.gothim.util;

import java.util.Comparator;
import java.util.Objects;

/**
 * A basic Point object for storing coordinates.
 *
 * @param x the x coordinate
 * @param y the y coordinate
 * @author MR-Spagetty
 */
public record Point(double x, double y) implements Comparable<Point> {
    /**
     * add two points together.
     *
     * @param other the point to add ot this one
     * @return the new point sum
     */
    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    /**
     * subtract the given point from this one.
     *
     * @param other the point to subtract
     * @return the new point result
     */
    public Point sub(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    /**
     * multiply this point by the given scalar.
     *
     * @param scalar the multiplier to use
     * @return the new Point result
     */
    public Point mul(double scalar) {
        return new Point(this.x * scalar, this.y * scalar);
    }

    /**
     * divide this point by the given divisor.
     *
     * @param divisor the value to divide this point by
     * @return the new Point result
     */
    public Point div(double divisor) {
        return mul(1 / divisor);
    }

    /**
     * Calculate the magnitude of this point.
     *
     * @return the magnitude of this point
     */
    public Double mag() {
        return Math.hypot(x, y);
    }

    @Override
    public int compareTo(Point o) {
        final Comparator<Point> comp = Comparator.comparingDouble(Point::y)
                .thenComparingDouble(Point::x);
        return comp.compare(this, o);
    }

    @Override
    public final boolean equals(Object other) {
        if (other.getClass() == this.getClass() && other instanceof Point otherP) {
            return this.x == otherP.x && this.y == otherP.y;
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}