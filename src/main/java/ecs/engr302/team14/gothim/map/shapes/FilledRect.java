package ecs.engr302.team14.gothim.map.shapes;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.util.Point;
import java.util.HashMap;

/**
 * {@link Shape} representing a filled rectangle.
 *
 * @author MR-Spagetty
 */
public class FilledRect extends Rectangle {

    @DeserializationMethod(serialFieldNames = { "type", "properties", "topLeft",
            "bottomRight" })
    FilledRect(String type, HashMap<String, Object> properties, Point topLeft, Point bottomRight) {
        super(type, properties, topLeft, bottomRight);
    }

    @Override
    protected boolean placeTile(Point at) {
        return at.x() >= topLeft.x() && at.x() <= bottomRight.x()
                && at.y() >= bottomRight.y() && at.y() <= topLeft.y();
    }
}
