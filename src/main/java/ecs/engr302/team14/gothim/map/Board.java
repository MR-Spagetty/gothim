package ecs.engr302.team14.gothim.map;

import java.util.List;
import java.util.Map;

import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.entities.NPC;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Point;

public class Board {

    Map<Point, PrimitiveTile> board;
    List<PrimitiveTile> allTiles;
    Map<String, Player> players;
    Map<String, NPC> npcs;
    List<Entity> allEntities;
    
    /* Testing commit .*/
    Board(List<PrimitiveTile> tiles , List<Entity> entities){
        this.allTiles = tiles;
        this.allEntities = entities;
        setPlayersAndNpcs();
        setBoard();
    }

    /*---------------------- Entities Stuff -----------------------*/

    public void setPlayersAndNpcs(){
        for(Entity e : allEntities){
            if(e instanceof Player p){
                players.put(p.getName(), p);
            }
        }
    }

    /*---------------------- Board Stuff -----------------------*/

    public void setBoard(){
        for(PrimitiveTile t: allTiles){
            board.put(t.getPosition(), t);
        }
    }

    public Map<Point, PrimitiveTile> getBoard(){
        return board;
    }

    public void updateBoard(Point position){
        
    }

    /*---------------------- Player Stuff -----------------------*/

    public Map<String, Player> getPlayers(){
        return players;
    }

    public Player getPlayer(String name){
        return players.get(name);
    }

    public Point getPlayerPos(String name){
        return players.get(name).getPosition();
    }

    public void updatePlayerPos(String name, Point p){
        players.get(name).setPosition(p);
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

    public Point getNPCPos(String name){
        return npcs.get(name).getPosition();
    }

    public void updateNPCPos(String name, Point p){
        npcs.get(name).setPosition(p);
    }


}
