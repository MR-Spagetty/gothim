package ecs.engr302.team14.gothim.persistancy;

import java.util.Optional;

/**
 * a basic interface to lump all JSON collection types (array, object) together
 * for ease of differentiation.
 *
 * @param <K> the "key" type of the collection
 */
public abstract class JsonCollection<K> implements JsonType {
    public abstract Optional<JsonType> get(K position);

    public abstract Optional<JsonType> remove(K position);

    public abstract int size();

    public String prettyPrint() {
        return prettyPrint(0);
    }

    protected abstract String prettyPrint(int indentationLevel);

    static String indent(int level){
        return "    ".repeat(level);
    }
}
