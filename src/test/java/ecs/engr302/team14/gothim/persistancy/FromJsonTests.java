package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ecs.engr302.team14.gothim.util.Point;

public class FromJsonTests {
    @Test
    void basicArrayList() {
        var exp = new ArrayList<Double>(List.of(1.0, 2.0, 3.0, 4.0, 5.0));
        var res = Serialization.fromJson(JsonArray.parse("""
                [
                    1,
                    2,
                    3,
                    4,
                    5
                ]
                """).getKey());

        assertEquals(exp, res);
    }

    @Test
    void nestedArrayList() {
        var exp = new ArrayList<List<Double>>(List.of(new ArrayList<>(List.of(1.0, 2.0, 3.0)),
                new ArrayList<>(List.of(4.0, 5.0))));
        var res = Serialization.fromJson(JsonArray.parse("""
                [
                    [
                        1,
                        2,
                        3
                    ],
                    [
                        4,
                        5
                    ]
                ]
                """).getKey());
        assertEquals(exp, res);
    }

    @Test
    void stringKeyMap() {
        var exp = new HashMap<String, Double>(Map.of("a", 1.0, "b", 2.0, "c", 3.0));
        var res = Serialization.fromJson(JsonObject.parse("""
                {
                    "kind": "map",
                    "class": "java.util.HashMap",
                    "valueType": "double",
                    "entries": {
                        "a": 1,
                        "b": 2,
                        "c": 3
                    }
                }
                """).getKey());
        assertEquals(exp, res);
    }

    @Test
    void intKeyMap() {
        var exp = new HashMap<Integer, Double>(Map.of(1, 1.0, 2, 2.0, 3, 3.0));
        var res = Serialization.fromJson(JsonObject.parse("""
                {
                    "kind": "map",
                    "class": "java.util.HashMap",
                    "keyType": "int",
                    "valueType": "double",
                    "entries": [
                        {"key":1, "value":1},
                        {"key":2, "value":2},
                        {"key":3, "value":3}
                    ]
                }
                """).getKey());
        assertEquals(exp, res);
    }

    @Test
    void complexMap() {
        var exp = new HashMap<List<Double>, Map<String, Double>>(Map.of(List.of(1.0, 2., 3.),
                Map.of("a", 1.0, "b", 2.0), List.of(4., 5.), Map.of("c", 3.0)));
        var res = Serialization.fromJson(JsonObject.parse("""
                {
                    "kind": "map",
                    "class": "java.util.HashMap",
                    "keyType": "java.util.List",
                    "valueType": "java.util.Map",
                    "entries": [
                        {
                            "key": [1, 2, 3],
                            "value": {
                                "kind": "map",
                                "class": "java.util.HashMap",
                                "valueType": "double",
                                "entries": {
                                    "a": 1,
                                    "b": 2
                                }
                            }
                        },
                        {
                            "key": [4, 5],
                            "value": {
                                "kind": "map",
                                "class": "java.util.HashMap",
                                "valueType": "double",
                                "entries": {
                                    "c": 3
                                }
                            }
                        }
                    ]
                }""").getKey());
        assertEquals(exp, res);
    }

    @Test
    void point(){
        var exp = new Point(0, 0);
        var res = Serialization.fromJson(JsonObject.parse("""
                {
                    "class": "ecs.engr302.team14.gothim.util.Point",
                    "fields": {
                        "x": 0,
                        "y": 0
                    }
                }
                """).getKey());
        assertEquals(exp, res);
    }
}
