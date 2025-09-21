package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.map.Board;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializationExtends;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;
import java.util.Optional;

/**
 * The most basic tile there can be, only has a position and a style.
 *
 * @author MR-Spagetty
 */
@SerializationExtends(PrimitiveTile.class)
public abstract class PrimitiveTile {

    protected Board mapRef = null;

    /**
     * Link this tile to the map so that the map may be used in the logic.
     *
     * @param map the map to link
     */
    public void linkMap(Board map) {
        if (this.mapRef != null) {
            throw new IllegalStateException("Map reference can only be set once");
        }
        mapRef = map;
    }

    @SerializedField
    protected final Point pos;
    @SerializedField
    public final String style;
    private Optional<Entity> ocupant = Optional.empty();

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

    public boolean canEnter(Entity e) {
        return ocupant.isEmpty();
    }

    public Optional<Entity> getOcupant() {
        return ocupant;
    }

    public void setOcupant(Entity ocupant) {
        this.ocupant = Optional.of(ocupant);
    }

    /**
     * Make the given entity enter this tile.
     *
     * @param e the entity to enter this tile
     * @throws IllegalArgumentException if the entity may not enter this tile
     */
    public void enter(Entity e) {
        if (!canEnter(e)) {
            throw new IllegalArgumentException("Entity: %s may not enter this tile".formatted(e));
        }
        this.ocupant = Optional.of(e);
    }

    public Point pos() {
        return this.pos;
    }
}