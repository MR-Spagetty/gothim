package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for parsing JsonArray objects from JSON strings.
 *
 * @author MR-Spagetty
 */
public class JsonArrayParsingTests {
    @Test
    void validEmpty() {
        var exp = new JsonArray();
        var ret = JsonArray.parse("[]");
        assertEquals(2, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void valid2dSingleInner() {
        var exp = new JsonArray();
        var inner = new JsonArray();
        exp.add(inner);
        var ret = JsonArray.parse("[[]]");
        assertEquals(4, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validNums() {
        var exp = new JsonArray();
        exp.add(new JsonNum(1.0));
        exp.add(new JsonNum(2.0));
        exp.add(new JsonNum(3.0));
        var ret = JsonArray.parse("[1, 2, 3]");
        assertEquals(9, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validBools() {
        var exp = new JsonArray();
        exp.add(JsonBool.TRUE);
        exp.add(JsonBool.FALSE);
        exp.add(JsonBool.TRUE);
        var ret = JsonArray.parse("[true, false, true]");
        assertEquals(19, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validStrings() {
        var exp = new JsonArray();
        exp.add(new JsonString("hello"));
        exp.add(new JsonString("world"));
        var ret = JsonArray.parse("[\"hello\", \"world\"]");
        assertEquals(18, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validNulls() {
        var exp = new JsonArray();
        exp.add(JsonValue.NULL);
        exp.add(JsonValue.NULL);
        var ret = JsonArray.parse("[null, null]");
        assertEquals(12, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validObjects() {
        var exp = new JsonArray();
        var obj1 = new JsonObject();
        obj1.put("a", new JsonNum(1.0));
        var obj2 = new JsonObject();
        obj2.put("b", new JsonNum(2.0));
        exp.add(obj1);
        exp.add(obj2);
        var ret = JsonArray.parse("[{\"a\": 1}, {\"b\": 2}]");
        assertEquals(20, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validMixedNoObjects() {
        var exp = new JsonArray();
        exp.add(new JsonNum(1.0));
        exp.add(JsonBool.FALSE);
        exp.add(new JsonString("hello"));
        var inner = new JsonArray();
        inner.add(JsonValue.NULL);
        exp.add(inner);
        var ret = JsonArray.parse("[1, false, \"hello\", [null]]");
        assertEquals(27, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validMixedNested() {
        var exp = new JsonArray();
        exp.add(new JsonNum(1.0));
        var inner1 = new JsonArray();
        inner1.add(JsonBool.FALSE);
        var inner2 = new JsonArray();
        inner2.add(new JsonString("hello"));
        var inner3 = new JsonArray();
        inner3.add(JsonValue.NULL);
        var inner4 = new JsonObject();
        inner3.add(inner4);
        inner4.put("key", new JsonNum(5.0));
        inner2.add(inner3);
        inner1.add(inner2);
        exp.add(inner1);
        var ret = JsonArray.parse("[1, [false, [\"hello\", [null, {\"key\": 5}]]]]");
        assertEquals(43, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void invalidMissingEnd() {
        assertThrows(IllegalArgumentException.class, () -> JsonArray.parse("[1, 2"));
    }

    @Test
    void invalidMissingComma() {
        assertThrows(IllegalArgumentException.class, () -> JsonArray.parse("[1 2]"));
    }

    @Test
    void invalidExtraComma() {
        assertThrows(IllegalArgumentException.class, () -> JsonArray.parse("[1, 2, ]"));
    }

    @Test
    void invalidNoStart() {
        assertThrows(IllegalArgumentException.class, () -> JsonArray.parse("1, 2]"));
    }
}
