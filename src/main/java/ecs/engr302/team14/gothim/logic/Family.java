package ecs.engr302.team14.gothim.logic;

import ecs.engr302.team14.gothim.persistancy.annotations.HasSerializedConstants;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedConstant;

/**
 * Enum that represents the possible Families that a player can eb part of od be disguised as.
 *
 * @authro MR-Spagetty
 */
@HasSerializedConstants
public enum Family implements DisguiseableAs {
    @SerializedConstant
    None,
    @SerializedConstant
    Robbersons,
    @SerializedConstant
    Theiftons,
    @SerializedConstant
    Stealalots,
    @SerializedConstant
    Crooks;
}
