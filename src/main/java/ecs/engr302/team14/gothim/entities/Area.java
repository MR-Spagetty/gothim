package ecs.engr302.team14.gothim.entities;

public class Area {
    
    private String name; 
    private int requiredAccess; 

    public Area(String name, int requiredAccess) {
        this.name = name;
        this.requiredAccess = requiredAccess;
    }

    public String accessLevelToString() {
        switch(requiredAccess) {
            case 0: return "PRIVATE";
            case 1: return "PROTECTED";
            case 2: return "PUBLIC";
            default: return "UNKNOWN";
        }
    }

    public int getRequiredAccess(){
        return this.requiredAccess;
    }

    public String getName() {
        return this.name;
    }
}
