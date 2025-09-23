package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.logic.AccessModifier;
import ecs.engr302.team14.gothim.util.Day;
import ecs.engr302.team14.gothim.util.Task;
import ecs.engr302.team14.gothim.logic.Clue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Taskbook {

    private Map<Day, List<Task>> tasks = new HashMap<>();
    private List<Clue> discoveredInformation = new ArrayList<>();

    public Taskbook() {
        tasks.put(Day.ONE, List.of(
                new Task(AccessModifier.Static, "Find something about purple cabbage"),
                new Task(AccessModifier.Public, "Talk to someone about the cow")
        ));

        tasks.put(Day.TWO, List.of(
                new Task(AccessModifier.Private, "Talk to a Robberson")
        ));
        discoveredInformation.add(
                new Clue(AccessModifier.Static, "Day1_Cabbage", "Purple cabbage make purple cow pats")
        );
    }

    public Map<Day, List<Task>> getTasks() {return tasks;}
    public List<Clue> getDiscoveredInformation() { return discoveredInformation; }

    public void addDiscoveredInformation(Clue clue) {
        discoveredInformation.add(clue);
    }
}
