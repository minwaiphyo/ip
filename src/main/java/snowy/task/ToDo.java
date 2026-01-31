package snowy.task;

/**
 * Represents a simple todo task without any date or time constraints.
 * A ToDo task only has a description and a completion status.
 * Unlike {@link Deadline} and {@link Event} tasks, ToDo tasks do not have
 * any associated dates or time periods, making them suitable for general
 * tasks that can be completed at any time.
 *
 * <p>ToDo tasks are displayed with the [T] type indicator in the task list.</p>
 */
public class ToDo extends Task {

    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String printDetailed(){
        return this.toString();
    }
}
