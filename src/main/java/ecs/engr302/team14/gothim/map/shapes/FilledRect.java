package ecs.engr302.team14.gothim.map.shapes;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializationExtends;
import ecs.engr302.team14.gothim.util.Point;
import java.util.Map;

/**
 * {@link Shape} representing a filled rectangle.
 *
 * @author MR-Spagetty
 */
@SerializationExtends(Rectangle.class)
public class FilledRect extends Rectangle {

    /**
     * Creates a new filled rectangle shape with the given corners.
     *
     * @param type the type of tile to use
     * @param properties the properties of the tile(s)
     * @param topLeft the top-left corner of the rectangle
     * @param bottomRight the bottom-right corner of the rectangle
     */
    @DeserializationMethod(serialFieldNames = { "type", "properties", "topLeft",
            "bottomRight" })
    public FilledRect(String type, Map<String, Object> properties,
            Point topLeft, Point bottomRight) {
        super(type, properties, topLeft, bottomRight);
    }

    @Override
    public boolean placeTile(Point at) {
        return at.x() >= topLeft.x() && at.x() <= bottomRight.x()
                && at.y() >= bottomRight.y() && at.y() <= topLeft.y();
    }
}
