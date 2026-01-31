package snowy;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * Handles UI operations such as reading input and displaying output
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String INDENT = "     ";
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message & Snowy's ASCII art
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println("Woof woof! I'm snowy.Snowy!");
        System.out.println("  /^-----^\\");
        System.out.println(" V  o o  |");
        System.out.println("  |  Y  |");
        System.out.println("   \\ Q /");
        System.out.println("   / - \\");
        System.out.println("   |    \\");
        System.out.println("   |     \\     )");
        System.out.println("   || (___\\====");
        System.out.println("What can I do for you? :3");
        System.out.println(LINE);
    }

    /**
     * Displays the goodbye message
     */
    public void showGoodbye() {
        System.out.println(INDENT + LINE);
        System.out.println(INDENT + "Sad puppy noises* Bye... Hope to play with you again soon!");
        System.out.println(INDENT + LINE);
    }

    /**
     * Reads user's command
     * @return The user's input as a string
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the line separator
     */
    public void showLine() {
        System.out.println(INDENT + LINE);
    }

    /**
     * Displays an error message
     * @param message The error message to display
     */
    public void showError(String message) {
        System.out.println(INDENT + LINE);
        System.out.println(INDENT + message);
        System.out.println(INDENT + LINE);
    }

    /**
     * Displays a loading error message
     */
    public void showLoadingError() {
        showError("Woof! There was an error loading your tasks.");
    }

    /**
     * Displays the list of tasks
     * @param tasks The list of tasks to display
     */
    public void showTaskList(ArrayList<Task> tasks) {
        System.out.println(INDENT + LINE);
        System.out.println(INDENT + "Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println(INDENT + (i + 1) + "." + task.printDetailed());
        }
        System.out.println(INDENT + LINE);
    }

    /**
     * Displays a message when a task is marked as done
     * @param task The task that was marked
     */
    public void showTaskMarked(Task task) {
        System.out.println(INDENT + LINE);
        System.out.println(INDENT + "Nice! I've marked this task as done:");
        System.out.println(INDENT + task.printDetailed());
        System.out.println(INDENT + LINE);
    }

    /**
     * Displays a message when a task is unmarked
     * @param task The task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(INDENT + LINE);
        System.out.println(INDENT + "Ok, I've marked this task as not done yet:");
        System.out.println(INDENT + task.printDetailed());
        System.out.println(INDENT + LINE);
    }

    /**
     * Displays a message when a task is added
     * @param task The task that was added
     * @param taskCount The total number of tasks in the list
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(INDENT + LINE);
        System.out.println(INDENT + "Got it. I've added this task:");
        System.out.println(INDENT + task.printDetailed());
        System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
        System.out.println(INDENT + LINE);
    }

    /**
     * Displays a message when a task is deleted
     * @param task The task that was deleted
     * @param taskCount The total number of tasks remaining in the list
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(INDENT + LINE);
        System.out.println(INDENT + "Noted. I've removed this task:");
        System.out.println(INDENT + task.printDetailed());
        System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
        System.out.println(INDENT + LINE);
    }

    /**
     * Displays tasks occurring on a specific date to handle "on" command
     * @param matchingTasks The list of tasks on that date
     * @param date The target date to display
     */
    public void showTasksOnDate(ArrayList<Task> matchingTasks, java.time.LocalDate date) {
        System.out.println(INDENT + LINE);
        if (matchingTasks.isEmpty()) {
            System.out.println(INDENT + "No tasks found on " + date.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd yyyy")));
        } else {
            System.out.println(INDENT + "Tasks on " + date.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(INDENT + (i + 1) + "." + matchingTasks.get(i).printDetailed());
            }
        }
        System.out.println(INDENT + LINE);
    }

    /**
     * Closes the scanners
     */
    public void close() {
        scanner.close();
    }
}