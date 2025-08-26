package ecs.engr302.team14.gothim.persistancy;

import java.util.Map;

/**
 * Record for storing numbers in JSON.
 *
 * @param value the numeric value of this object
 * @author MR-Spagetty
 */
public record JsonNum(Double value) implements JsonValue<Double> {

    @Override
    public final String toString() {
        return "" + value;
    }

    /**
     * Parses a JsonNum from the start of the given JSON string.
     *
     * @param jsonData the JSON string to parse from
     * @return the parsed JsonNum and the position of the next token in the
     *      given string
     * @throws IllegalArgumentException if the first token in the string is not
     *      a JsonNum
     */
    public static Map.Entry<JsonNum, Integer> parse(String jsonData) {
        int end = 0;
        while (end < jsonData.length() && Character.isWhitespace(jsonData.charAt(end))) {
            end++;
        }
        while (end < jsonData.length()
                && (Character.isDigit(jsonData.charAt(end)) || jsonData.charAt(end) == '-'
                        || jsonData.charAt(end) == '+' || jsonData.charAt(end) == 'e'
                        || jsonData.charAt(end) == 'E' || jsonData.charAt(end) == '.')) {
            end++;
        }
        try {
            return Map.entry(new JsonNum(Double.parseDouble(jsonData.substring(0, end))), end);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "First JSON token in: %s \n is not a valid JsonNum".formatted(jsonData));
        }
    }

    /**
     * Checks if the next token in the given JSON string is a JsonNum.
     *
     * @param jsonData the JSON string to check
     * @return whether the next token is a JsonNum
     */
    public static boolean isNext(String jsonData) {
        return switch (jsonData.strip().charAt(0)) {
            case '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> true;
            default -> false;
        };
    }

}
