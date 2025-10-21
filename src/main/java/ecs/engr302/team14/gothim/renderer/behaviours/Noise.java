package ecs.engr302.team14.gothim.renderer.behaviours;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Condition that returns a deterministic pseudo-random boolean for a given tile.
 * The randomness is derived from the tile's fields (position, style) combined
 * with optional parameters so the same tile at the same position will always
 * produce the same result. Parameters:
 *  - chance: probability [0..1] of returning true
 *  - salt: an optional string to vary the randomness
 *  - variant: an integer used to produce different distributions for the same tile
 */
public record Noise(
        @SerializedField
        double chance,
        @SerializedField
        String salt,
        @SerializedField
        Integer variant,
        @SerializedField
        boolean includeNeighbours
) implements BehaviourCondition {

    @DeserializationMethod(serialFieldNames = { "chance", "salt", "variant", "includeNeighbours" })
    public Noise {
        if (chance < 0 || chance > 1) {
            throw new IllegalArgumentException("chance must be between 0 and 1");
        }
        if (salt == null) {
            salt = "";
        }
    }

    @Override
    public Boolean applies(PrimitiveTile to, Map<Point, PrimitiveTile> neighbours) {
        // copy and validate neighbours like other behaviours
        neighbours = Map.copyOf(neighbours);
        neighbours = new java.util.HashMap<>(neighbours);
        neighbours.remove(to.pos());
        if (neighbours.size() != 8) {
            throw new IllegalArgumentException("Must have all 8 neighbours");
        }

        // Build a stable byte sequence from tile fields and parameters
        StringBuilder sb = new StringBuilder();
        sb.append(to.pos().x()).append(',').append(to.pos().y()).append(';');
        sb.append(to.style).append(';');
        sb.append(salt).append(';');
        sb.append(variant).append(';');

        // Optionally include neighbour presence to vary noise by surroundings
        // We'll include the neighbour styles in a fixed order (sorted by point)
        if (includeNeighbours) {
            neighbours.entrySet().stream()
                    .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                    .forEach(e -> {
                        sb.append(e.getKey().x()).append(',').append(e.getKey().y()).append(':');
                        sb.append(e.getValue() == null ? "null" : e.getValue().style).append('|');
                    });
        }

        byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);

        // Compute a deterministic 64-bit hash (FNV-1a-like) and convert to double [0,1)
        long hash = 1469598103934665603L; // FNV offset basis
        for (byte b : data) {
            hash ^= (b & 0xff);
            hash *= 1099511628211L; // FNV prime
        }

    // Mix hash further with variant (handle boxed Integer possibly null)
    int var = (variant == null) ? 0 : variant;
    hash ^= (long) var * 0x9e3779b97f4a7c15L;
        hash = Long.rotateLeft(hash, 23) ^ (hash >>> 17);

    // Convert to double in [0,1) using top 53 bits (safe and well-distributed)
    long top53 = (hash >>> 11); // keep top 53 bits
    double rnd = top53 * (1.0 / (1L << 53));

        return rnd < chance;
    }

}
