package ecs.engr302.team14.gothim.map.shapes;

import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.BasicAbsoluteTeleportTile;
import ecs.engr302.team14.gothim.tiles.BasicRelativeTeleportTile;
import ecs.engr302.team14.gothim.tiles.Passable;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.tiles.Solid;
import ecs.engr302.team14.gothim.util.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * simple class for defining shapes to make map creation easier.
 *
 * @author MR-Spagetty
 */
public abstract class Shape {
    @SerializedField
    String type;
    @SerializedField
    HashMap<String, Object> properties;

    Shape(String type, HashMap<String, Object> properties) {
        if (!validate(type, properties)) {
            throw new IllegalArgumentException("Invalid shape type or properties");
        }
        this.type = type;
        this.properties = properties;
    }

    public String type() {
        return type;
    }

    public void build(Map<Point, PrimitiveTile> map) {
        map.putAll(build());
    }

    protected abstract Map<Point, PrimitiveTile> build();

    protected abstract boolean placeTile(Point at);

    static boolean validate(String type, HashMap<String, Object> properties) {

        return switch (type) {
            case "solid", "wall" -> true;
            case "relTeleport" -> properties.containsKey("relativeDestination")
                    && properties.get("relativeDestination") instanceof Point;
            case "teleport" -> properties.containsKey("destination")
                    && properties.get("destination") instanceof Point;
            case "open", "passable", "floor" -> true;

            default -> throw new IllegalArgumentException("Unknown shape type: " + type);
        } && properties.containsKey("style") && properties.get("style") instanceof String;
    }

    static PrimitiveTile createTile(String type, Point pos, HashMap<String, Object> properties) {
        String style = (String) properties.get("style");
        return switch (type) {
            case "solid", "wall" -> new Solid(pos, style);
            case "open", "passable", "floor" -> new Passable(pos, style);
            case "relTeleport" -> new BasicRelativeTeleportTile(
                    pos, style,
                    (Point) properties.get("relativeDestination"));
            case "teleport" -> new BasicAbsoluteTeleportTile(
                    pos, style,
                    (Point) properties.get("destination"));


            default -> throw new IllegalArgumentException("Unknown tile type: " + type);
        };
    }
}
