package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Tests for parsing JsonString objects from JSON strings.
 *
 * @author MR-Spagetty
 */
public class JsonStringParsingTests {
    @Test
    void valid1() {
        var exp = new JsonString("Hello World!");
        var ret = JsonString.parse("\"Hello World!\"");
        assertEquals(exp.toString().length(), ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void valid2() {
        var exp = new JsonString("Hello\nWorld!");
        var ret = JsonString.parse("\"Hello\\nWorld!\"");
        assertEquals(exp.toString().length(), ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void valid3() {
        var exp = new JsonString("");
        var ret = JsonString.parse("\"\"");
        assertEquals(exp.toString().length(), ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validMultiple1() {
        var exp1 = new JsonString("Hello");
        var exp2 = new JsonString("World!");
        String inp = "\"Hello\"   \"World!\"";
        var ret1 = JsonString.parse(inp);
        var ret2 = JsonString.parse(inp.substring(ret1.getValue()));
        assertEquals(exp1.toString().length(), ret1.getValue());
        assertEquals(exp1, ret1.getKey());
        assertEquals(exp2.toString().length() + 3, ret2.getValue());
        assertEquals(exp2, ret2.getKey());
    }

    @Test
    void invalid1() {
        assertThrows(IllegalArgumentException.class, () -> JsonString.parse(""));
    }

    @Test
    void invalid2() {
        assertThrows(IllegalArgumentException.class, () -> JsonString.parse("\"Hello World!"));
    }

    @Test
    void invalid3() {
        assertThrows(IllegalArgumentException.class, () -> JsonString.parse("Hello World!\""));
    }

    @Test
    void miscEscapes() {
        List.of("\"", "\\", "\b", "\f", "\r", "\t").forEach(s -> {
            var exp = new JsonString(s);
            var ret = JsonString.parse(exp.toString());
            assertEquals(exp.toString().length(), ret.getValue(), "Failed for %s".formatted(s));
            assertEquals(exp, ret.getKey(), "Failed for %s".formatted(s));
        });
    }
}
