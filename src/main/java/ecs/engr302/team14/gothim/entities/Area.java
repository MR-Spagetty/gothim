package ecs.engr302.team14.gothim.entities;

/**
 * Area class for usi in access control of a space.
 */
public class Area {

    private String name;
    private int requiredAccess;

    public Area(String name, int requiredAccess) {
        this.name = name;
        this.requiredAccess = requiredAccess;
    }

    /**
     * converts the required accessLevel numerical id to a human readable string.
     *
     * @return the human readable string
     */
    public String accessLevelToString() {
        return switch (requiredAccess) {
            case 0 -> "PRIVATE";
            case 1 -> "PROTECTED";
            case 2 -> "PUBLIC";
            default -> "UNKNOWN";
        };
    }

    public int getRequiredAccess() {
        return this.requiredAccess;
    }

    public String getName() {
        return this.name;
    }
}
