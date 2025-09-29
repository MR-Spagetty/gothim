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
        // Use ArrayList so we can modify later
        List<Task> dayOneTasks = new ArrayList<>();
        dayOneTasks.add(new Task(AccessModifier.Static, "Find something about purple cabbage"));
        dayOneTasks.add(new Task(AccessModifier.Public, "Talk to someone about the cow"));
        tasks.put(Day.ONE, dayOneTasks);

        List<Task> dayTwoTasks = new ArrayList<>();
        dayTwoTasks.add(new Task(AccessModifier.Private, "Talk to a Robberson"));
        tasks.put(Day.TWO, dayTwoTasks);

        discoveredInformation.add(
                new Clue(AccessModifier.Static, "Day1_Cabbage", "Purple cabbage make purple cow pats")
        );
    }

    public Map<Day, List<Task>> getTasks() {return tasks;}
    public List<Clue> getDiscoveredInformation() { return discoveredInformation; }

    public void addDiscoveredInformation(Clue clue) {
        discoveredInformation.add(clue);
    }
    public void completeTask(Day day, Task task) {
        List<Task> dayTasks = tasks.get(day);
        if (dayTasks != null) {
            dayTasks.remove(task);
        }
    }
}