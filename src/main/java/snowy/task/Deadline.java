package snowy.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a specific deadline date and time.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Creates a new Deadline task with the given description and deadline date/time.
     *
     * @param description The description of the deadline task.
     * @param by          The date and time by which this task must be completed.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString();
    }

    @Override
    public String printDetailed() {
        return this + " (by: " + by.format(DateTimeFormatter.ofPattern("MMM  d yyyy HH:mm")) + ")";
    }

    @Override
    public LocalDateTime getDate() {
        return this.by;
    }

    public LocalDateTime getBy() {
        return this.by;
    }
}

