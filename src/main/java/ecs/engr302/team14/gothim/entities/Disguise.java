package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.app.LevelManager;
import ecs.engr302.team14.gothim.logic.DisguiseableAs;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializationExtends;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;
import java.util.Optional;

/**
 * Disguise item allows the player to disguise themselves for example as a
 * member of one of the 4 families.
 *
 * @author MR-Spagetty
 */
@SerializationExtends(Item.class)
public class Disguise extends Item {

    @SerializedField
    private final DisguiseableAs disguisesAs;

    @DeserializationMethod(serialFieldNames = { "name", "position", "description", "collected",
            "disguisesAs" })
    public Disguise(String name, Point position, String description, boolean collected,
            DisguiseableAs disguisesAs) {
        super(name, position, description, collected);
        this.disguisesAs = disguisesAs;
    }

    public DisguiseableAs disguise() {
        return this.disguisesAs;
    }

    @Override
    public void interact(Player p) {
        if (this.isCollected()) {
            return;
        }
        PrimitiveTile posTile = LevelManager.getLevelData().map().getTile(this.position);
        Optional.ofNullable(p.disguise).ifPresentOrElse(d -> {
            d.position = this.position;
            posTile.setOccupant(d);
        }, () -> posTile.setOccupant(null));
        p.disguise = this;
    }
}
