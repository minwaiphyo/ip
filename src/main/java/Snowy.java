import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Snowy {
    private static final String FILEPATH = "data/tasks.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize file and directory
        initializeFile();

        String LINE = "____________________________________________________________";
        String INDENT = "     ";

        // Greets user
        System.out.println(LINE);
        System.out.println("Woof woof! I'm Snowy!");
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

        while (true) {
            String input = scanner.nextLine();

            try {
                // User terminating
                if (input.equals("bye")) {
                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Sad puppy noises* Bye... Hope to play with you again soon!");
                    System.out.println(INDENT + LINE);
                    break;
                }

                // Lists out tasks
                if (input.trim().equals("list")) {
                    ArrayList<Task> tasks = loadTasks(FILEPATH);
                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        Task task = tasks.get(i);
                        System.out.println(INDENT + (i + 1) + "." + task.printDetailed());
                    }
                    System.out.println(INDENT + LINE);

                    // Unmark task
                } else if (input.startsWith("unmark ") || input.equals("unmark")) {

                    // Handles empty taskIndex
                    if (input.length() <= 7) {
                        throw new SnowyException("Woof woof! Please specify the number of the task to unmark!");
                    }

                    int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                    ArrayList<Task> tasks = loadTasks(FILEPATH);

                    // Handles invalid taskIndex
                    if (taskIndex < 0 || taskIndex >= tasks.size()) {
                        throw new SnowyException("Woof woof! That number doesn't exist!");
                    }

                    tasks.get(taskIndex).markAsNotDone();
                    saveTasks(tasks, FILEPATH);

                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Ok, I've marked this task as not done yet:");
                    System.out.println(INDENT + tasks.get(taskIndex).printDetailed());
                    System.out.println(INDENT + LINE);


                    // Mark task
                } else if (input.startsWith("mark ") || input.equals("mark")) {

                    // Handles empty taskIndex
                    if (input.length() < 6) {
                        throw new SnowyException("Woof! Please specify the number of the task to mark!");
                    }

                    int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                    ArrayList<Task> tasks = loadTasks(FILEPATH);

                    // Handles invalid taskIndex
                    if (taskIndex < 0 || taskIndex >= tasks.size()) {
                        throw new SnowyException("Woof woof! That task number doesn't exist!");
                    }

                    tasks.get(taskIndex).markAsDone();
                    saveTasks(tasks, FILEPATH);

                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Nice! I've marked this task as done:");
                    System.out.println(INDENT + tasks.get(taskIndex).printDetailed());
                    System.out.println(INDENT + LINE);


                    // Creates todo
                } else if (input.startsWith("todo ")) {
                    String description = input.substring(5).trim();
                    if (description.isEmpty()) {
                        throw new SnowyException("Woof woof! The description of a ToDo cannot be empty!");
                    }

                    ToDo todo = new ToDo(description);
                    ArrayList<Task> tasks = loadTasks(FILEPATH);
                    tasks.add(todo);
                    saveTasks(tasks, FILEPATH);

                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Got it. I've added this task:");
                    System.out.println(INDENT + todo);
                    System.out.println(INDENT + "Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(INDENT + LINE);
                } else if (input.equals("todo")) {
                    throw new SnowyException("Woof woof! The description of a ToDo cannot be empty!");


                    // Creates deadline
                } else if (input.startsWith("deadline "))  {
                    String details = input.substring(9).trim();

                    // Handles empty details
                    if (details.isEmpty()) {
                        throw new SnowyException("Woof! The description of a deadline cannot be empty!");
                    }
                    if (!details.contains(" /by ")) {
                        throw new SnowyException("Woof woof! Please use the format: deadline [task] /by [yyyy-MM-dd HHmm]");
                    }

                    String[] parts = details.split(" /by ");

                    if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                        throw new SnowyException("Woof! Both the description and deadline are required!");
                    }

                    String description = parts[0];
                    String byString = parts[1].trim();
                    LocalDateTime by = parseDateTime(byString);
                    Deadline deadline = new Deadline(description, by);

                    ArrayList<Task> tasks = loadTasks(FILEPATH);
                    tasks.add(deadline);
                    saveTasks(tasks, FILEPATH);

                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Got it. I've added this task:");
                    System.out.println(INDENT + deadline.printDetailed());
                    System.out.println(INDENT + "Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(INDENT + LINE);
                } else if (input.equals("deadline")){
                    throw new SnowyException("Woof! The description of a deadline cannot be empty!");

                    // Creates event
                } else if (input.startsWith("event ")) {
                    String details = input.substring(6);

                    if (details.isEmpty()) {
                        throw new SnowyException("Woof! The description of an event cannot be empty!");
                    }
                    if (!details.contains(" /from ") || !details.contains(" /to ")) {
                        throw new SnowyException("Woof woof! Please use the format: event [task] /from [yyyy-MM-dd HHmm] /to [yyyy-MM-dd HHmm]");
                    }

                    String[] parts = details.split(" /from | /to ");

                    if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
                        throw new SnowyException("Woof! Description, start time, and end time are all required!");
                    }

                    String description = parts[0];
                    String fromString = parts[1].trim();
                    String toString = parts[2].trim();
                    LocalDateTime from = parseDateTime(fromString);
                    LocalDateTime to = parseDateTime(toString);

                    Event event = new Event(description, from, to);

                    ArrayList<Task> tasks = loadTasks(FILEPATH);
                    tasks.add(event);
                    saveTasks(tasks, FILEPATH);

                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Got it. I've added this task:");
                    System.out.println(INDENT + event.printDetailed());
                    System.out.println(INDENT + "Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(INDENT + LINE);


                } else if (input.equals("event")) {
                    throw new SnowyException("Woof woof! The description of an event cannot be empty!");

                    // Deletes task
                } else if (input.startsWith("delete ") || input.equals("delete")) {

                    // Handles empty taskIndex
                    if (input.length() <= 7) {
                        throw new SnowyException("Woof woof! Please specify the index  of the task to delete!");
                    }

                    int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                    ArrayList<Task> tasks = loadTasks(FILEPATH);

                    // Handles invalid taskIndex
                    if (taskIndex < 0 || taskIndex >= tasks.size()) {
                        throw new SnowyException("Woof woof! That number doesn't exist!");
                    }

                    Task task = tasks.get(taskIndex);
                    tasks.remove(taskIndex);
                    saveTasks(tasks, FILEPATH);

                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Noted. I've removed this task:");
                    System.out.println(INDENT + task.printDetailed());
                    System.out.println(INDENT + "Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(INDENT + LINE);


                // Strech goal: "on" command - print deadlines/events occurring on a specific date
                } else if (input.startsWith("on ")) {
                    String dateString = input.substring(3).trim();

                    if (dateString.isEmpty()) {
                        throw new SnowyException("Woof! Please specify a date in date in yyyy-MM-dd format!");
                    }

                    LocalDate targetDate = parseDate(dateString);
                    ArrayList<Task> tasks = loadTasks(FILEPATH);
                    ArrayList<Task> matchingTasks = new ArrayList<>();

                    for (Task task : tasks) {
                        if (task instanceof Deadline deadline) {
                            if (deadline.getBy().toLocalDate().equals(targetDate)) {
                                matchingTasks.add(task);
                            }
                        } else if (task instanceof Event event) {
                            LocalDate fromDate = event.getStart().toLocalDate();
                            LocalDate toDate = event.getEnd().toLocalDate();
                            if (!targetDate.isBefore(fromDate) && !targetDate.isAfter(toDate)) {
                                matchingTasks.add(task);
                            }
                        }
                    }

                    System.out.println(INDENT + LINE);
                    if (matchingTasks.isEmpty()) {
                        System.out.println(INDENT + "No tasks found on " + targetDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")));
                    } else {
                        System.out.println(INDENT + "Tasks on " + targetDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");
                        for (int i = 0; i < matchingTasks.size(); i++) {
                            System.out.println(INDENT + (i + 1) + "." + matchingTasks.get(i).printDetailed());
                        }
                    }
                    System.out.println(INDENT + LINE);
                } else if (input.equals("on")){
                    throw new SnowyException("Woof! Please specify a date in yyyy-MM-dd format!");
                } else {
                    throw new SnowyException("""
                        *Sad Snowy noises* I don't understand that command! Try 'todo', 'deadline', 'event', 'list', 'mark', 'unmark', 'delete', or 'on'!
                        """);
                }
            } catch(SnowyException e) {
                System.out.println(INDENT + LINE);
                System.out.println(INDENT + e.getMessage());
                System.out.println(INDENT + LINE);
            } catch (NumberFormatException e) {
                System.out.println(INDENT + LINE);
                System.out.println(INDENT + "Woof! Please provide a valid task number");
                System.out.println(INDENT + LINE);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(INDENT + LINE);
                System.out.println(INDENT + "Woof woof! Something went wrong with parsing your command. Please check the format");
                System.out.println(INDENT + LINE);
            } catch (DateTimeParseException e) {
                System.out.println(INDENT + LINE);
                System.out.println(INDENT + "Woof! Invalid date format. Please use yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)");
            }
        }
        scanner.close();
    }

    /**
     * Initialize the data directory and file if they don't exist
     */
    private static void initializeFile() {
        try {
            File directory = new File("data");
            if (!directory.exists()) {
                directory.mkdir();
            }

            File file = new File(FILEPATH);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error initializing file: " + e.getMessage());
        }
    }

    /**
     * Load all tasks from the file
     * @param filepath Path to the tasks file
     * @return ArrayList of tasks
     */
    private static ArrayList<Task> loadTasks(String filepath) {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            File file = new File(filepath);
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            fileScanner.close();

        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return empty list
        }

        return tasks;
    }

    /**
     * Save all tasks to the file
     * @param tasks ArrayList of tasks to save
     * @param filepath Path to the tasks file
     */
    private static void saveTasks(ArrayList<Task> tasks, String filepath) {
        try {
            FileWriter writer = new FileWriter(filepath);

            for (Task task : tasks) {
                writer.write(taskToString(task) + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Parse a line from the file into a Task object
     * Format: TaskType | isDone | description | [additional fields]
     * @param line Line from file
     * @return Task object or null if parse fails
     */
    private static Task parseTask(String line) {
        try {
            String[] parts = line.split(" \\| ");

            if (parts.length < 3) {
                return null;
            }

            String taskType = parts[0];
            boolean isDone = parts[1].equals("1");
            String description = parts[2];

            Task task = null;

            switch (taskType) {
                case "T":
                    task = new ToDo(description);
                    break;
                case "D":
                    if (parts.length >= 4) {
                        LocalDateTime by = LocalDateTime.parse(parts[3]);
                        task = new Deadline(description, by);
                    }
                    break;
                case "E":
                    if (parts.length >= 5) {
                        LocalDateTime from = LocalDateTime.parse(parts[3]);
                        LocalDateTime to = LocalDateTime.parse(parts[4]);
                        task = new Event(description, from, to);
                    }
                    break;
            }

            if (task != null && isDone) {
                task.markAsDone();
            }

            return task;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Convert a Task object into a saveable string
     * Format: TaskType | isDone | description | [additional fields]
     * @param task Task to convert
     * @return String representation for file
     */
    private static String taskToString(Task task) {
        String isDone = task.isDone ? "1" : "0";

        if (task instanceof ToDo) {
            return "T | " + isDone + " | " + task.description;
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D | " + isDone + " | " + task.description + " | " + deadline.getBy().toString();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return "E | " + isDone + " | " + task.description + " | " + event.getStart().toString() + " | " + event.getEnd().toString();
        }
        return "";
    }

    /**
     * Parse a date-time string into LocalDateTime
     * Accepts format: yyyy-MM-dd HHmm
     * @param dateTimeString The date-time string to parse
     * @return LocalDateTime object
     */
    private static LocalDateTime parseDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    /**
     * Parse a date string into LocalDate
     * Accepts format: yyyy-MM-dd
     * @param dateString The date string to parse
     * @return LocalDate object
     */
    private static LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }


}


