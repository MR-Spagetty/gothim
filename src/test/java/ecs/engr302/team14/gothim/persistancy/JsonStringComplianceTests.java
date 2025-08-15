package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class JsonStringComplianceTests {
    @Test
    void newLines() {
        String val = """
                the quick brown
                fox jumps
                over the lazy dogs
                """;
        String exp = "\"the quick brown\\nfox jumps\\nover the lazy dogs\"";
        JsonString str = new JsonString(val);
        assertEquals(val, str.value());
        assertEquals(exp, str.toString());
    }

    @Test
    void otherEscapes() {
        String val = "\"\t\\\b\f\r";
        String exp = "\\\"\\t\\\\\\b\\f\\r";
        JsonString str = new JsonString(val);
        assertEquals(val, str.value());
        assertEquals(exp, val.toString());
    }
}
