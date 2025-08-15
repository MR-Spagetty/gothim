package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

/**
 * Testing of the implementation JsonObject.
 *
 * @author MR-Spagetty
 */
public class JsonObjectTests {
    @Test
    void testInitialState() {
        JsonObject obj = new JsonObject();
        assertEquals(0, obj.size());
        assertTrue(obj.put("blah", JsonValue.NULL).isEmpty());
    }

    @Test
    void testPut() {
        JsonObject obj = new JsonObject();
        assertEquals(0, obj.size());
        obj.put("blah", JsonBool.of(true));
        assertEquals(1, obj.size());
        Optional<JsonType> val = obj.put("blah", JsonBool.of(false));
        assertTrue(val.isPresent());
        assertEquals(JsonBool.of(true), val.get());
        Optional<JsonType> ret = obj.put("2", JsonValue.NULL);
        assertEquals(2, obj.size());
        assertFalse(ret.isPresent());
    }

    @Test
    void testGet() {
        JsonObject obj = new JsonObject();
        IntStream.range(-20, 20).mapToDouble(i -> i).forEach(i -> obj.put("" + i, new JsonNum(i)));
        IntStream.range(-20, 20).mapToDouble(i -> i).forEach(i -> {
            Optional<JsonType> ret = obj.get("" + i);
            assertTrue(ret.isPresent());
            assertEquals(new JsonNum(i), ret.get());
        });
        assertFalse(obj.get("other").isPresent());
    }

    @Test
    void testRemove() {
        JsonObject obj = new JsonObject();
        IntStream.range(-20, 20).mapToDouble(i -> i).forEach(i -> obj.put("" + i, new JsonNum(i)));
        String key = "" + 3.;
        JsonNum exp = new JsonNum(3.);
        assertEquals(40, obj.size());
        assertTrue(obj.get(key).isPresent());
        assertEquals(exp, obj.remove(key).get());
        assertEquals(39, obj.size());
        assertFalse(obj.get(key).isPresent());
    }
}
