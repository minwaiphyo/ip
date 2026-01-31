package snowy.tasklist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import snowy.exception.SnowyException;
import snowy.task.Task;
import snowy.task.ToDo;
import snowy.task.Deadline;
import snowy.task.Event;

/**
 * JUnit tests for TaskList class.
 */
public class TaskListTest {
    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void addTask_singleTask_taskAddedSuccessfully() {
        Task task = new ToDo("Read book");
        taskList.addTask(task);
        assertEquals(1, taskList.size());
    }

    @Test
    public void addTask_multipleTasks_allTasksAdded() {
        taskList.addTask(new ToDo("Task 1"));
        taskList.addTask(new ToDo("Task 2"));
        taskList.addTask(new ToDo("Task 3"));
        assertEquals(3, taskList.size());
    }

    @Test
    public void deleteTask_validIndex_taskDeleted() throws SnowyException {
        Task task = new ToDo("Read book");
        taskList.addTask(task);
        Task deleted = taskList.deleteTask(0);

        assertEquals(task, deleted);
        assertEquals(0, taskList.size());
    }

    @Test
    public void deleteTask_invalidIndex_exceptionThrown() {
        taskList.addTask(new ToDo("Task 1"));

        assertThrows(SnowyException.class, () -> {
            taskList.deleteTask(5);
        });
    }

    @Test
    public void deleteTask_negativeIndex_exceptionThrown() {
        taskList.addTask(new ToDo("Task 1"));

        assertThrows(SnowyException.class, () -> {
            taskList.deleteTask(-1);
        });
    }

    @Test
    public void markTask_validIndex_taskMarked() throws SnowyException {
        Task task = new ToDo("Read book");
        taskList.addTask(task);
        taskList.markTask(0);

        assertTrue(taskList.getTask(0).isDone());
    }

    @Test
    public void markTask_invalidIndex_exceptionThrown() {
        taskList.addTask(new ToDo("Task 1"));

        assertThrows(SnowyException.class, () -> {
            taskList.markTask(10);
        });
    }

    @Test
    public void getTasksOnDate_deadlineOnDate_taskFound() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 12, 25, 18, 0);
        Deadline deadline = new Deadline("Submit assignment", dateTime);
        taskList.addTask(deadline);
        taskList.addTask(new ToDo("Random task"));

        LocalDate searchDate = LocalDate.of(2024, 12, 25);
        ArrayList<Task> result = taskList.getTasksOnDate(searchDate);

        assertEquals(1, result.size());
        assertEquals(deadline, result.get(0));
    }

    @Test
    public void getTasksOnDate_eventOnDate_taskFound() {
        LocalDateTime start = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 25, 12, 0);
        Event event = new Event("Team meeting", start, end);
        taskList.addTask(event);

        LocalDate searchDate = LocalDate.of(2024, 12, 25);
        ArrayList<Task> result = taskList.getTasksOnDate(searchDate);

        assertEquals(1, result.size());
        assertEquals(event, result.get(0));
    }

    @Test
    public void getTasksOnDate_noTasksOnDate_emptyListReturned() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 12, 25, 18, 0);
        taskList.addTask(new Deadline("Submit assignment", dateTime));

        LocalDate searchDate = LocalDate.of(2024, 12, 26);
        ArrayList<Task> result = taskList.getTasksOnDate(searchDate);

        assertEquals(0, result.size());
    }

    @Test
    public void getTasksOnDate_multipleTasksOnSameDate_allTasksFound() {
        LocalDateTime deadline1 = LocalDateTime.of(2024, 12, 25, 18, 0);
        LocalDateTime deadline2 = LocalDateTime.of(2024, 12, 25, 23, 59);

        taskList.addTask(new Deadline("Assignment 1", deadline1));
        taskList.addTask(new Deadline("Assignment 2", deadline2));
        taskList.addTask(new ToDo("Random task"));

        LocalDate searchDate = LocalDate.of(2024, 12, 25);
        ArrayList<Task> result = taskList.getTasksOnDate(searchDate);

        assertEquals(2, result.size());
    }
}