package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.app.Main;
import ecs.engr302.team14.gothim.logic.dialogue.Dialogue;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializationExtends;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;

/**
 * Basic NPC class.
 */
@SerializationExtends(Entity.class)
public class NPC extends InteractableEntity {

    @SerializedField
    private Dialogue dialogue;

    @DeserializationMethod(serialFieldNames = { "name", "dialogue", "pos" })
    public NPC(String name, Dialogue dialogue, Point position) {
        super(name, position);
        this.dialogue = dialogue;
    }

    //Complete interaction
    public void interact(Player p) {
        Main.dialogue(this.name, this.dialogue);
    }
}