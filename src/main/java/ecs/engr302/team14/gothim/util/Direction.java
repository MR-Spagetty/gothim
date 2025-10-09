package ecs.engr302.team14.gothim.util;

import ecs.engr302.team14.gothim.persistancy.annotations.HasSerializedConstants;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedConstant;

/**
 * Simple Direction enum for making movement simple.
 */
@HasSerializedConstants
public enum Direction {
    @SerializedConstant
    Up(new Point(0, -1)),
    @SerializedConstant
    Down(new Point(0, 1)),
    @SerializedConstant
    Left(new Point(-1, 0)),
    @SerializedConstant
    Right(new Point(1, 0)),
    @SerializedConstant
    UpLeft(new Point(-1, -1)),
    @SerializedConstant
    UpRight(new Point(1, -1)),
    @SerializedConstant
    DownLeft(new Point(-1, 1)),
    @SerializedConstant
    DownRight(new Point(1, 1));

    public final Point offset;

    Direction(Point offset) {
        this.offset = offset;
    }
}
