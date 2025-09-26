package ecs.engr302.team14.gothim.map;

import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.entities.NPC;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class for holding the map data for the game.
 */
public class Board {

    Map<Point, PrimitiveTile> board = new HashMap<>();
    @SerializedField(deserialParamName = "tiles")
    List<PrimitiveTile> allTiles;
    List<Player> players = new ArrayList<>();
    Map<String, NPC> npcs = new HashMap<>();
    @SerializedField(deserialParamName = "entities")
    List<Entity> allEntities;
    
    /**
     * Creates a new board from the given tiles and entities.
     *
     * @param tiles the tiles for the map
     * @param entities the entities within the map
     */
    @DeserializationMethod(serialFieldNames = { "tiles", "entities" })
    Board(List<PrimitiveTile> tiles, List<Entity> entities) {
        this.allTiles = new ArrayList<>(tiles);
        this.allTiles.stream().forEach(t -> {
            this.board.put(t.pos(), t);
            t.linkMap(this);
        });
        this.allEntities = new ArrayList<>(entities);
        this.allEntities.forEach(e -> {
            switch (e) {
                case NPC npc -> this.npcs.put(npc.getName(), npc);
                case Player p -> this.players.add(p);
                default -> {
                }
            }
        });
    }

    /*---------------------- Board Stuff -----------------------*/

    public Map<Point, PrimitiveTile> getBoard() {
        return board;
    }

    /*---------------------- Tile Stuff -----------------------*/

    /**
     * Gets the tile at the given point.
     *
     * @param p the point to get the tile at
     * @return the tile at that point
     */
    public PrimitiveTile getTile(Point p) {
        return board.get(p);
    }

    /**
     * Gets all tiles within the given rectangle.
     *
     * @param topleft the top left Point of the rectangle
     * @param bottomRight the bottom right Point of the rectangle
     * @return a list of the tiles within that rectangle
     */
    public List<PrimitiveTile> getTiles(Point topleft, Point bottomRight) {
        List<PrimitiveTile> ls = new ArrayList<>();
        for (double x = topleft.x(); x <= bottomRight.x(); x++) {
            for (double y = topleft.y(); y <= bottomRight.y(); y++) {
                //get an option from the board and add to list, otherwise do nothing
                Optional.ofNullable(board.get(new Point(x, y))).ifPresent(ls::add);
            }
        }
        return ls;
    }



    /*---------------------- Player Stuff -----------------------*/

    /**
     * Get all the player entities on the map.
     *
     * @return list of the players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Get the player with the given ID.
     *
     * @param id the id of the player to get.
     * @return the specified player
     */
    public Player getPlayer(int id) {
        return players.get(id);
    }

    /*---------------------- NPC Stuff -----------------------*/
    /**
     * Get all NPCs on the map.
     *
     * @return map of NPCs with their names as keys.
     */
    public Map<String, NPC> getNpcs() {
        return npcs;
    }

    /**
     * Get the NPC with the given name.
     *
     * @param name the name of the NPC to get.
     * @return the specified NPC
     */
    public NPC getNPC(String name) {
        return npcs.get(name);
    }


}
