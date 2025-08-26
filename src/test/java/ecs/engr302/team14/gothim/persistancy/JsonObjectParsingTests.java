package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for parsing JsonObject objects from JSON strings.
 *
 * @author MR-Spagetty
 */
public class JsonObjectParsingTests {
    @Test
    void validEmpty() {
        var exp = new JsonObject();
        var ret = JsonObject.parse("{}");
        assertEquals(2, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validSingleKeyValue() {
        var exp = new JsonObject();
        exp.put("a", new JsonNum(1.0));
        var ret = JsonObject.parse("{\"a\": 1}");
        assertEquals(8, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validMultipleKeyValues() {
        var exp = new JsonObject();
        exp.put("a", new JsonNum(1.0));
        exp.put("b", new JsonNum(2.0));
        exp.put("c", new JsonNum(3.0));
        var ret = JsonObject.parse("{\"a\": 1, \"b\": 2, \"c\": 3}");
        assertEquals(24, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validNestedObjects() {
        var exp = new JsonObject();
        var inner = new JsonObject();
        inner.put("b", new JsonNum(2.0));
        exp.put("a", inner);
        var ret = JsonObject.parse("{\"a\": {\"b\": 2}}");
        assertEquals(15, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validArrayValue() {
        var exp = new JsonObject();
        var arr = new JsonArray();
        arr.add(new JsonNum(1.0));
        arr.add(new JsonNum(2.0));
        exp.put("nums", arr);
        var ret = JsonObject.parse("{\"nums\": [1, 2]}");
        assertEquals(16, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validBoolValues() {
        var exp = new JsonObject();
        exp.put("t", JsonBool.TRUE);
        exp.put("f", JsonBool.FALSE);
        var ret = JsonObject.parse("{\"t\": true, \"f\": false}");
        assertEquals(23, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validStringValues() {
        var exp = new JsonObject();
        exp.put("hello", new JsonString("world"));
        exp.put("foo", new JsonString("bar"));
        var ret = JsonObject.parse("{\"hello\": \"world\", \"foo\": \"bar\"}");
        assertEquals(32, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validNullValue() {
        var exp = new JsonObject();
        exp.put("n", JsonValue.NULL);
        var ret = JsonObject.parse("{\"n\": null}");
        assertEquals(11, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validMixedTypes() {
        var exp = new JsonObject();
        exp.put("num", new JsonNum(1.0));
        exp.put("bool", JsonBool.TRUE);
        exp.put("str", new JsonString("hello"));
        exp.put("arr", new JsonArray());
        var ret = JsonObject.parse("{\"num\": 1, \"bool\": true, \"str\": \"hello\", \"arr\": []}");
        assertEquals(51, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validSuperNested() {
        var inner1 = new JsonArray();
        inner1.add(new JsonNum(1.0));
        var inner2 = new JsonArray();
        inner2.add(JsonBool.FALSE);
        var inner3 = new JsonArray();
        inner3.add(new JsonString("hello"));
        var inner4 = new JsonArray();
        inner4.add(JsonValue.NULL);
        var innerObj = new JsonObject();
        innerObj.put("key", new JsonNum(5.0));
        inner4.add(innerObj);
        inner3.add(inner4);
        inner2.add(inner3);
        inner1.add(inner2);
        var exp = new JsonObject();
        exp.put("a", inner1);
        var ret = JsonObject.parse("{\"a\": [1, [false, [\"hello\", [null, {\"key\": 5}]]]]}");
        assertEquals(50, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void invalidMissingEnd() {
        assertThrows(IllegalArgumentException.class, () -> JsonObject.parse("{\"a\": 1"));
    }

    @Test
    void invalidMissingComma() {
        assertThrows(IllegalArgumentException.class, () -> JsonObject.parse("{\"a\": 1 \"b\": 2}"));
    }

    @Test
    void invalidExtraComma() {
        assertThrows(IllegalArgumentException.class, () -> JsonObject.parse("{\"a\": 1, }"));
    }

    @Test
    void invalidNoStart() {
        assertThrows(IllegalArgumentException.class, () -> JsonObject.parse("\"a\": 1}"));
    }
}
