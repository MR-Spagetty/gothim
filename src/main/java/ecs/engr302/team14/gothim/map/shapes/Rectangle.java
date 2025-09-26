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
    final Point topLeft;
    @SerializedField
    final Point bottomRight;

    @DeserializationMethod(serialFieldNames = { "type", "properties", "topLeft",
            "bottomRight" })
    Rectangle(String type, HashMap<String, Object> properties, Point topLeft, Point bottomRight) {
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
    protected boolean placeTile(Point at) {
        return at.x() == topLeft.x() || at.x() == bottomRight.x()
                || at.y() == topLeft.y() || at.y() == bottomRight.y();
    }
}
