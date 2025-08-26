package ecs.engr302.team14.gothim.persistancy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * class for storing object style data in JSON.
 *
 * @author MR-Spagetty
 */
public final class JsonObject extends JsonCollection<String> {
    private final Map<String, JsonType> items = new LinkedHashMap<>();

    @Override
    public Optional<JsonType> get(String position) {
        return Optional.ofNullable(this.items.get(position));
    }

    /**
     * Adds or updates an item in this JsonObject.
     *
     * @param position the key to add or update
     * @param newItem the item to add or update
     * @return the previous item at the given key, or empty if there was none
     * @throws IllegalArgumentException if adding the new item would create a
     *      cycle in the JSON structure
     */
    public Optional<JsonType> put(String position, JsonType newItem) {
        if (newItem == this || newItem instanceof JsonCollection col && col.containsExactly(this)) {
            throw new IllegalArgumentException("Collection Cycles are not permitted");
        }
        return Optional.ofNullable(this.items.put(position, newItem));
    }

    @Override
    protected boolean containsExactly(JsonType jsonItem) {
        return items.values().parallelStream().anyMatch(i -> i == jsonItem
                || (i instanceof JsonCollection col && col.containsExactly(jsonItem)));
    }

    @Override
    public Optional<JsonType> remove(String position) {
        return Optional.of(items.remove(position));
    }

    @Override
    public int size() {
        return this.items.size();
    }

    private String serialPairs(boolean pretty, int indentationLevel) {
        return this.items.entrySet().parallelStream().map(p -> {
            String key = indent(pretty ? indentationLevel : 0) + new JsonString(p.getKey()) + ": ";
            if (pretty && p.getValue() instanceof JsonCollection col) {
                return key + col.prettyPrint(indentationLevel);
            }
            return key + p.getValue().toString();
        }).collect(Collectors.joining(pretty ? ",\n" : ", "));
    }

    @Override
    protected String prettyPrint(int indentationLevel) {
        return String.format("""
                {
                %s
                %s}""", serialPairs(true, indentationLevel + 1), indent(indentationLevel));
    }

    @Override
    public String toString() {
        return "{%s}".formatted(serialPairs(false, 0));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JsonObject object && object.items.equals(this.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    /**
     * Parses a JsonObject from the start of the given string.
     *
     * @param jsonData the JSON string to parse
     * @return the parsed JsonObject and the position of the next token in the
     *      given string
     * @throws IllegalArgumentException if the first token in the string is not
     *      a JsonObject
     */
    public static Map.Entry<JsonObject, Integer> parse(String jsonData) {
        if (!isNext(jsonData)) {
            throw new IllegalArgumentException(
                    "First JSON token in: %s \n is not a valid JsonObject".formatted(jsonData));
        }
        int overallOffset = jsonData.indexOf('{') + 1;
        jsonData = jsonData.substring(overallOffset);
        JsonObject obj = new JsonObject();
        while (!jsonData.strip().startsWith("}") && !jsonData.isEmpty()) {
            var keyDat = JsonString.parse(jsonData);
            String key = keyDat.getKey().value();
            overallOffset += keyDat.getValue();
            jsonData = jsonData.substring(keyDat.getValue());
            final var pairSepPat = Pattern.compile("^\\s*:");
            var matcher = pairSepPat.matcher(jsonData);
            if (matcher.find()) {
                jsonData = jsonData.substring(matcher.end());
                overallOffset += matcher.end();
            } else {
                throw new IllegalArgumentException(
                        "Expected ':' after key in JsonObject at: %s".formatted(key + jsonData));
            }
            var itemDat = parseItem(jsonData);
            int off = itemDat.getValue();
            obj.put(key, itemDat.getKey());
            jsonData = jsonData.substring(off);
            overallOffset += off;
            if (parseItemSep(jsonData) >= 0) {
                off = parseItemSep(jsonData);
                jsonData = jsonData.substring(off);
                overallOffset += off;
                if (jsonData.strip().startsWith("}")) {
                    throw new IllegalArgumentException(
                        "Trailing comma in JsonObject is not permitted"
                    );
                }
            } else if (!jsonData.strip().startsWith("}")) {
                throw new IllegalArgumentException(
                        "Expected ',' or '}' in JsonObject at: %s".formatted(jsonData));
            }
        }
        overallOffset += jsonData.indexOf('}') + 1;
        return Map.entry(obj, overallOffset);
    }

    public static boolean isNext(String jsonData) {
        return jsonData.strip().startsWith("{");
    }

}
