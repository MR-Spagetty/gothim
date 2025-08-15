package ecs.engr302.team14.gothim.persistancy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * class for storing object style data in JSON.
 *
 * @author MR-Spagetty
 */
public final class JsonObject implements JsonCollection<String> {
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
}
