package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.app.LevelManager;
import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.logic.LevelHolder;
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

    @SerializedField
    protected final Point pos;
    @SerializedField
    public final String style;
    private Optional<Entity> occupant = Optional.empty();

    /**
     * creates a new Tile with the given position and style.
     *
     * @param pos the position of the tile
     * @param style the style of the tile
     */
    public PrimitiveTile(Point pos, String style) {
        this.pos = pos;
        this.style = style;
    }

    public boolean canEnter(Entity e) {
        return occupant.isEmpty();
    }

    public Optional<Entity> getOccupant() {
        return occupant;
    }

    public void setOccupant(Entity occupant) {
        this.occupant = Optional.ofNullable(occupant);
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
        if (mapRef().getTile(e.getPosition()).getOccupant().equals(Optional.of(e))) {
            mapRef().getTile(e.getPosition()).occupant = Optional.empty();
        }
        this.occupant = Optional.of(e);
        e.setPosition(pos);
    }

    public Point pos() {
        return this.pos;
    }

    static Board mapRef() {
        return LevelManager.getLevelData().map();
    }
}