
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * A trivial, collections based database for Task objects
 */
public class TasksDatabase {
    private final Map<Integer, Task> tasks;

    /**
     * Create a new database and populate it with sample data
     */
    public TasksDatabase() {
        tasks = new HashMap<>();
        this.add(new Task(1, "Buy milk"));
        this.add(new Task(2, "Feed the cat"));
    }

    /**
     * Get all tasks
     *
     * @return A list of tasks
     */
    public List<Task> all() {
        return new ArrayList<>(tasks.values());
    }

    /**
     * Get a single task
     *
     * @param id the id of the task
     * @return The task object or null if the task was not found
     */
    public Task get(Integer id) {
        return tasks.get(id);
    }

    /**
     * Add a task to the database
     *
     * @param task The task to be stored
     * @return false if the task was not stored
     */
    public boolean add(Task task) {
        if (tasks.containsKey(task.getId())) return false;
        tasks.put(task.getId(), task);
        return true;
    }
}
