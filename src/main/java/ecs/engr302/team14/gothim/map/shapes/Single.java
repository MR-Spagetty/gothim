package ecs.engr302.team14.gothim.map.shapes;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializationExtends;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;
import java.util.Map;

/**
 * {@link Shape} representing a single tile.
 *
 * @author MR-Spagetty
 */
@SerializationExtends(Shape.class)
public class Single extends Shape {

    @SerializedField
    public final Point pos;

    /**
     * Creates a new single-tile shape at the given position.
     *
     * @param type the type of tile to use
     * @param properties the properties of the tile
     * @param pos the position of the tile
     */
    @DeserializationMethod(serialFieldNames = { "type", "properties", "pos" })
    public Single(String type, Map<String, Object> properties, Point pos) {
        super(type, properties);
        this.pos = pos;
    }

    @Override
    protected Map<Point, PrimitiveTile> build() {
        return Map.of(this.pos, createTile(type, this.pos, properties));
    }

    @Override
    public boolean placeTile(Point at) {
        return this.pos.equals(at);
    }

}
