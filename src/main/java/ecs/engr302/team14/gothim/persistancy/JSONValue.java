package ecs.engr302.team14.gothim.persistancy;

import java.util.Map;

/**
 * a basic interface to lump all JSON value types (string, number, boolean,
 * null) together for ease of differentiation.
 *
 * @param <T> the equivalent type in java of the implementing JSON type
 * @author MR-Spagetty
 */
public interface JSONValue<T> extends JSONType {
    static final JSONValue<Object> NULL = new JSONValue<Object>() {
        @Override
        public Object value() {
            return null;
        }

        @Override
        public String toString() {
            return "" + value();
        }
    };

    /**
     * Parses a JSONNull from the start of the given JSON string.
     *
     * @param jsonData the JSON string to parse from
     * @return the parsed JSONNull and the position of the next token in the
     *      given string
     * @throws IllegalArgumentException if the first token in the string is not
     *      a JSONNull
     */
    public static Map.Entry<JSONValue<Object>, Integer> parseNull(String jsonData) {
        if (jsonData.stripLeading().startsWith("null")) {
            return Map.entry(NULL, jsonData.indexOf("null") + 4);
        } else {
            throw new IllegalArgumentException(
                    "First JSON token in: %s \n is not a valid JSONNull".formatted(jsonData));
        }
    }

    /**
     * Checks if the next token in the given JSON string is a JSONNull.
     *
     * @param jsonData the JSON string to check
     * @return whether the next token is a JSONNull
     */
    public static boolean isNextNull(String jsonData) {
        try {
            parseNull(jsonData);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    T value();
}
