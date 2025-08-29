package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import ecs.engr302.team14.gothim.util.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * tests Serialization.toJSON.
 *
 * @author MR-Spagetty
 */
public class ToJSONTests {
    static final Random rand = new Random();

    @RepeatedTest(50)
    void pointToJSON0() {
        double x = rand.nextDouble();
        double y = rand.nextDouble();
        Point p = new Point(x, y);
        assertEquals("""
                {
                    "class": "ecs.engr302.team14.gothim.util.Point",
                    "fields": {
                        "x": %s,
                        "y": %s
                    }
                }""".formatted("" + x, "" + y),
                ((JSONObject) Serialization.toJSON(p)).prettyPrint());
    }

    @Test
    void stringToJSON() {
        String exp = "Hello World!!";
        Object out = Serialization.toJSON(exp);
        assertInstanceOf(JSONString.class, out);
        assertEquals(exp, ((JSONString) out).value());
    }

    @RepeatedTest(50)
    void doubleToJSON() {
        double exp = rand.nextDouble();
        Object out = Serialization.toJSON(exp);
        assertInstanceOf(JSONNum.class, out);
        assertEquals(exp, ((JSONNum) out).value());
    }

    @RepeatedTest(50)
    void longToJSON() {
        long exp = rand.nextLong();
        Object out = Serialization.toJSON(exp);
        assertInstanceOf(JSONNum.class, out);
        assertEquals(exp, ((JSONNum) out).value());
    }

    @RepeatedTest(50)
    void intToJSON() {
        int exp = rand.nextInt();
        Object out = Serialization.toJSON(exp);
        assertInstanceOf(JSONNum.class, out);
        assertEquals(exp, ((JSONNum) out).value());
    }

    @RepeatedTest(50)
    void floatToJSON() {
        float exp = rand.nextFloat();
        Object out = Serialization.toJSON(exp);
        assertInstanceOf(JSONNum.class, out);
        assertEquals(exp, ((JSONNum) out).value());
    }

    @Test
    void booleanToJSON() {
        assertSame(JSONBool.TRUE, Serialization.toJSON(true));
        assertSame(JSONBool.FALSE, Serialization.toJSON(false));
    }

    @Test
    void arrayListToJSON() {
        var list = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Object out = Serialization.toJSON(list);
        assertInstanceOf(JSONArray.class, out);
        assertEquals("""
                [
                    1.0,
                    2.0,
                    3.0,
                    4.0,
                    5.0
                ]""", ((JSONArray) out).prettyPrint());
    }

    @Test
    void hashSetToJSON() {
        var set = new HashSet<>(Set.of(1, 2, 3, 4, 5));
        Object out = Serialization.toJSON(set);
        assertInstanceOf(JSONObject.class, out);
        assertEquals("""
                {
                    "kind": "collection",
                    "class": "java.util.HashSet",
                    "values": [
                        1.0,
                        2.0,
                        3.0,
                        4.0,
                        5.0
                    ]
                }""", ((JSONObject) out).prettyPrint());
    }

    @Test
    void hashMapToJSON() {
        var map = new HashMap<>(Map.of("a", 2, "b", 3, "c", 4));
        Object out = Serialization.toJSON(map);
        assertInstanceOf(JSONObject.class, out);
        assertEquals("""
                {
                    "kind": "map",
                    "class": "java.util.HashMap",
                    "valueType": "int",
                    "entries": {
                        "a": 2.0,
                        "b": 3.0,
                        "c": 4.0
                    }
                }""", ((JSONObject) out).prettyPrint());
    }

    @Test
    void list2dToJSON() {
        var list = new LinkedList<>(
                List.of(new ArrayList<>(List.of(1, 2, 3)), new LinkedList<>(List.of(4, 5, 6))));
        Object out = Serialization.toJSON(list);
        assertInstanceOf(JSONObject.class, out);
        assertEquals("""
                {
                    "kind": "collection",
                    "class": "java.util.LinkedList",
                    "values": [
                        [
                            1.0,
                            2.0,
                            3.0
                        ],
                        {
                            "kind": "collection",
                            "class": "java.util.LinkedList",
                            "values": [
                                4.0,
                                5.0,
                                6.0
                            ]
                        }
                    ]
                }""", ((JSONObject) out).prettyPrint());
    }
}
