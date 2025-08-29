package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for parsing JSONObject objects from JSON strings.
 *
 * @author MR-Spagetty
 */
public class JSONObjectParsingTests {
    @Test
    void validEmpty() {
        var exp = new JSONObject();
        var ret = JSONObject.parse("{}");
        assertEquals(2, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validSingleKeyValue() {
        var exp = new JSONObject();
        exp.put("a", new JSONNum(1.0));
        var ret = JSONObject.parse("{\"a\": 1}");
        assertEquals(8, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validMultipleKeyValues() {
        var exp = new JSONObject();
        exp.put("a", new JSONNum(1.0));
        exp.put("b", new JSONNum(2.0));
        exp.put("c", new JSONNum(3.0));
        var ret = JSONObject.parse("{\"a\": 1, \"b\": 2, \"c\": 3}");
        assertEquals(24, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validNestedObjects() {
        var exp = new JSONObject();
        var inner = new JSONObject();
        inner.put("b", new JSONNum(2.0));
        exp.put("a", inner);
        var ret = JSONObject.parse("{\"a\": {\"b\": 2}}");
        assertEquals(15, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validArrayValue() {
        var exp = new JSONObject();
        var arr = new JSONArray();
        arr.add(new JSONNum(1.0));
        arr.add(new JSONNum(2.0));
        exp.put("nums", arr);
        var ret = JSONObject.parse("{\"nums\": [1, 2]}");
        assertEquals(16, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validBoolValues() {
        var exp = new JSONObject();
        exp.put("t", JSONBool.TRUE);
        exp.put("f", JSONBool.FALSE);
        var ret = JSONObject.parse("{\"t\": true, \"f\": false}");
        assertEquals(23, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validStringValues() {
        var exp = new JSONObject();
        exp.put("hello", new JSONString("world"));
        exp.put("foo", new JSONString("bar"));
        var ret = JSONObject.parse("{\"hello\": \"world\", \"foo\": \"bar\"}");
        assertEquals(32, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validNullValue() {
        var exp = new JSONObject();
        exp.put("n", JSONValue.NULL);
        var ret = JSONObject.parse("{\"n\": null}");
        assertEquals(11, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validMixedTypes() {
        var exp = new JSONObject();
        exp.put("num", new JSONNum(1.0));
        exp.put("bool", JSONBool.TRUE);
        exp.put("str", new JSONString("hello"));
        exp.put("arr", new JSONArray());
        var ret = JSONObject.parse("{\"num\": 1, \"bool\": true, \"str\": \"hello\", \"arr\": []}");
        assertEquals(51, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validSuperNested() {
        var inner1 = new JSONArray();
        inner1.add(new JSONNum(1.0));
        var inner2 = new JSONArray();
        inner2.add(JSONBool.FALSE);
        var inner3 = new JSONArray();
        inner3.add(new JSONString("hello"));
        var inner4 = new JSONArray();
        inner4.add(JSONValue.NULL);
        var innerObj = new JSONObject();
        innerObj.put("key", new JSONNum(5.0));
        inner4.add(innerObj);
        inner3.add(inner4);
        inner2.add(inner3);
        inner1.add(inner2);
        var exp = new JSONObject();
        exp.put("a", inner1);
        var ret = JSONObject.parse("{\"a\": [1, [false, [\"hello\", [null, {\"key\": 5}]]]]}");
        assertEquals(50, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void invalidMissingEnd() {
        assertThrows(IllegalArgumentException.class, () -> JSONObject.parse("{\"a\": 1"));
    }

    @Test
    void invalidMissingComma() {
        assertThrows(IllegalArgumentException.class, () -> JSONObject.parse("{\"a\": 1 \"b\": 2}"));
    }

    @Test
    void invalidExtraComma() {
        assertThrows(IllegalArgumentException.class, () -> JSONObject.parse("{\"a\": 1, }"));
    }

    @Test
    void invalidNoStart() {
        assertThrows(IllegalArgumentException.class, () -> JSONObject.parse("\"a\": 1}"));
    }
}
