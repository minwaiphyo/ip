package snowy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading tasks from file and saving tasks to file
 */
public class Storage {
    private String filePath;

    /**
     * Creates a Storage object with the specified file path
     * @param filePath Path to the data file
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Initialize the data directory and file if they don't already exist
     * @throws SnowyException if there's an error creating the file
     */
    public void initializeFile() throws SnowyException {
        try {
            File directory = new File("data");
            if (!directory.exists()) {
                directory.mkdir();
            }

            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new SnowyException("Error initializing file: " + e.getMessage());
        }
    }

    /**
     * Load all tasks from the file
     * @return ArrayList of tasks
     * @throws SnowyException if there's an error loading tasks
     */
    public ArrayList<Task> load() throws SnowyException {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            File file = new File(filePath);
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
     * @throws SnowyException if there's an error saving tasks
     */
    public void save(ArrayList<Task> tasks) throws SnowyException {
        try {
            FileWriter writer = new FileWriter(filePath);

            for (Task task : tasks) {
                writer.write(taskToString(task) + "\n");
            }

            writer.close();
        } catch (IOException e) {
            throw new SnowyException("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Parse a line from the file into a Task object
     * Format: TaskType | isDone | description | [additional fields]
     * @param line Line from file
     * @return Task object or null if parse fails
     */
    private Task parseTask(String line) {
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
     * Converts a Task object into a saveable string
     * Format: TaskType | isDone | description | [additional fields]
     * @param task Task to convert
     * @return String representation for file
     */
    private String taskToString(Task task) {
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
}