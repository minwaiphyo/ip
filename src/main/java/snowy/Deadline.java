package snowy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected LocalDateTime by;

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
        return this.toString() + " (by: " + by.format(DateTimeFormatter.ofPattern("MMM  d yyyy HH:mm")) + ")";
    }

    public LocalDateTime getBy() {
        return this.by;
    }
}

