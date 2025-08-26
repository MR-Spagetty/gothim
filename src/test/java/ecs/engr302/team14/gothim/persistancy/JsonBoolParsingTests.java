package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for parsing JsonBool objects from JSON strings.
 *
 * @author MR-Spagetty
 */
public class JsonBoolParsingTests {
    @Test
    void validTrue() {
        var exp = JsonBool.TRUE;
        var ret = JsonBool.parse("true");
        assertEquals(4, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void invalidTrue() {
        assertThrows(IllegalArgumentException.class, () -> JsonBool.parse("True"));
    }

    @Test
    void validFalse() {
        var exp = JsonBool.FALSE;
        var ret = JsonBool.parse("false");
        assertEquals(5, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void invalidFalse() {
        assertThrows(IllegalArgumentException.class, () -> JsonBool.parse("False"));
    }

    @Test
    void invalidGeneric() {
        assertThrows(IllegalArgumentException.class, () -> JsonBool.parse("hello world"));
        assertThrows(IllegalArgumentException.class, () -> JsonBool.parse(""));
        assertThrows(IllegalArgumentException.class, () -> JsonBool.parse("123"));
    }
}
