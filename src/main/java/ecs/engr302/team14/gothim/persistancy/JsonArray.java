package ecs.engr302.team14.gothim.persistancy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * class for storing array or list style data in JSON.
 *
 * @author MR-Spagetty
 */
public final class JsonArray extends JsonCollection<Integer> {

    private final List<JsonType> items = new ArrayList<>();

    @Override
    public Optional<JsonType> get(Integer position) {
        if (position < 0 || position >= size()) {
            return Optional.empty();
        }
        return Optional.of(items.get(position));
    }

    /**
     * Adds a new item to the end of this JsonArray.
     *
     * @param newItem the item to add
     * @throws IllegalArgumentException if adding the new item would create a cycle
     *      in the JSON structure
     */
    public void add(JsonType newItem) {
        if (newItem == this || newItem instanceof JsonCollection col && col.containsExactly(this)) {
            throw new IllegalArgumentException("Collection Cycles are not permitted");
        }
        this.items.add(newItem);
    }

    @Override
    protected boolean containsExactly(JsonType jsonItem) {
        return items.parallelStream().anyMatch(i -> i == jsonItem
        || (i instanceof JsonCollection col && col.containsExactly(jsonItem)));
    }

    @Override
    public Optional<JsonType> remove(Integer position) {
        var ret = get(position);
        this.items.remove((int) position);
        return ret;
    }

    @Override
    public int size() {
        return this.items.size();
    }

    @Override
    protected String prettyPrint(int indentationLevel) {
        return items.stream().map(i -> {
            if (i instanceof JsonCollection col) {
                return col.prettyPrint(indentationLevel + 1);
            }
            return i.toString();
        }).map(i -> indent(indentationLevel + 1) + i)
                .collect(Collectors.joining(",\n", "[\n", "\n" + indent(indentationLevel) + "]"));
    }

    @Override
    public String toString() {
        return items.stream().map(Object::toString).toList().toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JsonArray arr && arr.items.equals(this.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    /**
     * Parses a JsonArray from the start of the given JSON string.
     *
     * @param jsonData the JSON string to parse
     * @return the parsed JsonArray and the position of the next token in the given string
     * @throws IllegalArgumentException if the first token in the string is not a JsonArray
     */
    public static Map.Entry<JsonArray, Integer> parse(String jsonData) {
        if (!isNext(jsonData)) {
            throw new IllegalArgumentException("JsonArray must start with '['");
        }
        JsonArray arr = new JsonArray();
        int overallOffset = jsonData.indexOf('[') + 1;
        jsonData = jsonData.substring(overallOffset);
        while (!jsonData.strip().startsWith("]") && !jsonData.isEmpty()) {
            var itemDat = parseItem(jsonData);
            int off = itemDat.getValue();
            arr.add(itemDat.getKey());
            jsonData = jsonData.substring(off);
            overallOffset += off;
            if (parseItemSep(jsonData) >= 0) {
                off = parseItemSep(jsonData);
                jsonData = jsonData.substring(off);
                overallOffset += off;
                if (jsonData.strip().startsWith("]")) {
                    throw new IllegalArgumentException(
                        "Trailing comma in JsonArray is not permitted"
                    );
                }
            } else if (!jsonData.strip().startsWith("]")) {
                throw new IllegalArgumentException("JsonArray must end with ']'");
            }
        }
        overallOffset += jsonData.indexOf(']') + 1;
        return Map.entry(arr, overallOffset);
    }

    public static boolean isNext(String jsonData) {
        return jsonData.strip().startsWith("[");
    }

}
