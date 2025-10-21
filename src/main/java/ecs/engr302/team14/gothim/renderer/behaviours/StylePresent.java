package ecs.engr302.team14.gothim.renderer.behaviours;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Direction;
import ecs.engr302.team14.gothim.util.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Condition that is active tif the adjacent tile in the given direction is of the given style.
 */
public record StylePresent(
        @SerializedField
        String style,
        @SerializedField
        Direction side
) implements BehaviourCondition {

    @DeserializationMethod(serialFieldNames = { "style", "side" })
    public StylePresent {}

    @Override
    public Boolean applies(PrimitiveTile to, Map<Point, PrimitiveTile> neighbours) {
        neighbours = new HashMap<>(neighbours);
        neighbours.remove(to.pos());
        if (neighbours.size() != 8) {
            throw new IllegalArgumentException("Must have all 8 neighbours");
        }
        return neighbours.get(to.pos().add(side.offset)).style.equals(style);
    }
}
