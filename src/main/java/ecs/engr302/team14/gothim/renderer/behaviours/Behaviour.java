package ecs.engr302.team14.gothim.renderer.behaviours;

import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * basic interface to lump all tile asset behaviours together.
 *
 * @author MR-Spagetty
 */
public interface Behaviour {
    /**
     * checks if this behaviour should be applied.
     *
     * @param to the tile the behaviour applies to
     * @param neighbours the neighbours of the tile (permits the tile passed as
     *      {@code to} to be included)
     * @return whether this behaviour should be applied
     */
    public default Boolean applies(PrimitiveTile to, List<PrimitiveTile> neighbours) {
        neighbours = neighbours.parallelStream().filter(t -> !t.equals(to)).toList();
        if (neighbours.size() != 8) {
            throw new IllegalArgumentException("Must give all 8 neighbours of the tile");
        }
        Map<Point, PrimitiveTile> intermed = new HashMap<>();
        for (PrimitiveTile t : neighbours) {
            intermed.compute(t.pos(), (_, v) -> {
                if (v == null) {
                    return t;
                }
                throw new IllegalArgumentException("Must not have any tiles at the same position");
            });
        }
        return applies(to, intermed);
    }

    public Boolean applies(PrimitiveTile to, Map<Point, PrimitiveTile> neighbours);

    /**
     * The name of the asset that should be used if this behaviour should be applied.
     *
     * @return the asset name as used by {@link ecs.engr302.team14.gothim.renderer.Renderer#sprites}
     */
    public String assetName();
}
