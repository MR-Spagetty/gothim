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
public class Disguise<T extends DisguiseableAs> extends Item {

    @SerializedField
    private final T disguisesAs;

    @DeserializationMethod(serialFieldNames = { "name", "position", "description", "collected",
            "disguisesAs" })
    public Disguise(String name, Point position, String description, boolean collected,
            T disguisesAs) {
        super(name, position, description, collected);
        this.disguisesAs = disguisesAs;
    }

    public T disguise() {
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
            posTile.setOcupant(d);
        }, () -> posTile.setOcupant(null));
        p.disguise = this;
    }
}
