package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.util.Point;

/**
 * The most basic tile there can be, only has a position and a style.
 *
 * @author MR-Spagetty
 */
public abstract class PrimitiveTile {
    protected final Point pos;
    public final String style;

    /**
     * creates a new Tile with the given position and style.
     *
     * @param pos   the position of the tile
     * @param style the style of the tile
     */
    public PrimitiveTile(Point pos, String style) {
        this.pos = pos;
        this.style = style;
    }

}