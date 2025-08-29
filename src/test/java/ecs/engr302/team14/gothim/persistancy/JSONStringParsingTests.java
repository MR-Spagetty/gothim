package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Tests for parsing JSONString objects from JSON strings.
 *
 * @author MR-Spagetty
 */
public class JSONStringParsingTests {
    @Test
    void valid1() {
        var exp = new JSONString("Hello World!");
        var ret = JSONString.parse("\"Hello World!\"");
        assertEquals(exp.toString().length(), ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void valid2() {
        var exp = new JSONString("Hello\nWorld!");
        var ret = JSONString.parse("\"Hello\\nWorld!\"");
        assertEquals(exp.toString().length(), ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void valid3() {
        var exp = new JSONString("");
        var ret = JSONString.parse("\"\"");
        assertEquals(exp.toString().length(), ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validMultiple1() {
        var exp1 = new JSONString("Hello");
        var exp2 = new JSONString("World!");
        String inp = "\"Hello\"   \"World!\"";
        var ret1 = JSONString.parse(inp);
        var ret2 = JSONString.parse(inp.substring(ret1.getValue()));
        assertEquals(exp1.toString().length(), ret1.getValue());
        assertEquals(exp1, ret1.getKey());
        assertEquals(exp2.toString().length() + 3, ret2.getValue());
        assertEquals(exp2, ret2.getKey());
    }

    @Test
    void invalid1() {
        assertThrows(IllegalArgumentException.class, () -> JSONString.parse(""));
    }

    @Test
    void invalid2() {
        assertThrows(IllegalArgumentException.class, () -> JSONString.parse("\"Hello World!"));
    }

    @Test
    void invalid3() {
        assertThrows(IllegalArgumentException.class, () -> JSONString.parse("Hello World!\""));
    }

    @Test
    void miscEscapes() {
        List.of("\"", "\\", "\b", "\f", "\r", "\t").forEach(s -> {
            var exp = new JSONString(s);
            var ret = JSONString.parse(exp.toString());
            assertEquals(exp.toString().length(), ret.getValue(), "Failed for %s".formatted(s));
            assertEquals(exp, ret.getKey(), "Failed for %s".formatted(s));
        });
    }
}
