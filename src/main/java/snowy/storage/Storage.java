package snowy.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import snowy.exception.SnowyException;
import snowy.task.Deadline;
import snowy.task.Event;
import snowy.task.Task;
import snowy.task.ToDo;



/**
 * Handles data maintenance and manipulation for the Snowy chatbot.
 * This class manages loading tasks from a file and saving tasks to a file,
 * including parsing task data from the storage format and converting task
 * objects into a saveable string format. It abstracts all file I/O operations
 * from the business logic of the chatbot.
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
     * Initializes the data directory and file if they don't already exist
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
     * Loads all tasks from the file
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
     * Saves all tasks to the file
     * @param tasks ArrayList of tasks to save
     * @throws SnowyException if there's an error saving tasks
     */
    public void save(ArrayList<Task> tasks) throws SnowyException {
        assert tasks != null : "Task list to save should not be null";
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
     * Parses a line from the file into a Task object
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
                default:
                    // Unknown task type in file — corrupted or unsupported data, skip this entry
                    return null;
            }

            if (task != null && isDone) {
                task.markAsDone();
                assert task.isDone() : "Task should be marked done after markAsDone()";
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
        assert task != null : "Cannot convert null task to string";
        String isDone = task.isDone() ? "1" : "0";

        if (task instanceof ToDo) {
            return "T | " + isDone + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D | " + isDone + " | " + task.getDescription() + " | " + deadline.getBy().toString();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return "E | " + isDone + " | " + task.getDescription() + " | " + event.getStart().toString() + " | " + event.getEnd().toString();
        }
        return "";
    }
}