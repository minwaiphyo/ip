package snowy.task;


/**
 * Represents a task with a description and completion status.
 * This is the base class for all task types in the Snowy chatbot.
 */
public class Task {
    protected String description;
    protected  boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "]  " + description;
    }

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
