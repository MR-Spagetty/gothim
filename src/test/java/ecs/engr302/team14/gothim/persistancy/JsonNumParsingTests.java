package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests fr parsing JsonNum objects from JSON strings.
 *
 * @author MR-Spagetty
 */
public class JsonNumParsingTests {
    @Test
    void valid1() {
        var exp = new JsonNum(123.0);
        var ret = JsonNum.parse("123.0");
        assertEquals(exp.toString().length(), ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void valid2() {
        var exp = new JsonNum(-123.0);
        var ret = JsonNum.parse("-123.0");
        assertEquals(exp.toString().length(), ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void valid3() {
        var exp = new JsonNum(1.23e10);
        var ret = JsonNum.parse("1.23e10");
        assertEquals(exp.toString().length(), ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void valid4() {
        var exp = new JsonNum(-1.23e-10);
        var ret = JsonNum.parse("-1.23e-10");
        assertEquals(exp.toString().length(), ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void valid5() {
        var exp = new JsonNum(0.0);
        var ret = JsonNum.parse("0");
        assertEquals(1, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void invalid1() {
        assertThrows(IllegalArgumentException.class, () -> JsonNum.parse(""));
    }

    @Test
    void invalid2() {
        assertThrows(IllegalArgumentException.class, () -> JsonNum.parse("Hello World!"));
    }

    @Test
    void invalid3() {
        assertThrows(IllegalArgumentException.class, () -> JsonNum.parse("1.2.3"));
    }

    @Test
    void invalid4() {
        assertThrows(IllegalArgumentException.class, () -> JsonNum.parse("-0"));
    }
}
