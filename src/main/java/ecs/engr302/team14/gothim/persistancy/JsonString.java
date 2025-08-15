package ecs.engr302.team14.gothim.persistancy;

/**
 * Record for storing Strings in JSON.
 *
 * @param value the string value of this object
 * @author MR-Spagetty
 */
public record JsonString(String value) implements JsonValue<String> {
    @Override
    public final String toString() {
        return "\"%s\"".formatted(value);
    }
}
