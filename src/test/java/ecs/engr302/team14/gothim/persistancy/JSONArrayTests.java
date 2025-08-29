package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

/**
 * Tests for the implimentation of JSONArray.
 *
 * @author MR-Spagetty
 */
public class JSONArrayTests {
    @Test
    void testInitalState() {
        JSONArray arr = new JSONArray();
        assertEquals(0, arr.size());
        assertFalse(arr.get(0).isPresent());
    }

    @Test
    void testAdd() {
        JSONArray arr = new JSONArray();
        IntStream.range(0, 20).mapToDouble(i -> i).mapToObj(JSONNum::new).forEach(arr::add);
        assertEquals(20, arr.size());
    }

    @Test
    void testGet() {
        JSONArray arr = new JSONArray();
        IntStream.range(0, 20).mapToDouble(i -> i).mapToObj(JSONNum::new).forEach(arr::add);
        assertEquals(20, arr.size());
        IntStream.range(0, 20).forEach(i -> {
            Optional<JSONType> ret = arr.get(i);
            assertTrue(ret.isPresent());
            assertEquals(new JSONNum((double) i), ret.get());
        });
        assertFalse(arr.get(-1).isPresent());
        assertFalse(arr.get(20).isPresent());
    }

    @Test
    void testRemove() {
        JSONArray arr = new JSONArray();
        IntStream.range(0, 20).mapToDouble(i -> i).mapToObj(JSONNum::new).forEach(arr::add);
        assertEquals(20, arr.size());
        Optional<JSONType> ret = arr.remove(3);
        assertTrue(ret.isPresent());
        assertEquals(new JSONNum(3.), ret.get());
        assertEquals(new JSONNum(4.), arr.get(3).get());
        assertFalse(arr.get(19).isPresent());
    }
}
