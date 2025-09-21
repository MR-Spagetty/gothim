package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.logic.AccessModifier;

import java.util.Map;

public class Taskbook {
    private Player player;
    private Map<AccessModifier, String> tasks;
    private Map<AccessModifier, String> discoveredInformation;

    public Taskbook(Player player) {}

    public Map<AccessModifier, String> getTasks() {
        return tasks;
    }

    public void setTasks(Map<AccessModifier, String> tasks) {}

    public Map<AccessModifier, String> getDiscoveredInformation() {
        return discoveredInformation;
    }

    public void setDiscoveredInformation(Map<AccessModifier, String> discoveredInformation) {
        this.discoveredInformation = discoveredInformation;
    }
}
