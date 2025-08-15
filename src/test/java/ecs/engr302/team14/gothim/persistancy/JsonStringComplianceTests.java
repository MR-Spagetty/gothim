package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests to check that the JSONstrings correctly escape things to be compliant with
 * json.
 *
 * @author MR-Spagetty
 */
public class JsonStringComplianceTests {
    @Test
    void newLines() {
        String val = """
                the quick brown
                fox jumps
                over the lazy dogs""";
        String exp = "\"the quick brown\\nfox jumps\\nover the lazy dogs\"";
        JsonString str = new JsonString(val);
        assertEquals(val, str.value());
        assertEquals(exp, str.toString());
    }

    @Test
    void otherEscapes() {
        assertEquals("\"\\\"\"", new JsonString("\"").toString());
        assertEquals("\"\\\\\"", new JsonString("\\").toString());
        assertEquals("\"\\b\"", new JsonString("\b").toString());
        assertEquals("\"\\f\"", new JsonString("\f").toString());
        assertEquals("\"\\r\"", new JsonString("\r").toString());
        assertEquals("\"\\t\"", new JsonString("\t").toString());
    }
}
