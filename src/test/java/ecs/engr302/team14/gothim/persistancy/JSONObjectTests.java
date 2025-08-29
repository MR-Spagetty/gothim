package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

/**
 * Testing of the implementation JSONObject.
 *
 * @author MR-Spagetty
 */
public class JSONObjectTests {
    @Test
    void testInitialState() {
        JSONObject obj = new JSONObject();
        assertEquals(0, obj.size());
        assertTrue(obj.put("blah", JSONValue.NULL).isEmpty());
    }

    @Test
    void testPut() {
        JSONObject obj = new JSONObject();
        assertEquals(0, obj.size());
        obj.put("blah", JSONBool.of(true));
        assertEquals(1, obj.size());
        Optional<JSONType> val = obj.put("blah", JSONBool.of(false));
        assertTrue(val.isPresent());
        assertEquals(JSONBool.of(true), val.get());
        Optional<JSONType> ret = obj.put("2", JSONValue.NULL);
        assertEquals(2, obj.size());
        assertFalse(ret.isPresent());
    }

    @Test
    void testGet() {
        JSONObject obj = new JSONObject();
        IntStream.range(-20, 20).mapToDouble(i -> i).forEach(i -> obj.put("" + i, new JSONNum(i)));
        IntStream.range(-20, 20).mapToDouble(i -> i).forEach(i -> {
            Optional<JSONType> ret = obj.get("" + i);
            assertTrue(ret.isPresent());
            assertEquals(new JSONNum(i), ret.get());
        });
        assertFalse(obj.get("other").isPresent());
    }

    @Test
    void testRemove() {
        JSONObject obj = new JSONObject();
        IntStream.range(-20, 20).mapToDouble(i -> i).forEach(i -> obj.put("" + i, new JSONNum(i)));
        String key = "" + 3.;
        JSONNum exp = new JSONNum(3.);
        assertEquals(40, obj.size());
        assertTrue(obj.get(key).isPresent());
        assertEquals(exp, obj.remove(key).get());
        assertEquals(39, obj.size());
        assertFalse(obj.get(key).isPresent());
    }
}
