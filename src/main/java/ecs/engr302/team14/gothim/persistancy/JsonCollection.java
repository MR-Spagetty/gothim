package ecs.engr302.team14.gothim.persistancy;

import java.util.Optional;

/**
 * a basic interface to lump all JSON collection types (array, object) together
 * for ease of differentiation.
 *
 * @param <K> the "key" type of the collection
 */
public interface JsonCollection<K> extends JsonObject {
    Optional<JsonObject> get(K position);
}
