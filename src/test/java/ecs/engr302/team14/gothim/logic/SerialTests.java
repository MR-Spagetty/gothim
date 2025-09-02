package ecs.engr302.team14.gothim.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import ecs.engr302.team14.gothim.persistancy.Serialization;
import org.junit.jupiter.api.Test;


/**
 * Tests for symmetrical serialization of all small classes within the logic
 * package.
 *
 * @author MR-Spagetty
 */
public class SerialTests {
    @Test
    void serialAccessModifier() {
        var pub = Serialization.fromJSON(Serialization.toJSON(AccessModifier.Public));
        assertSame(AccessModifier.Public, pub);
        var priv = Serialization.fromJSON(Serialization.toJSON(AccessModifier.Private));
        assertSame(AccessModifier.Private, priv);
        var stat = Serialization.fromJSON(Serialization.toJSON(AccessModifier.Static));
        assertSame(AccessModifier.Static, stat);
    }

    @Test
    void serialClue() {
        var exp = new Clue(AccessModifier.Public, "test", "this is a test Clue");
        var res = Serialization.fromJSON(Serialization.toJSON(exp));
        assertEquals(exp, res);
    }

}
