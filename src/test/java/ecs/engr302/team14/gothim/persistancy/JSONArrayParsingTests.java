package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for parsing JSONArray objects from JSON strings.
 *
 * @author MR-Spagetty
 */
public class JSONArrayParsingTests {
    @Test
    void validEmpty() {
        var exp = new JSONArray();
        var ret = JSONArray.parse("[]");
        assertEquals(2, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void valid2dSingleInner() {
        var exp = new JSONArray();
        var inner = new JSONArray();
        exp.add(inner);
        var ret = JSONArray.parse("[[]]");
        assertEquals(4, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validNums() {
        var exp = new JSONArray();
        exp.add(new JSONNum(1.0));
        exp.add(new JSONNum(2.0));
        exp.add(new JSONNum(3.0));
        var ret = JSONArray.parse("[1, 2, 3]");
        assertEquals(9, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validBools() {
        var exp = new JSONArray();
        exp.add(JSONBool.TRUE);
        exp.add(JSONBool.FALSE);
        exp.add(JSONBool.TRUE);
        var ret = JSONArray.parse("[true, false, true]");
        assertEquals(19, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validStrings() {
        var exp = new JSONArray();
        exp.add(new JSONString("hello"));
        exp.add(new JSONString("world"));
        var ret = JSONArray.parse("[\"hello\", \"world\"]");
        assertEquals(18, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validNulls() {
        var exp = new JSONArray();
        exp.add(JSONValue.NULL);
        exp.add(JSONValue.NULL);
        var ret = JSONArray.parse("[null, null]");
        assertEquals(12, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validObjects() {
        var exp = new JSONArray();
        var obj1 = new JSONObject();
        obj1.put("a", new JSONNum(1.0));
        var obj2 = new JSONObject();
        obj2.put("b", new JSONNum(2.0));
        exp.add(obj1);
        exp.add(obj2);
        var ret = JSONArray.parse("[{\"a\": 1}, {\"b\": 2}]");
        assertEquals(20, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validMixedNoObjects() {
        var exp = new JSONArray();
        exp.add(new JSONNum(1.0));
        exp.add(JSONBool.FALSE);
        exp.add(new JSONString("hello"));
        var inner = new JSONArray();
        inner.add(JSONValue.NULL);
        exp.add(inner);
        var ret = JSONArray.parse("[1, false, \"hello\", [null]]");
        assertEquals(27, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void validMixedNested() {
        var exp = new JSONArray();
        exp.add(new JSONNum(1.0));
        var inner1 = new JSONArray();
        inner1.add(JSONBool.FALSE);
        var inner2 = new JSONArray();
        inner2.add(new JSONString("hello"));
        var inner3 = new JSONArray();
        inner3.add(JSONValue.NULL);
        var inner4 = new JSONObject();
        inner3.add(inner4);
        inner4.put("key", new JSONNum(5.0));
        inner2.add(inner3);
        inner1.add(inner2);
        exp.add(inner1);
        var ret = JSONArray.parse("[1, [false, [\"hello\", [null, {\"key\": 5}]]]]");
        assertEquals(43, ret.getValue());
        assertEquals(exp, ret.getKey());
    }

    @Test
    void invalidMissingEnd() {
        assertThrows(IllegalArgumentException.class, () -> JSONArray.parse("[1, 2"));
    }

    @Test
    void invalidMissingComma() {
        assertThrows(IllegalArgumentException.class, () -> JSONArray.parse("[1 2]"));
    }

    @Test
    void invalidExtraComma() {
        assertThrows(IllegalArgumentException.class, () -> JSONArray.parse("[1, 2, ]"));
    }

    @Test
    void invalidNoStart() {
        assertThrows(IllegalArgumentException.class, () -> JSONArray.parse("1, 2]"));
    }
}
