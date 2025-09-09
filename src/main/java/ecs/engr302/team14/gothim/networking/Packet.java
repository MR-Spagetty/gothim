package ecs.engr302.team14.gothim.networking;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;

/**
 * Interface to lump all packet types together.
 *
 * @author MR-Spagetty
 */
public interface Packet {

    /**
     * Packet for sending a specific request.
     *
     * @param playerId the id of the player this is comming from
     * @param request the request to execute
     */
    record Request(
                @SerializedField int playerId,
                @SerializedField String request) implements Packet {
        @DeserializationMethod(serialFieldNames = { "playerId", "request" })
        public Request {
        }
    }

    /**
     * Packet for telling anouther player that you have performed an action.
     *
     * @param playerId who performed the action
     * @param act the action that was performed
     */
    record Action(
            @SerializedField int playerId,
            @SerializedField(deserialParamName = "action") PlayerAction act) implements Packet {
        @DeserializationMethod(serialFieldNames = { "playerId", "action" })
        public Action {
        }

    }

    /**
     * Packet for requesting a data sync from another player.
     *
     * @param playerId the player the request comes from
     */
    record Sync(@SerializedField int playerId) implements Packet {
        @DeserializationMethod(serialFieldNames = { "playerId" })
        public Sync {
        }

        /**
         * Packet for replying to Sync requests.
         *
         * @param playerId the player the reply comes from
         * @param update the sync/update data
         */
        public record Reply(
                @SerializedField int playerId,
                @SerializedField UpdateData update) implements Packet {
            @DeserializationMethod(serialFieldNames = { "playerId", "update" })
            public Reply {}
        }
    }

    /**
     * Packet for updating another player.
     *
     * <p>Generally only sent by the host
     *
     * @param playerId the player the update was sent by
     * @param update the update data
     */
    record Update(
            @SerializedField int playerId,
            @SerializedField UpdateData update) implements Packet{
        @DeserializationMethod(serialFieldNames = { "playerId", "update" })
        public Update {
        }
    }
}
