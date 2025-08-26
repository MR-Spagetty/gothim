package ecs.engr302.team14.gothim.persistancy;

import static org.apache.commons.text.StringEscapeUtils.escapeJson;
import static org.apache.commons.text.StringEscapeUtils.unescapeJson;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Record for storing Strings in JSON.
 *
 * @param value the string value of this object
 * @author MR-Spagetty
 */
public record JsonString(String value) implements JsonValue<String> {

    static final String regex = "^\\s*\\\"(.*?(?:(?:\\\\\\\\)|[^\\\\]))?\\\"";
    static final Pattern pattern = Pattern.compile(regex);

    @Override
    public final String toString() {
        final String serialValue = "\"%s\"".formatted(escapeJson(value));
        return serialValue;
    }

    /**
     * Parses a JsonString from the start of the given JSON string.
     *
     * @param jsonData the JSON string to parse from
     * @return the parsed JsonString and the position of the next token in the
     *      given string
     * @throws IllegalArgumentException if the first token in the string is not
     *      a JsonString
     */
    public static Map.Entry<JsonString, Integer> parse(String jsonData) {
        var matcher = pattern.matcher(jsonData);
        if (matcher.find()) {
            return Map.entry(
                    new JsonString(matcher.group(1) == null ? "" : unescapeJson(matcher.group(1))),
                    matcher.end());
        } else {
            throw new IllegalArgumentException(
                    "First JSON token in: %s \n is not a valid JsonString".formatted(jsonData));
        }
    }

    /**
     * checks if the next token in the given JSON string is a JsonString.
     *
     * @param jsonData the JSON string to check
     * @return whether the next token is a JsonString
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
