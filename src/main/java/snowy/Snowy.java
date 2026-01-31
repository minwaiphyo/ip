package snowy;

import snowy.exception.SnowyException;
import snowy.parser.Parser;
import snowy.storage.Storage;
import snowy.task.Deadline;
import snowy.task.Event;
import snowy.task.Task;
import snowy.task.ToDo;
import snowy.tasklist.TaskList;
import snowy.ui.Ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


/**
 * Main class for the Snowy chatbot.
 * Snowy is a task manager that helps users keep track of todos, deadlines,
 * and events. It provides a seamless command-line interface for users to add, delete,
 * mark, and view tasks. Tasks are persisted to a file for future sessions.
 * This class coordinates the interaction between the UI, Storage, TaskList,
 * and Parser components.
 */
public class Snowy {
    private static final String FILEPATH = "data/tasks.txt";
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Snowy(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            storage.initializeFile();
            tasks = new TaskList(storage.load());
        } catch (SnowyException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main application loop.
     * Displays the welcome message, continuously reads and processes user commands,
     * and handles all exceptions appropriately until the user exits.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();

            try {
                String command = Parser.parseCommand(input);

                switch (command) {
                    case "bye":
                        ui.showGoodbye();
                        ui.close();
                        return;

                    case "list":
                        ui.showTaskList(tasks.getTasks());
                        break;

                    case "mark":
                        handleMark(input);
                        break;

                    case "unmark":
                        handleUnmark(input);
                        break;

                    case "todo":
                        handleTodo(input);
                        break;

                    case "deadline":
                        handleDeadline(input);
                        break;

                    case "event":
                        handleEvent(input);
                        break;

                    case "delete":
                        handleDelete(input);
                        break;

                    case "on":
                        handleOn(input);
                        break;

                    default:
                        throw new SnowyException("""
                            *Sad snowy.Snowy noises* I don't understand that command! Try 'todo', 'deadline', 'event', 'list', 'mark', 'unmark', 'delete', or 'on'!
                            """);
                }
            } catch (SnowyException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showError("Woof! Please provide a valid task number");
            } catch (ArrayIndexOutOfBoundsException e) {
                ui.showError("Woof woof! Something went wrong with parsing your command. Please check the format");
            } catch (DateTimeParseException e) {
                ui.showError("Woof! Invalid date format. Please use yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)");
            }
        }
    }

    /**
     * Handles the mark command by marking a task as completed.
     * Parses the task index, marks the task, saves to storage, and displays confirmation.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the task index is invalid or missing.
     */
    private void handleMark(String input) throws SnowyException {
        int taskIndex = Parser.parseTaskIndex(input, 5);
        tasks.markTask(taskIndex);
        storage.save(tasks.getTasks());
        ui.showTaskMarked(tasks.getTask(taskIndex));
    }

    /**
     * Handles the unmark command by marking a task as not completed.
     * Parses the task index, unmarks the task, saves to storage, and displays confirmation.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the task index is invalid or missing.
     */
    private void handleUnmark(String input) throws SnowyException {
        int taskIndex = Parser.parseTaskIndex(input, 7);
        tasks.unmarkTask(taskIndex);
        storage.save(tasks.getTasks());
        ui.showTaskUnmarked(tasks.getTask(taskIndex));
    }

    /**
     * Handles the todo command by creating and adding a new ToDo task.
     * Parses the description, creates the task, saves to storage, and displays confirmation.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the description is missing or empty.
     */
    private void handleTodo(String input) throws SnowyException {
        String description = Parser.parseTodoDescription(input);
        ToDo todo = new ToDo(description);
        tasks.addTask(todo);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(todo, tasks.size());
    }

    /**
     * Handles the deadline command by creating and adding a new Deadline task.
     * Parses the description and deadline datetime, creates the task, saves to storage,
     * and displays confirmation.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the format is invalid or any required information is missing.
     */
    private void handleDeadline(String input) throws SnowyException {
        String[] parts = Parser.parseDeadline(input);
        String description = parts[0];
        String byString = parts[1];
        LocalDateTime by = Parser.parseDateTime(byString);

        Deadline deadline = new Deadline(description, by);
        tasks.addTask(deadline);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(deadline, tasks.size());
    }

    /**
     * Handles the event command by creating and adding a new Event task.
     * Parses the description, start time, and end time, creates the task, saves to storage,
     * and displays confirmation.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the format is invalid or any required information is missing.
     */
    private void handleEvent(String input) throws SnowyException {
        String[] parts = Parser.parseEvent(input);
        String description = parts[0];
        String fromString = parts[1];
        String toString = parts[2];
        LocalDateTime from = Parser.parseDateTime(fromString);
        LocalDateTime to = Parser.parseDateTime(toString);

        Event event = new Event(description, from, to);
        tasks.addTask(event);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(event, tasks.size());
    }

    /**
     * Handles the delete command by removing a task from the list.
     * Parses the task index, removes the task, saves to storage, and displays confirmation.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the task index is invalid or missing.
     */
    private void handleDelete(String input) throws SnowyException {
        int taskIndex = Parser.parseTaskIndex(input, 7);
        Task removedTask = tasks.deleteTask(taskIndex);
        storage.save(tasks.getTasks());
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    /**
     * Handles the "on" command by finding and displaying all tasks on a specific date.
     * Parses the date, finds matching tasks, and displays them to the user.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the date format is invalid or missing.
     */
    private void handleOn(String input) throws SnowyException {
        String dateString = Parser.parseOnDate(input);
        LocalDate targetDate = Parser.parseDate(dateString);
        ArrayList<Task> matchingTasks = tasks.getTasksOnDate(targetDate);
        ui.showTasksOnDate(matchingTasks, targetDate);
    }

    /**
     * The main entry point for the Snowy application.
     * Creates a new Snowy instance with the default file path and starts the application.
     *
     * @param args Command line arguments (currently unused).
     */
    public static void main(String[] args) {
        new Snowy(FILEPATH).run();
    }
}