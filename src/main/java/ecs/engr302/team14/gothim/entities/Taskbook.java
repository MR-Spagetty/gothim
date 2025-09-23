package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.logic.AccessModifier;
import ecs.engr302.team14.gothim.util.Day;
import ecs.engr302.team14.gothim.util.Task;

import java.util.Map;

public class Taskbook {
    private Player player;

    private Map<Day, Task> tasks;

    private Map<AccessModifier, String> discoveredInformation;

    public Taskbook(Player player) {}

    public Map<Day, Task> getTasks() {
        return tasks;
    }

    public Map<AccessModifier, String> getDiscoveredInformation() {
        return discoveredInformation;
    }

    public void setDiscoveredInformation(Map<AccessModifier, String> discoveredInformation) {
        this.discoveredInformation = discoveredInformation;
    }
}
