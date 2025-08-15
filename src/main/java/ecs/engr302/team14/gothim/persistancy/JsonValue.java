package ecs.engr302.team14.gothim.persistancy;

/**
 * a basic interface to lump all JSON value types (string, number, boolean,
 * null) together for ease of differentiation.
 *
 * @param <T> the equivalent type in java of the implementing JSON type
 * @author MR-Spagetty
 */
public interface JsonValue<T> extends JsonObject {
    T value();
}
