package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.logic.AccessModifier;
import ecs.engr302.team14.gothim.logic.Clue;
import ecs.engr302.team14.gothim.util.Day;
import ecs.engr302.team14.gothim.util.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The taskbook, shows the user what clues they need to find and hte clues they
 * have already found.
 */
public class Taskbook {

    private Map<Day, List<Task>> tasks = new HashMap<>();
    private List<Clue> discoveredInformation = new ArrayList<>();

    /**
     * Creates a new taskbook.
     */
    public Taskbook() {
        // Use ArrayList so we can modify later
        List<Task> dayOneTasks = new ArrayList<>();
        dayOneTasks.add(new Task(AccessModifier.Static, "Find something about purple cabbage"));
        dayOneTasks.add(new Task(AccessModifier.Public, "Talk to someone about the cow"));
        tasks.put(Day.ONE, dayOneTasks);

        List<Task> dayTwoTasks = new ArrayList<>();
        dayTwoTasks.add(new Task(AccessModifier.Private, "Talk to a Robberson"));
        tasks.put(Day.TWO, dayTwoTasks);

        discoveredInformation.add(new Clue(AccessModifier.Static, "Day1_Cabbage",
                "Purple cabbage make purple cow pats"));
    }

    public Map<Day, List<Task>> getTasks() {
        return tasks;
    }

    public List<Clue> getDiscoveredInformation() {
        return discoveredInformation;
    }

    public void addDiscoveredInformation(Clue clue) {
        discoveredInformation.add(clue);
    }

    /**
     * Completes the specified task on the specified day.
     *
     * @param day the day to complete the task on
     * @param task the task to complete
     */
    public void completeTask(Day day, Task task) {
        List<Task> dayTasks = tasks.get(day);
        if (dayTasks != null) {
            dayTasks.remove(task);
        }
    }
}