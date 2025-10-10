package ecs.engr302.team14.gothim.renderer.behaviours;

import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Direction;
import java.util.ArrayList;

/**
 * Behaviour is applied if an entity of matching categories can enter is at the specified side.
 *
 * @author MR-Spagetty
 */
public record EntityCanEnterFrom(
        @SerializedField
        Direction side,
        @SerializedField
        ArrayList<String> categories
) implements EntityRelated {

    @DeserializationMethod(serialFieldNames = { "side", "categories" })
    public EntityCanEnterFrom {}

    @Override
    public boolean entityCond(Entity e, PrimitiveTile to) {
        return to.canEnter(e);
    }

}
