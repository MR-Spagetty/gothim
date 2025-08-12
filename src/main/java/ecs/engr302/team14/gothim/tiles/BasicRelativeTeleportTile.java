package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.util.Point;
import java.util.Objects;

/**
 * basic tile that teleports the entity that enters it to a point relative to
 * this location.
 *
 * @author MR-Spagetty
 */
public class BasicRelativeTeleportTile extends PrimitiveEffectTile {

    protected final Point destOffset;

    public BasicRelativeTeleportTile(Point pos, String style, Point destinationOffset) {
        super(pos, style);
        this.destOffset = Objects.requireNonNull(destinationOffset);
    }

    @Override
    public boolean canEnter() {
        return true;
    }

    // TODO the teleportation

}
