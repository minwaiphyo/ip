package snowy;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Manages the list of tasks
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates an empty TaskList
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a TaskList with existing tasks
     * @param tasks ArrayList of tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list
     * @param task Task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes a task at the specified index
     * @param index Index of task to delete (0-based)
     * @return The deleted task
     * @throws SnowyException if index is invalid
     */
    public Task deleteTask(int index) throws SnowyException {
        if (index < 0 || index >= tasks.size()) {
            throw new SnowyException("Woof! That task number doesn't exist!");
        }
        return tasks.remove(index);
    }

    /**
     * Gets a task at the specified index
     * @param index Index of task to get (0-based)
     * @return The task at the index
     * @throws SnowyException if index is invalid
     */
    public Task getTask(int index) throws SnowyException {
        if (index < 0 || index >= tasks.size()) {
            throw new SnowyException("Woof! That task number doesn't exist!");
        }
        return tasks.get(index);
    }

    /**
     * Marks a task as done
     * @param index Index of task to mark (0-based)
     * @throws SnowyException if index is invalid
     */
    public void markTask(int index) throws SnowyException {
        if (index < 0 || index >= tasks.size()) {
            throw new SnowyException("Woof woof! That task number doesn't exist!");
        }
        tasks.get(index).markAsDone();
    }

    /**
     * Marks a task as not done
     * @param index Index of task to unmark (0-based)
     * @throws SnowyException if index is invalid
     */
    public void unmarkTask(int index) throws SnowyException {
        if (index < 0 || index >= tasks.size()) {
            throw new SnowyException("Woof woof! That number doesn't exist!");
        }
        tasks.get(index).markAsNotDone();
    }

    /**
     * Gets all tasks in the list
     * @return ArrayList of all tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Gets the number of tasks in the list
     * @return Number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Finds tasks occurring on a specific date
     * @param date The date to search for
     * @return ArrayList of tasks on that date
     */
    public ArrayList<Task> getTasksOnDate(LocalDate date) {
        ArrayList<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getBy().toLocalDate().equals(date)) {
                    matchingTasks.add(task);
                }
            } else if (task instanceof Event) {
                Event event = (Event) task;
                LocalDate fromDate = event.getStart().toLocalDate();
                LocalDate toDate = event.getEnd().toLocalDate();
                if (!date.isBefore(fromDate) && !date.isAfter(toDate)) {
                    matchingTasks.add(task);
                }
            }
        }

        return matchingTasks;
    }
}