package ecs.engr302.team14.gothim.tiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ecs.engr302.team14.gothim.persistancy.JSONObject;
import ecs.engr302.team14.gothim.persistancy.Serialization;
import ecs.engr302.team14.gothim.util.Point;
import org.junit.jupiter.api.Test;

/**
 * Basic tests to check if the various tiles get correctly serialized and
 * deserialized.
 *
 * @author MR-Spagetty
 */
public class CanSerializeTests {

    @Test
    void passable() {
        var exp = new Passable(new Point(-23, 42), "super duper secret test");
        String serial = Serialization.toJSON(exp).toString();
        try {
            var out = Serialization.fromJSON(JSONObject.parse(serial).getKey());
            assertEquals(exp, out);
        } catch (IllegalArgumentException iae) {
            fail(serial, iae);
        }
    }

    @Test
    void solid() {
        var exp = new Solid(new Point(-23, 42), "super duper secret test");
        String serial = Serialization.toJSON(exp).toString();
        try {
            var out = Serialization.fromJSON(JSONObject.parse(serial).getKey());
            assertEquals(exp, out);
        } catch (IllegalArgumentException iae) {
            fail(serial, iae);
        }
    }

    @Test
    void basicRelTeleport() {
        var exp = new BasicRelativeTeleportTile(new Point(-23, 42), "super duper secret test",
                new Point(0, 23));
        String serial = Serialization.toJSON(exp).toString();
        try {
            var out = Serialization.fromJSON(JSONObject.parse(serial).getKey());
            assertEquals(exp, out);
        } catch (IllegalArgumentException iae) {
            fail(serial, iae);
        }
    }

    @Test
    void basicAbsTeleport() {
        var exp = new BasicAbsoluteTeleportTile(new Point(-23, 42), "super duper secret test",
                new Point(0, 0));
        String serial = Serialization.toJSON(exp).toString();
        try {
            var out = Serialization.fromJSON(JSONObject.parse(serial).getKey());
            assertEquals(exp, out);
        } catch (IllegalArgumentException iae) {
            fail(serial, iae);
        }
    }
}
