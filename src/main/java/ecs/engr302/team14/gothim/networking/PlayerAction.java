
package ecs.engr302.team14.gothim.networking;

import ecs.engr302.team14.gothim.persistancy.annotations.HasSerializedConstants;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedConstant;

/**
 * Enum of all grid based Player actions for use in Action packets.
 *
 * @author MR-Spagetty
 */
@HasSerializedConstants
public enum PlayerAction {
    @SerializedConstant MoveUp,
    @SerializedConstant MoveDown,
    @SerializedConstant MoveLeft,
    @SerializedConstant MoveRight,
    @SerializedConstant InteractN,
    @SerializedConstant InteractNE,
    @SerializedConstant InteractE,
    @SerializedConstant InteractSE,
    @SerializedConstant InteractS,
    @SerializedConstant InteractSW,
    @SerializedConstant InteractW,
    @SerializedConstant InteractNW
}
