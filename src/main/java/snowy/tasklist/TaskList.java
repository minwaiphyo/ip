package snowy.tasklist;

import snowy.exception.SnowyException;
import snowy.task.Deadline;
import snowy.task.Event;
import snowy.task.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Manages the task list for the Snowy chatbot.
 * This class encapsulates an ArrayList of tasks and provides operations
 * to add, delete, mark, unmark, and retrieve tasks. It also handles
 * validation of task indices and provides specific queries such as
 * finding tasks occurring on a specific date.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates an empty TaskList with no tasks.
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
        assert task != null : "Cannot add a null task to the list";
        tasks.add(task);
    }

    /**
     * Deletes a task at the specified index
     * @param index Index of task to delete (0-based)
     * @return The deleted task
     * @throws SnowyException if index is invalid
     */
    public Task deleteTask(int index) throws SnowyException {
        validateIndex(index);
        Task removed = tasks.remove(index);
        assert removed != null : "Removed task should not be null";
        return removed;
    }

    /**
     * Gets a task at the specified index
     * @param index Index of task to get (0-based)
     * @return The task at the index
     * @throws SnowyException if index is invalid
     */
    public Task getTask(int index) throws SnowyException {
        validateIndex(index);
        assert index >= 0 && index < tasks.size() : "Index must be valid after bounds check";
        return tasks.get(index);
    }

    /**
     * Marks a task as done
     * @param index Index of task to mark (0-based)
     * @throws SnowyException if index is invalid
     */
    public void markTask(int index) throws SnowyException {
        validateIndex(index);
        tasks.get(index).markAsDone();
    }

    /**
     * Marks a task as not done
     * @param index Index of task to unmark (0-based)
     * @throws SnowyException if index is invalid
     */
    public void unmarkTask(int index) throws SnowyException {
        validateIndex(index);
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
        return tasks.stream()
                .filter(task -> isTaskOnDate(task, date))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Checks whether a task falls on the specified date.
     * For a Deadline, the task matches if its due date equals the given date.
     * For an Event, the task matches if the given date falls within its
     * start and end dates (inclusive). Other task types never match.
     *
     * @param task The task to check.
     * @param date The date to check against.
     * @return true if the task occurs on the given date, false otherwise.
     */
    private boolean isTaskOnDate(Task task, LocalDate date) {
        if (task instanceof Deadline deadline) {
            return deadline.getBy().toLocalDate().equals(date);
        }
        if (task instanceof Event event) {
            return !date.isBefore(event.getStart().toLocalDate())
                    && !date.isAfter(event.getEnd().toLocalDate());
        }
        return false;
    }

    /**
     * Finds tasks that contain the specified keyword in their description
     *
     * @param keyword The keyword to search for
     * @return ArrayList of tasks containing the keyword
     */
    public ArrayList<Task> findTasks(String keyword) {
        return tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Validates whether the specified index exists in tasks
     *
     * @param index The task index to validate
     * @throws SnowyException if the index is invalid
     */
    private void validateIndex(int index) throws SnowyException {
        if (index < 0 || index >= tasks.size()) {
            throw new SnowyException("Woof! That task number doesn't exist!");
        }
    }
}