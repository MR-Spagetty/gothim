package ecs.engr302.team14.gothim.renderer.behaviours;

import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.entities.Item;
import ecs.engr302.team14.gothim.entities.NPC;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Direction;
import ecs.engr302.team14.gothim.util.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Entity related Behaviour conditions are conditions for behaviour ot be
 * dictated by a nearby entity.
 *
 * @author MR-Spagetty
 */
public interface EntityRelated extends BehaviourCondition {
    boolean entityCond(Entity e, PrimitiveTile to);

    Direction side();

    ArrayList<String> categories();

    @Override
    default Boolean applies(PrimitiveTile to, Map<Point, PrimitiveTile> neighbours) {
        return (side() == Direction.None ? to : neighbours.get(to.pos().add(side().offset)))
                .getOccupant()
                .filter(e -> ofCategory(categories(), e)).map(e -> entityCond(e, to)).orElse(false);
    }

    /**
     * Checks if the given entity meets all of the specified categories.
     *
     * @param categories the categories to go through
     * @param e the entity to check
     * @return whether it fulfils the categories
     */
    static boolean ofCategory(List<String> categories, Entity e) {
        boolean res = true;
        for (String cat : categories) {
            if (cat.startsWith("name")) {
                if (!cat.substring(cat.indexOf("=") + 1).equals(e.getName())) {
                    res &= (cat.charAt(cat.indexOf("=") - 1) == '!') ? true : false;
                } else {
                    res &= (cat.charAt(cat.indexOf("=") - 1) == '!') ? false : true;
                }
                continue;
            }
            res &= switch (cat) {
                case "player" -> e instanceof Player;
                case "item" -> e instanceof Item;
                case "npc" -> e instanceof NPC;
                default -> false;
            };
        }
        return res;
    }
}
