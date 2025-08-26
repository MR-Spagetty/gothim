package ecs.engr302.team14.gothim.persistancy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
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
     * @throws IllegalArgumentException if adding the new item would create a cycle
     *      in the JSON structure
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

}
