package ecs.engr302.team14.gothim.renderer.behaviours;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;
import java.util.Map;

/**
 * Simple OR behaviour.
 *
 * @authro MR-Spagetty
 */
public record And(
        @SerializedField
        BehaviourCondition a,
        @SerializedField
        BehaviourCondition b
) implements BehaviourCondition {
    @DeserializationMethod(serialFieldNames = { "a", "b" })
    public And {}

    @Override
    public Boolean applies(PrimitiveTile to, Map<Point, PrimitiveTile> neighbours) {
        return a.applies(to, neighbours) && b.applies(to, neighbours);
    }
}
