package ecs.engr302.team14.gothim.renderer.behaviours;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;
import java.util.ArrayList;
import java.util.Map;

/**
 * Condition that contains an arbitrary number of sub conditions and is true if
 * any of the subconditions are.
 *
 * @author MR-Spagetty
 */
public record All(
        @SerializedField
        ArrayList<BehaviourCondition> conds
) implements BehaviourCondition {

    @DeserializationMethod(serialFieldNames = { "conds" })
    public All {}

    @Override
    public Boolean applies(PrimitiveTile to, Map<Point, PrimitiveTile> neighbours) {
        return conds.parallelStream().allMatch(cond -> cond.applies(to, neighbours));
    }

}
