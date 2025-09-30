package ecs.engr302.team14.gothim.networking;

import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.entities.Taskbook;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.util.List;

/**
 * Record for storing update data.
 *
 * @author MR-Spagetty
 */
public record UpdateData(
        @SerializedField
        String levelID,
        @SerializedField
        List<Player> playerData,
        @SerializedField
        List<Entity> nonPlayersData,
        @SerializedField
        Taskbook taskbookData) {

    /**
     * Creates a new update data object.
     *
     * @param playerData the data of the players
     * @param nonPlayersData the data of the non player entities
     * @param taskbookData the data of the taskbook
     */
    @DeserializationMethod(serialFieldNames = {
        "levelID",
        "playerData",
        "nonPlayersData",
        "taskbookData" })
    public UpdateData {}

}
