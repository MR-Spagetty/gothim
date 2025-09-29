package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.util.Point;

/**
 * Basic tile that is effectively empty pace.
 *
 * @author MR-Spagetty
 */
public class Passable extends PrimitiveTile {

    @DeserializationMethod(serialFieldNames = { "pos", "style" })
    public Passable(Point pos, String style) {
        super(pos, style);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass()) && ((Passable) obj).pos.equals(this.pos)
                && ((Passable) obj).style.equals(this.style);
    }

}
