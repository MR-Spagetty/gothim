package ecs.engr302.team14.gothim.persistancy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests to check that the JSONstrings correctly escape things to be compliant with
 * json.
 *
 * @author MR-Spagetty
 */
public class JSONStringComplianceTests {
    @Test
    void newLines() {
        String val = """
                the quick brown
                fox jumps
                over the lazy dogs""";
        String exp = "\"the quick brown\\nfox jumps\\nover the lazy dogs\"";
        JSONString str = new JSONString(val);
        assertEquals(val, str.value());
        assertEquals(exp, str.toString());
    }

    @Test
    void otherEscapes() {
        assertEquals("\"\\\"\"", new JSONString("\"").toString());
        assertEquals("\"\\\\\"", new JSONString("\\").toString());
        assertEquals("\"\\b\"", new JSONString("\b").toString());
        assertEquals("\"\\f\"", new JSONString("\f").toString());
        assertEquals("\"\\r\"", new JSONString("\r").toString());
        assertEquals("\"\\t\"", new JSONString("\t").toString());
    }
}
