package snowy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import snowy.exception.SnowyException;
import snowy.parser.Parser;
import snowy.storage.Storage;
import snowy.task.Deadline;
import snowy.task.Event;
import snowy.task.Task;
import snowy.task.ToDo;
import snowy.tasklist.TaskList;

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
    private static final DateTimeFormatter DATE_DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM DD YYYY");
    private Storage storage;
    private TaskList tasks;

    private static final int MARK_CMD_LENGTH = 5;    // "mark "
    private static final int UNMARK_CMD_LENGTH = 7;  // "unmark "
    private static final int DELETE_CMD_LENGTH = 7;  // "delete "

    /**
     * Creates a new Snowy instance with default file path.
     */
    public Snowy() {
        this(FILEPATH);
    }

    /**
     * Creates a new Snowy instance with the specified file path.
     * Initializes storage at the given location, loads existing tasks from file,
     * and creates an empty task list if loading fails.
     *
     * @param filePath The path to the data file for storing tasks.
     */
    public Snowy(String filePath) {

        storage = new Storage(filePath);
        try {
            storage.initializeFile();
            tasks = new TaskList(storage.load());
        } catch (SnowyException e) {
            tasks = new TaskList();
        }
    }


    /**
     * Handles the mark command by marking a task as completed.
     * Parses the task index, marks the task, saves to storage, and displays confirmation.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the task index is invalid or missing.
     */
    private String handleMark(String input) throws SnowyException {
        int taskIndex = Parser.parseTaskIndex(input, MARK_CMD_LENGTH);
        tasks.markTask(taskIndex);
        storage.save(tasks.getTasks());
        return "Nice! I've marked this task as done:\n" + tasks.getTask(taskIndex).printDetailed();
    }

    /**
     * Handles the unmark command by marking a task as not completed.
     * Parses the task index, unmarks the task, saves to storage, and displays confirmation.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the task index is invalid or missing.
     */
    private String handleUnmark(String input) throws SnowyException {
        int taskIndex = Parser.parseTaskIndex(input, UNMARK_CMD_LENGTH);
        tasks.unmarkTask(taskIndex);
        storage.save(tasks.getTasks());
        return "Ok, I've marked this task as not done yet:\n" + tasks.getTask(taskIndex).printDetailed();
    }

    /**
     * Handles the todo command by creating and adding a new ToDo task.
     * Parses the description, creates the task, saves to storage, and displays confirmation.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the description is missing or empty.
     */
    private String handleTodo(String input) throws SnowyException {
        String description = Parser.parseTodoDescription(input);
        Task task = new ToDo(description);
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        return "Got it. I've added this task:\n" + task.printDetailed() + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the deadline command by creating and adding a new Deadline task.
     * Parses the description and deadline datetime, creates the task, saves to storage,
     * and displays confirmation.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the format is invalid or any required information is missing.
     */
    private String handleDeadline(String input) throws SnowyException {
        try {
            // Parse command into components
            String[] parts = Parser.parseDeadline(input);
            LocalDateTime by = Parser.parseDateTime(parts[1]);

            // Construct and store the tasks
            Task task = new Deadline(parts[0], by);
            tasks.addTask(task);
            storage.save(tasks.getTasks());

            return "Got it. I've added this task:\n" + task.printDetailed() + "\n"
                    + "Now you have " + tasks.size() + " tasks in the list.";
        } catch (DateTimeParseException e) {
            throw new SnowyException("Woof! Please use the format: yyyy-MM-dd HHmm");
        }
    }

    /**
     * Handles the event command by creating and adding a new Event task.
     * Parses the description, start time, and end time, creates the task, saves to storage,
     * and displays confirmation.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the format is invalid or any required information is missing.
     */
    private String handleEvent(String input) throws SnowyException {
        try {
            // Parse command intro components
            String[] parts = Parser.parseEvent(input);
            LocalDateTime from = Parser.parseDateTime(parts[1]);
            LocalDateTime to = Parser.parseDateTime(parts[2]);

            // Construct and store the task
            Task task = new Event(parts[0], from, to);
            tasks.addTask(task);
            storage.save(tasks.getTasks());
            return "Got it. I've added this task:\n" + task.printDetailed() + "\n"
                    + "Now you have " + tasks.size() + " tasks in the list.";
        } catch (DateTimeParseException e) {
            throw new SnowyException("Woof! Please use the format: yyyy-MM-dd HHmm");
        }
    }

    /**
     * Handles the delete command by removing a task from the list.
     * Parses the task index, removes the task, saves to storage, and displays confirmation.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the task index is invalid or missing.
     */
    private String handleDelete(String input) throws SnowyException {
        int taskIndex = Parser.parseTaskIndex(input, DELETE_CMD_LENGTH);
        Task removedTask = tasks.deleteTask(taskIndex);
        storage.save(tasks.getTasks());
        return "Noted. I've removed this task:\n" + removedTask.printDetailed() + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the "on" command by finding and displaying all tasks on a specific date.
     * Parses the date, finds matching tasks, and displays them to the user.
     *
     * @param input The complete user input string.
     * @throws SnowyException If the date format is invalid or missing.
     */
    private String handleOn(String input) throws SnowyException {
        try {
            String dateString = Parser.parseOnDate(input);
            LocalDate date = Parser.parseDate(dateString);
            ArrayList<Task> matchingTasks = tasks.getTasksOnDate(date);

            if (matchingTasks.isEmpty()) {
                return "No tasks found on " + date.format(DATE_DISPLAY_FORMAT);
            }

            StringBuilder result = new StringBuilder("Tasks on ")
                    .append(date.format(DATE_DISPLAY_FORMAT))
                    .append(":\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                result.append((i + 1)).append(". ").append(matchingTasks.get(i).printDetailed()).append("\n");
            }
            return result.toString().trim();
        } catch (DateTimeParseException e) {
            throw new SnowyException("Woof! Please use the format: " + DATE_DISPLAY_FORMAT);
        }

    }

    /**
     * Handles the find command
     *
     * @param input The full user input
     * @throws SnowyException If there's an error processing the search
     */
    private String handleFind(String input) throws SnowyException {
        String keyword = Parser.parseFindKeyword(input);
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        if (matchingTasks.isEmpty()) {
            return "Woof! No matching tasks found in your list.";
        }

        StringBuilder result = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            result.append((i + 1)).append(". ").append(matchingTasks.get(i).printDetailed()).append("\n");
        }
        return result.toString().trim();
    }

    private String formatTaskList() {
        if (tasks.size() == 0) {
            return "Woof! Your task list is empty!";
        }
        StringBuilder result = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            result.append((i + 1)).append(". ").append(tasks.getTask(i).printDetailed()).append("\n");
        }
        return result.toString().trim();
    }

    public String getWelcome() {
        return "Woof woof! I'm Snowy!\n";
    }

    public String getResponse(String input) {
        try {
            String command = Parser.parseCommand(input);
            assert command != null && !command.isEmpty() : "Parsed command should never be null or empty";
            switch (command) {
                case "bye":
                    return "Sad puppy noises* Bye... Hope to play with you again soon!";

                case "list":
                    return formatTaskList();

                case "mark":
                    return handleMark(input);

                case "unmark":
                    return handleUnmark(input);

                case "todo":
                    return handleTodo(input);

                case "deadline":
                    return handleDeadline(input);

                case "event":
                    return handleEvent(input);

                case "delete":
                    return handleDelete(input);

                case "on":
                    return handleOn(input);

                case "find":
                    return handleFind(input);

                default:
                    return "Woof! I don't understand that command. :(";
            }
        } catch (SnowyException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Woof! Something went wrong: " + e.getMessage();
        }
    }

}