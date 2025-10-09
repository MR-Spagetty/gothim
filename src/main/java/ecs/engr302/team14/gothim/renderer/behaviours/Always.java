package ecs.engr302.team14.gothim.renderer.behaviours;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;
import java.util.Map;

/**
 * Basic Always active behaviour. Typical to be used for base case.
 *
 * @author MR-Spagetty
 */
public record Always(
        @SerializedField
        String assetName
) implements Behaviour {

    @DeserializationMethod(serialFieldNames = { "assetName" })
    public Always {
    }

    @Override
    public Boolean applies(PrimitiveTile to, Map<Point, PrimitiveTile> neighbours) {
        return true;
    }

}
