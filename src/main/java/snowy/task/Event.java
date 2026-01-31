package snowy.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    protected LocalDateTime start;
    protected LocalDateTime end;

    /**
     * Creates a new Event task with the given description, start time, and end time.
     *
     * @param description The description of the event.
     * @param start The date and time when the event starts.
     * @param end The date and time when the event ends.
     */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString();
    }

    @Override
    public String printDetailed() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
        return this.toString() + " (from: " + this.start.format(formatter) + " to: " + this.end.format(formatter) + ")";
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }
}
