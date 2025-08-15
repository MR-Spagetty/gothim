package ecs.engr302.team14.gothim.persistancy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * class for storing array or list style data in JSON.
 *
 * @author MR-Spagetty
 */
public class JsonArray extends JsonCollection<Integer> {

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

}
