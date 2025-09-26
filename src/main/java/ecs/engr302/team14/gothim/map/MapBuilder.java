package ecs.engr302.team14.gothim.map;

import ecs.engr302.team14.gothim.map.shapes.FilledRect;
import ecs.engr302.team14.gothim.map.shapes.Rectangle;
import ecs.engr302.team14.gothim.map.shapes.Shape;
import ecs.engr302.team14.gothim.map.shapes.Single;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;
import java.util.ArrayList;
import java.util.Map;

/**
 * Fluent Builder class for creating maps using {@link Shape Shapes}.
 *
 * @author MR-Spagetty
 */
public class MapBuilder {
    @SerializedField
    ArrayList<Shape> shapes;

    @DeserializationMethod(serialFieldNames = { "shapes" })
    public MapBuilder(ArrayList<Shape> shapes) {
        this.shapes = new ArrayList<>(shapes);
    }

    public MapBuilder() {
        this.shapes = new ArrayList<>();
    }

    /**
     * Adds the given shape to the shapes to use to be built.
     *
     * @param s the shape to add
     * @implNote if the shape is a {@link Single} uses
     *           {@link #withTile(String, Map, Point)} internally.
     * @see Shape
     * @return this builder
     */
    public MapBuilder withShape(Shape s) {
        if (s instanceof Single single) {
            return withTile(single.type(), single.properties(), single.pos);
        }
        this.shapes.add(s);
        return this;
    }

    /**
     * Adds a single tile to be built.
     *
     * @param type       the type of the tile
     * @param properties the properties of the tile
     * @param pos        where on the map the tile should be placed
     * @implNote if a tile with the same type and properties will already exist
     *           based on existing shapes the tile will not be added
     * @see Single
     * @return this builder
     */
    public MapBuilder withTile(String type, Map<String, Object> properties, Point pos) {
        if (shapes.parallelStream().filter(t -> t.type().equals(type) && t.placeTile(pos))
                .anyMatch(t -> t.properties().equals(properties))) {
            return this;
        }
        shapes.add(new Single(type, properties, pos));
        return this;
    }

    /**
     * Adds a rectangular outline of the specified tile type and properties to be
     * built in the map.
     *
     * @param type        the type of tile to use
     * @param properties  the properties of the tile(s)
     * @param topLeft     the top-left corner of the rectangle
     * @param bottomRight the bottom-right corner of the rectangle
     * @implNote if {@code topLeft} equals {@code bottomRight} uses
     *           {@link #withTile(String, Map, Point)} internally.
     * @see Rectangle
     * @return this builder
     */
    public MapBuilder withRect(String type, Map<String, Object> properties,
            Point topLeft, Point bottomRight) {
        if (topLeft.equals(bottomRight)) {
            return withTile(type, properties, topLeft);
        }
        shapes.add(new Rectangle(type, properties, topLeft, bottomRight));
        return this;
    }

    /**
     * Adds a filled rectangle of the specified tile type and properties to be built
     * in the map.
     *
     * @param type        the type of tile to use
     * @param properties  the properties of the tile(s)
     * @param topLeft     the top-left corner of the rectangle
     * @param bottomRight the bottom-right corner of the rectangle
     * @implNote if {@code topLeft} equals {@code bottomRight} uses
     *           {@link #withTile(String, Map, Point)} internally.
     * @see FilledRect
     * @return this builder
     */
    public MapBuilder withFilledRect(String type, Map<String, Object> properties,
            Point topLeft, Point bottomRight) {
        if (topLeft.equals(bottomRight)) {
            return withTile(type, properties, topLeft);
        }
        shapes.add(new FilledRect(type, properties, topLeft, bottomRight));
        return this;
    }

    public Board build() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
