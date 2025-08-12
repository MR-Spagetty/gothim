package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.util.Point;

/**
 * Basic tile that will affect entities as they enter it in some way.
 *
 * @author MR-Spagetty
 */
public abstract class PrimitiveEffectTile extends PrimitiveTile {

    public PrimitiveEffectTile(Point pos, String style) {
        super(pos, style);
    }

    // TODO entity entering effect tiles
    // public abstract void enter();
}
