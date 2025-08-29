package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for parsing JSONBool objects from JSON strings.
 *
 * @author MR-Spagetty
 */
public class JSONBoolParsingTests {
    @Test
    void validTrue() {
        var exp = JSONBool.TRUE;
        var ret = JSONBool.parse("true");
        assertEquals(4, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void invalidTrue() {
        assertThrows(IllegalArgumentException.class, () -> JSONBool.parse("True"));
    }

    @Test
    void validFalse() {
        var exp = JSONBool.FALSE;
        var ret = JSONBool.parse("false");
        assertEquals(5, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void invalidFalse() {
        assertThrows(IllegalArgumentException.class, () -> JSONBool.parse("False"));
    }

    @Test
    void invalidGeneric() {
        assertThrows(IllegalArgumentException.class, () -> JSONBool.parse("hello world"));
        assertThrows(IllegalArgumentException.class, () -> JSONBool.parse(""));
        assertThrows(IllegalArgumentException.class, () -> JSONBool.parse("123"));
    }
}
