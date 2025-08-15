package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

/**
 * Tests for the implimentation of JsonArray.
 *
 * @author MR-Spagetty
 */
public class JsonArrayTests {
    @Test
    void testInitalState() {
        JsonArray arr = new JsonArray();
        assertEquals(0, arr.size());
        assertFalse(arr.get(0).isPresent());
    }

    @Test
    void testAdd() {
        JsonArray arr = new JsonArray();
        IntStream.range(0, 20).mapToDouble(i -> i).mapToObj(JsonNum::new).forEach(arr::add);
        assertEquals(20, arr.size());
    }

    @Test
    void testGet() {
        JsonArray arr = new JsonArray();
        IntStream.range(0, 20).mapToDouble(i -> i).mapToObj(JsonNum::new).forEach(arr::add);
        assertEquals(20, arr.size());
        IntStream.range(0, 20).forEach(i -> {
            Optional<JsonType> ret = arr.get(i);
            assertTrue(ret.isPresent());
            assertEquals(new JsonNum((double) i), ret.get());
        });
        assertFalse(arr.get(-1).isPresent());
        assertFalse(arr.get(20).isPresent());
    }

    @Test
    void testRemove() {
        JsonArray arr = new JsonArray();
        IntStream.range(0, 20).mapToDouble(i -> i).mapToObj(JsonNum::new).forEach(arr::add);
        assertEquals(20, arr.size());
        Optional<JsonType> ret = arr.remove(3);
        assertTrue(ret.isPresent());
        assertEquals(new JsonNum(3.), ret.get());
        assertEquals(new JsonNum(4.), arr.get(3).get());
        assertFalse(arr.get(19).isPresent());
    }
}
