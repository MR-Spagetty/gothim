package ecs.engr302.team14.gothim.renderer.behaviours;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;
import java.util.Map;

/**
 * Proxy behaviour that inverts the inner Behaviour.
 *
 * @author MR-Spagetty
 */
public record Not(
        @SerializedField
        BehaviourCondition inner
) implements BehaviourCondition {

    @DeserializationMethod(serialFieldNames = { "inner" })
    public Not {}

    @Override
    public Boolean applies(PrimitiveTile to, Map<Point, PrimitiveTile> neighbours) {
        return !inner.applies(to, neighbours);
    }

}
