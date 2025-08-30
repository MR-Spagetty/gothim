package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;
import java.util.Objects;

/**
 * basic tile that teleports the entity that enters it to a point relative to
 * this location.
 *
 * @author MR-Spagetty
 */
public class BasicRelativeTeleportTile extends PrimitiveEffectTile {

    @SerializedField(deserialParamName = "destinationOffset")
    protected final Point destOffset;

    @DeserializationMethod(serialFieldNames = { "pos", "style", "destinationOffset" })
    public BasicRelativeTeleportTile(Point pos, String style, Point destinationOffset) {
        super(pos, style);
        this.destOffset = Objects.requireNonNull(destinationOffset);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass())
                && ((BasicRelativeTeleportTile) obj).pos.equals(this.pos)
                && ((BasicRelativeTeleportTile) obj).style.equals(this.style)
                && ((BasicRelativeTeleportTile) obj).destOffset.equals(this.destOffset);
    }

    @Override
    public boolean canEnter() {
        return true;
    }

    // TODO the teleportation

}
