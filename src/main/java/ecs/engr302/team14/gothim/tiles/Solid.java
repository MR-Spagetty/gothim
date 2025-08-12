package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.util.Point;

/**
 * Basic tile that can never be entered.
 *
 * @author MR-Spagetty
 */
public class Solid extends PrimitiveTile {

    public Solid(Point pos, String style) {
        super(pos, style);
    }

    @Override
    public boolean canEnter() {
        return false;
    }
}
