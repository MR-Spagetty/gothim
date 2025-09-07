package ecs.engr302.team14.gothim.logic;

import ecs.engr302.team14.gothim.persistancy.annotations.HasSerializedConstants;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedConstant;

/**
 * Simple enum for storing the access modifiers for ease of use.
 *
 * @author MR-Spagetty
 */
@HasSerializedConstants
public enum AccessModifier {
    @SerializedConstant
    Public,
    @SerializedConstant
    Private,
    @SerializedConstant
    Static;
}
