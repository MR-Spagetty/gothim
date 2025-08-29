package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.util.Point;

/**
 * Basic tile that is effectively empty pace.
 *
 * @author MR-Spagetty
 */
public class Passable extends PrimitiveTile {

    @DeserializationMethod
    public Passable(Point pos, String style) {
        super(pos, style);
    }

    @Override
    public boolean canEnter() {
        return true;
    }

}
