package snowy.task;


import java.time.LocalDateTime;

/**
 * Represents a task with a description and completion status.
 * This is the base class for all task types in the Snowy chatbot.
 */
public class Task {
    private String description;
    private  boolean isDone;

    /**
     * Creates a new Task with the given description.
     * The task is initially marked as not done.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        assert description != null && !description.isBlank()
                : "Task description must not be null or blank";
        this.description = description;
        this.isDone = false;
    }


    /**
     * Returns the status icon representing whether the task is completed.
     * Returns "X" for completed tasks and a space " " for incomplete tasks.
     *
     * @return "X" if the task is done, " " (space) otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }
    
    public LocalDateTime getDate() {
        return null;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "]  " + description;
    }


    /**
     * Returns a detailed string representation of the task.
     * For the base Task class, this is the same as toString().
     * Subclasses may override this to include additional details like dates.
     *
     * @return A detailed string representation of the task.
     */
    public String printDetailed() {
        return this.toString();
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }
}
