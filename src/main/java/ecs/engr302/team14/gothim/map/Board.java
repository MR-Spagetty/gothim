package ecs.engr302.team14.gothim.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.entities.NPC;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;

public class Board {

    Map<Point, PrimitiveTile> board = new HashMap<>();
    @SerializedField(deserialParamName="tiles")
    List<PrimitiveTile> allTiles;
    List<Player> players = new ArrayList<>();
    Map<String, NPC> npcs = new HashMap<>();
    @SerializedField(deserialParamName="entities")
    List<Entity> allEntities;
    
    /**
     * 
     * @param tiles
     * @param entities
     */
    @DeserializationMethod(serialFieldNames = { "tiles", "entities" })
    Board(List<PrimitiveTile> tiles , List<Entity> entities){
        this.allTiles = new ArrayList<>(tiles);
        this.allTiles.stream().forEach(t -> this.board.put(t.pos(), t));
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

    public Map<Point, PrimitiveTile> getBoard(){
        return board;
    }

     /*---------------------- Tile Stuff -----------------------*/

    public PrimitiveTile getTile(Point p){
        return board.get(p);
    }

    /*---------------------- Player Stuff -----------------------*/

    public List<Player> getPlayers(){
        return players;
    }

    public Player getPlayer(int id){
        return players.get(id);
    }

    /*---------------------- NPC Stuff -----------------------*/
    /**
     * 
     * @return
     */
    public Map<String, NPC> getNpcs(){
        return npcs;
    }

    public NPC getNPC(String name){
        return npcs.get(name);
    }


}
