package ecs.engr302.team14.gothim.persistancy;

/**
 * Enum for storing boolean values in JSON.
 *
 * @author MR-Spagetty
 */
public enum JsonBool implements JsonValue<Boolean> {
    TRUE, FALSE;

    @Override
    public Boolean value() {
        return this == TRUE;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
