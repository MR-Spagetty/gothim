package ecs.engr302.team14.gothim.entities;

public class GameTest {
    public static void main(String[] args) {
        
        Area townSquare = new Area("Town Square", 0); //public
        Area library = new Area("Library", 1); //protected (must be librarian)

        Player player = new Player("Purp"); 
        player.setCurrentArea(townSquare);
        
        System.out.println(player.getName() + " is currently in the " + player.getCurrentArea().getName());
        System.out.println(player.getName() + " has clearance: " + player.getCurrentArea().accessLevelToString());

        player.moveTo(library);
        player.setAccessLevel(1);
        player.moveTo(library);

        System.out.println(player.getName() + " is currently in the " + player.getCurrentArea().getName());
        System.out.println(player.getName() + " has clearance: " + player.getCurrentArea().accessLevelToString());
    }
}