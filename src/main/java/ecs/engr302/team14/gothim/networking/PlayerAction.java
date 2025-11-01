
package ecs.engr302.team14.gothim.networking;

import ecs.engr302.team14.gothim.persistancy.annotations.HasSerializedConstants;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedConstant;
import ecs.engr302.team14.gothim.util.Direction;

/**
 * Enum of all grid based Player actions for use in Action packets.
 *
 * @author MR-Spagetty
 */
@HasSerializedConstants
public enum PlayerAction {
    @SerializedConstant MoveUp(Direction.Up),
    @SerializedConstant MoveDown(Direction.Down),
    @SerializedConstant MoveLeft(Direction.Left),
    @SerializedConstant MoveRight(Direction.Right),
    @SerializedConstant InteractN(Direction.Up),
    @SerializedConstant InteractNE(Direction.UpRight),
    @SerializedConstant InteractE(Direction.Right),
    @SerializedConstant InteractSE(Direction.DownRight),
    @SerializedConstant InteractS(Direction.Down),
    @SerializedConstant InteractSW(Direction.DownLeft),
    @SerializedConstant InteractW(Direction.Left),
    @SerializedConstant InteractNW(Direction.UpLeft);

    public final Direction dir;

    PlayerAction(Direction dir) {
        this.dir = dir;
    }
}
