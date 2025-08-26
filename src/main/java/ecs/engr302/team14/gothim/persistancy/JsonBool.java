package ecs.engr302.team14.gothim.persistancy;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Enum for storing boolean values in JSON.
 *
 * @author MR-Spagetty
 */
public enum JsonBool implements JsonValue<Boolean> {
    TRUE, FALSE;

    static final String regex = "^\\s*(true|false)";
    static final Pattern pattern = Pattern.compile(regex);

    public static JsonBool of(boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public Boolean value() {
        return this == TRUE;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    /**
     * Parses a JsonBool from the start of the given JSON string.
     *
     * @param jsonData the JSON string to parse from
     * @return the parsed JsonBool and the position of the next token in the
     *      given string
     * @throws IllegalArgumentException if the first token in the string is not
     *      a JsonBool
     */
    public static Map.Entry<JsonBool, Integer> parse(String jsonData) {
        var matcher = pattern.matcher(jsonData);
        if (matcher.find()) {
            return Map.entry(matcher.group(1).equals("true") ? TRUE : FALSE, matcher.end());
        } else {
            throw new IllegalArgumentException(
                    "First JSON token in: %s \n is not a valid JsonBool".formatted(jsonData));
        }
    }

    /**
     * Checks if the next token in the given JSON string is a JsonBool.
     *
     * @param jsonData the JSON string to check
     * @return whether the next token is a JsonBool
     */
    public static boolean isNext(String jsonData) {
        try {
            parse(jsonData);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
