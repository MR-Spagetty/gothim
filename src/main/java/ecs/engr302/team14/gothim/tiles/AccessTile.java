package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.logic.dialogue.prerequisites.DialoguePrerequisite;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;

/**
 * Tile that can onl.y be entered by a player if that player meets its requirements
 *
 * @implNote uses DialoguePrerequisite to determine if player can enter for minimal code duplication
 * @author MR-Spagetty
 */
public class AccessTile extends PrimitiveTile {

    @SerializedField
    private final DialoguePrerequisite cond;

    @DeserializationMethod(serialFieldNames = { "pos", "style", "cond" })
    public AccessTile(Point pos, String style, DialoguePrerequisite cond) {
        super(pos, style);
        this.cond = cond;
    }

    @Override
    public boolean canEnter(Entity e) {
        boolean sup = super.canEnter(e);
        if (e instanceof Player p) {
            return sup && cond.met(p);
        }
        return sup;
    }

}
