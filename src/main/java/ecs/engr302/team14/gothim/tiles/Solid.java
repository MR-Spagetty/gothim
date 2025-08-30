package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.util.Point;

/**
 * Basic tile that can never be entered.
 *
 * @author MR-Spagetty
 */
public class Solid extends PrimitiveTile {

    @DeserializationMethod(serialFieldNames = { "pos", "style" })
    public Solid(Point pos, String style) {
        super(pos, style);
    }

    @Override
    public boolean canEnter() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass()) && ((Solid) obj).pos.equals(this.pos)
                && ((Solid) obj).style.equals(this.style);
    }
}
