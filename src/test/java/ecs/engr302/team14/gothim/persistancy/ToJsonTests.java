package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import ecs.engr302.team14.gothim.util.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * tests Serialization.toJson that the output is correct.
 *
 * @author MR-Spagetty
 */
public class ToJsonTests {
    static final Random rand = new Random();

    @RepeatedTest(50)
    void pointToJson0() {
        double x = rand.nextDouble();
        double y = rand.nextDouble();
        Point p = new Point(x, y);
        assertEquals("""
                {
                    "fields": {
                        "x": %s,
                        "y": %s
                    },
                    "class": "ecs.engr302.team14.gothim.util.Point"
                }""".formatted("" + x, "" + y),
                ((JsonObject) Serialization.toJson(p)).prettyPrint());
    }

    @Test
    void stringToJson() {
        String exp = "Hello World!!";
        Object out = Serialization.toJson(exp);
        assertInstanceOf(JsonString.class, out);
        assertEquals(exp, ((JsonString) out).value());
    }

    @RepeatedTest(50)
    void doubleToJson() {
        double exp = rand.nextDouble();
        Object out = Serialization.toJson(exp);
        assertInstanceOf(JsonNum.class, out);
        assertEquals(exp, ((JsonNum) out).value());
    }

    @RepeatedTest(50)
    void longToJson() {
        long exp = rand.nextLong();
        Object out = Serialization.toJson(exp);
        assertInstanceOf(JsonNum.class, out);
        assertEquals(exp, ((JsonNum) out).value());
    }

    @RepeatedTest(50)
    void intToJson() {
        int exp = rand.nextInt();
        Object out = Serialization.toJson(exp);
        assertInstanceOf(JsonNum.class, out);
        assertEquals(exp, ((JsonNum) out).value());
    }

    @RepeatedTest(50)
    void floatToJson() {
        float exp = rand.nextFloat();
        Object out = Serialization.toJson(exp);
        assertInstanceOf(JsonNum.class, out);
        assertEquals(exp, ((JsonNum) out).value());
    }

    @Test
    void booleanToJson() {
        assertSame(JsonBool.TRUE, Serialization.toJson(true));
        assertSame(JsonBool.FALSE, Serialization.toJson(false));
    }

    @Test
    void arrayListToJson() {
        var list = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Object out = Serialization.toJson(list);
        assertInstanceOf(JsonArray.class, out);
        assertEquals("""
                [
                    1.0,
                    2.0,
                    3.0,
                    4.0,
                    5.0
                ]""", ((JsonArray) out).prettyPrint());
    }

    @Test
    void hashSetToJson() {
        var set = new HashSet<>(Set.of(1, 2, 3, 4, 5));
        Object out = Serialization.toJson(set);
        assertInstanceOf(JsonObject.class, out);
        assertEquals("""
                {
                    "type": "java.util.HashSet",
                    "values": [
                        1.0,
                        2.0,
                        3.0,
                        4.0,
                        5.0
                    ]
                }""", ((JsonObject) out).prettyPrint());
    }
}
