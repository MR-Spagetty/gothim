package ecs.engr302.team14.gothim.persistancy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * class for storing object style data in JSON.
 *
 * @author MR-Spagetty
 */
public final class JsonObject extends JsonCollection<String> {
    private final Map<String, JsonType> items = new HashMap<>();

    @Override
    public Optional<JsonType> get(String position) {
        return Optional.ofNullable(this.items.get(position));
    }

    public Optional<JsonType> put(String position, JsonType newItem) {
        return Optional.ofNullable(this.items.put(position, newItem));
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
                %s}
                """, serialPairs(true, indentationLevel + 1), indent(indentationLevel));
    }

    @Override
    public String toString() {
        return "{%s}".formatted(serialPairs(false, 0));
    }

}
