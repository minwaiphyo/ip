public class Deadline extends Task {
    protected String end;

    public Deadline(String description, String end) {
        super(description);
        this.end = end;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString();
    }


    @Override
    public String printDetailed() {
        return this.toString() + " (by: " + this.end + ")";
    }
}
