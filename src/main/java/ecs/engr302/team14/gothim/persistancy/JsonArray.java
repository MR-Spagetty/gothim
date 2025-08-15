package ecs.engr302.team14.gothim.persistancy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * class for storing array or list style data in JSON.
 *
 * @author MR-Spagetty
 */
public class JsonArray implements JsonCollection<Integer> {

    private final List<JsonType> items = new ArrayList<>();

    @Override
    public Optional<JsonType> get(Integer position) {
        if (position < 0 || position >= size()) {
            return Optional.empty();
        }
        return Optional.of(items.get(position));
    }

    public void add(JsonType newItem) {
        this.items.add(newItem);
    }

    @Override
    public int size() {
        return this.items.size();
    }

}
