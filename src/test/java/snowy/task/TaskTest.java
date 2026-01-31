package snowy.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

/**
 * JUnit tests for Task-related classes.
 */
public class TaskTest {

    @Test
    public void markAsDone_newTask_taskIsMarked() {
        Task task = new ToDo("Read book");
        task.markAsDone();

        assertTrue(task.isDone());
        assertEquals("X", task.getStatusIcon());
    }

    @Test
    public void markAsNotDone_doneTask_taskIsUnmarked() {
        Task task = new ToDo("Read book");
        task.markAsDone();
        task.markAsNotDone();

        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void todoToString_newTodo_correctFormat() {
        Task task = new ToDo("Read book");
        assertEquals("[T][ ]  Read book", task.toString());
    }

    @Test
    public void todoToString_doneTodo_correctFormat() {
        Task task = new ToDo("Read book");
        task.markAsDone();
        assertEquals("[T][X]  Read book", task.toString());
    }

    @Test
    public void deadlineToString_newDeadline_correctFormat() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 12, 25, 18, 0);
        Task task = new Deadline("Submit assignment", dateTime);

        assertEquals("[D][ ]  Submit assignment", task.toString());
    }

    @Test
    public void deadlinePrintDetailed_deadline_includesDate() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 12, 25, 18, 0);
        Deadline task = new Deadline("Submit assignment", dateTime);

        String expected = "[D][ ]  Submit assignment (by: Dec  25 2024 18:00)";
        assertEquals(expected, task.printDetailed());
    }

    @Test
    public void eventToString_newEvent_correctFormat() {
        LocalDateTime start = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 25, 12, 0);
        Task task = new Event("Team meeting", start, end);

        assertEquals("[E][ ]  Team meeting", task.toString());
    }

    @Test
    public void eventPrintDetailed_event_includesDates() {
        LocalDateTime start = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 25, 12, 0);
        Event task = new Event("Team meeting", start, end);

        String expected = "[E][ ]  Team meeting (from: Dec 25 2024 10:00 to: Dec 25 2024 12:00)";
        assertEquals(expected, task.printDetailed());
    }

    @Test
    public void getDescription_task_returnsCorrectDescription() {
        Task task = new ToDo("Read book");
        assertEquals("Read book", task.getDescription());
    }

    @Test
    public void getBy_deadline_returnsCorrectDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 12, 25, 18, 0);
        Deadline deadline = new Deadline("Submit assignment", dateTime);

        assertEquals(dateTime, deadline.getBy());
    }

    @Test
    public void getStartEnd_event_returnsCorrectDateTimes() {
        LocalDateTime start = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 25, 12, 0);
        Event event = new Event("Team meeting", start, end);

        assertEquals(start, event.getStart());
        assertEquals(end, event.getEnd());
    }
}