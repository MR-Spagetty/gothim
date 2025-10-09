package ecs.engr302.team14.gothim.renderer.behaviours;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.BasicAbsoluteTeleportTile;
import ecs.engr302.team14.gothim.tiles.BasicRelativeTeleportTile;
import ecs.engr302.team14.gothim.tiles.Passable;
import ecs.engr302.team14.gothim.tiles.PrimitiveEffectTile;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.tiles.Solid;
import ecs.engr302.team14.gothim.util.Direction;
import ecs.engr302.team14.gothim.util.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Condition that is active tif the adjacent tile in the given direction is of the given style.
 */
public record TypePresent(
        @SerializedField
        String type,
        @SerializedField
        Direction side
) implements BehaviourCondition {

    @DeserializationMethod(serialFieldNames = { "type", "side" })
    public TypePresent {}

    @Override
    public Boolean applies(PrimitiveTile to, Map<Point, PrimitiveTile> neighbours) {
        neighbours = new HashMap<>(neighbours);
        neighbours.remove(to.pos());
        if (neighbours.size() != 8) {
            throw new IllegalArgumentException("Must have all 8 neighbours");
        }
        PrimitiveTile ref = neighbours.get(to.pos().add(side.offset));
        return switch (type) {
            case "solid" -> ref instanceof Solid;
            case "passable" -> ref instanceof Passable;
            case "effect" -> ref instanceof PrimitiveEffectTile;
            case "tp" -> ref instanceof BasicAbsoluteTeleportTile;
            case "rel-tp" -> ref instanceof BasicRelativeTeleportTile;
            default -> throw new RuntimeException("Unknown tile type: " + type);
        };
    }
}