package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;
import java.util.Objects;

/**
 * basic tile that teleports the entity that enters it to an absolute point on
 * the map.
 *
 * @author MR-Spagetty
 */
public class BasicAbsoluteTeleportTile extends PrimitiveEffectTile {

    @SerializedField(deserialParamName = "destination")
    protected final Point dest;

    @DeserializationMethod(serialFieldNames = { "pos", "style", "destination" })
    public BasicAbsoluteTeleportTile(Point pos, String style,
            Point destination /* , Region reg?? */) {
        super(pos, style);
        this.dest = Objects.requireNonNull(destination);
    }

    @Override
    public boolean canEnter() {
        return true;
    }

    // TODO the teleportation
}