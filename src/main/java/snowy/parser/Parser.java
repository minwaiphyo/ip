package snowy.parser;

import snowy.exception.SnowyException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Parses user commands and extracts relevant information.
 * This class contains static methods to interpret user input strings and
 * extract command types, task indices, descriptions, dates, and other
 * parameters needed by the application. It centralizes all parsing logic
 * and input validation related to command syntax.
 */
public class Parser {

    /**
     * Parses user input and returns the command type
     * @param fullCommand The full user input
     * @return The command word
     */
    public static String parseCommand(String fullCommand) {
        String trimmed = fullCommand.trim();

        if (trimmed.equals("bye") || trimmed.equals("list")) {
            return trimmed;
        }

        // Extract first word as command
        int spaceIndex = trimmed.indexOf(' ');
        if (spaceIndex == -1) {
            return trimmed; // Single word command
        }
        String command = trimmed.substring(0, spaceIndex);
        assert !command.isEmpty() : "Parse command should never be empty";

        return command;
    }

    /**
     * Parses the task index from mark/unmark/delete commands
     * @param fullCommand The full user input
     * @param commandLength The length of the command word + 1 (for the space)
     * @return The task index (0-based)
     * @throws SnowyException if index is missing or invalid
     */
    public static int parseTaskIndex(String fullCommand, int commandLength) throws SnowyException {
        if (fullCommand.length() <= commandLength) {
            throw new SnowyException("Woof! Please specify the number of the task!");
        }

        try {
            int index = Integer.parseInt(fullCommand.substring(commandLength).trim()) - 1;
            assert index >= 0 : "Task index should be non-negative after conversion: " + index;
            return index;
        } catch (NumberFormatException e) {
            throw new SnowyException("Woof! Please provide a valid task number!");
        }
    }

    /**
     * Parses the description from a todo command
     * @param fullCommand The full user input
     * @return The task description
     * @throws SnowyException if description is empty
     */
    public static String parseTodoDescription(String fullCommand) throws SnowyException {
        if (fullCommand.trim().equals("todo")) {
            throw new SnowyException("Woof woof! The description of a snowy.task.ToDo cannot be empty!");
        }

        String description = fullCommand.substring(5).trim();
        if (description.isEmpty()) {
            throw new SnowyException("Woof woof! The description of a snowy.task.ToDo cannot be empty!");
        }

        assert !description.isEmpty() : "Todo description should not be empty at this point";
        assert description.equals(description.trim()) : "Description should already be trimmed";

        return description;
    }

    /**
     * Parses a deadline command and returns the description and deadline
     * @param fullCommand The full user input
     * @return String array: [description, byString]
     * @throws SnowyException if format is invalid
     */
    public static String[] parseDeadline(String fullCommand) throws SnowyException {
        if (fullCommand.trim().equals("deadline")) {
            throw new SnowyException("Woof! The description of a deadline cannot be empty!");
        }

        String details = fullCommand.substring(9).trim();

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

        assert parts.length == 2: "Deadline split should produce exactly 2 parts";
        assert !parts[0].trim().isEmpty() : "Deadline description should not be empty";
        assert !parts[1].trim().isEmpty() : "Deadline 'by' field should not be empty";

        return new String[]{parts[0], parts[1].trim()};
    }

    /**
     * Parses an event command and returns the description, start time, and end time
     * @param fullCommand The full user input
     * @return String array: [description, fromString, toString]
     * @throws SnowyException if format is invalid
     */
    public static String[] parseEvent(String fullCommand) throws SnowyException {
        if (fullCommand.trim().equals("event")) {
            throw new SnowyException("Woof! The description of an event cannot be empty!");
        }

        String details = fullCommand.substring(6);

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

        assert parts.length == 3 : "Event split should produce exactly 3 parts";

        return new String[]{parts[0], parts[1].trim(), parts[2].trim()};
    }

    /**
     * Parses the date from an "on" command
     * @param fullCommand The full user input
     * @return The date string
     * @throws SnowyException if date is missing
     */
    public static String parseOnDate(String fullCommand) throws SnowyException {
        if (fullCommand.trim().equals("on")) {
            throw new SnowyException("Woof! Please specify a date in yyyy-MM-dd format!");
        }

        String dateString = fullCommand.substring(3).trim();

        if (dateString.isEmpty()) {
            throw new SnowyException("Woof! Please specify a date in date in yyyy-MM-dd format!");
        }

        return dateString;
    }

    /**
     * Parses a date-time string into LocalDateTime
     * Accepts format: yyyy-MM-dd HHmm
     * @param dateTimeString The date-time string to parse
     * @return LocalDateTime object
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    /**
     * Parses a date string into LocalDate
     * Accepts format: yyyy-MM-dd
     * @param dateString The date string to parse
     * @return LocalDate object
     */
    public static LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * Parses the keyword from a "find" command
     *
     * @param fullCommand The full user input
     * @return The search keyword
     * @throws SnowyException If keyword is missing
     */
    public static String parseFindKeyword(String fullCommand) throws SnowyException {
        if (fullCommand.trim().equals("find")) {
            throw new SnowyException("Woof! Please specify a keyword to search for!");
        }

        String keyword = fullCommand.substring(5).trim();

        if (keyword.isEmpty()) {
            throw new SnowyException("Woof! Please specify a keyword to search for!");
        }

        return keyword;
    }
}