package ecs.engr302.team14.gothim.map.shapes;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializationExtends;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * {@link Shape} representing a rectangular outline.
 *
 * @author MR-Spagetty
 */
@SerializationExtends(Shape.class)
public class Rectangle extends Shape {
    @SerializedField
    public final Point topLeft;
    @SerializedField
    public final Point bottomRight;

    /**
     * Creates a new rectangle shape with the given corners.
     *
     * @param type the type of tile to use
     * @param properties the properties of the tile(s)
     * @param topLeft the top-left corner of the rectangle
     * @param bottomRight the bottom-right corner of the rectangle
     */
    @DeserializationMethod(serialFieldNames = { "type", "properties", "topLeft",
            "bottomRight" })
    public Rectangle(String type, Map<String, Object> properties,
            Point topLeft, Point bottomRight) {
        super(type, properties);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        if (topLeft.x() > bottomRight.x() || topLeft.y() < bottomRight.y()) {
            throw new IllegalArgumentException("Invalid rectangle coordinates");
        }
    }

    @Override
    protected Map<Point, PrimitiveTile> build() {
        return IntStream.range((int) topLeft.x(), (int) bottomRight.x() + 1).boxed().parallel()
                .flatMap(x -> IntStream.range((int) bottomRight.y(), (int) topLeft.y() + 1)
                        .parallel()
                        .mapToObj(y -> new Point(x, y)))
                .filter(this::placeTile)
                .collect(HashMap::new,
                        (m, p) -> m.put(p, createTile(type, p, properties)),
                        HashMap::putAll);
    }

    @Override
    public boolean placeTile(Point at) {
        return at.x() == topLeft.x() || at.x() == bottomRight.x()
                || at.y() == topLeft.y() || at.y() == bottomRight.y();
    }
}
