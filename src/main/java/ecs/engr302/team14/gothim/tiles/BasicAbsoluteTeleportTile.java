package ecs.engr302.team14.gothim.tiles;

import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;
import java.util.Objects;
import java.util.Optional;

/**
 * basic tile that teleports the entity that enters it to an absolute point on
 * the map.
 *
 * @author MR-Spagetty
 */
public class BasicAbsoluteTeleportTile extends PrimitiveEffectTile {

    @SerializedField(deserialParamName = "destination")
    protected final Point dest;

    @DeserializationMethod(serialFieldNames = { "pos", "style", "destination" })
    public BasicAbsoluteTeleportTile(Point pos, String style,
            Point destination /* , Region reg?? */) {
        super(pos, style);
        this.dest = Objects.requireNonNull(destination);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass())
                && ((BasicAbsoluteTeleportTile) obj).pos.equals(this.pos)
                && ((BasicAbsoluteTeleportTile) obj).style.equals(this.style)
                && ((BasicAbsoluteTeleportTile) obj).dest.equals(this.dest);
    }


    @Override
    public boolean canEnter(Entity e) {
        return mapRef().getTile(dest).canEnter(e);
    }

    @Override
    public void enter(Entity e) {
        mapRef().getTile(dest).enter(e);
    }

    @Override
    public Optional<Entity> getOccupant() {
        return mapRef().getTile(dest).getOccupant();
    }
}