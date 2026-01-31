package snowy.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;

import snowy.exception.SnowyException;

/**
 * JUnit tests for Parser class.
 */
public class ParserTest {

    @Test
    public void parseCommand_byeCommand_returnsbye() {
        assertEquals("bye", Parser.parseCommand("bye"));
    }

    @Test
    public void parseCommand_listCommand_returnslist() {
        assertEquals("list", Parser.parseCommand("list"));
    }

    @Test
    public void parseCommand_todoCommand_returnstodo() {
        assertEquals("todo", Parser.parseCommand("todo read book"));
    }

    @Test
    public void parseCommand_commandWithExtraSpaces_returnsCorrectCommand() {
        assertEquals("deadline", Parser.parseCommand("deadline   finish assignment /by 2024-12-25 1800"));
    }

    @Test
    public void parseTodoDescription_validDescription_returnsDescription() throws SnowyException {
        assertEquals("read book", Parser.parseTodoDescription("todo read book"));
    }

    @Test
    public void parseTodoDescription_emptyDescription_throwsException() {
        assertThrows(SnowyException.class, () -> {
            Parser.parseTodoDescription("todo");
        });
    }

    @Test
    public void parseTodoDescription_onlySpaces_throwsException() {
        assertThrows(SnowyException.class, () -> {
            Parser.parseTodoDescription("todo    ");
        });
    }

    @Test
    public void parseDeadline_validFormat_returnsDescriptionAndDate() throws SnowyException {
        String[] result = Parser.parseDeadline("deadline submit assignment /by 2024-12-25 1800");

        assertEquals(2, result.length);
        assertEquals("submit assignment", result[0]);
        assertEquals("2024-12-25 1800", result[1]);
    }

    @Test
    public void parseDeadline_missingBy_throwsException() {
        assertThrows(SnowyException.class, () -> {
            Parser.parseDeadline("deadline submit assignment 2024-12-25 1800");
        });
    }

    @Test
    public void parseDeadline_emptyDescription_throwsException() {
        assertThrows(SnowyException.class, () -> {
            Parser.parseDeadline("deadline");
        });
    }

    @Test

    public void parseDeadline_emptyDate_throwsException() {
        assertThrows(SnowyException.class, () -> {
            Parser.parseDeadline("deadline submit assignment /by ");
        });
    }

    @Test
    public void parseEvent_validFormat_returnsDescriptionAndDates() throws SnowyException {
        String[] result = Parser.parseEvent("event team meeting /from 2024-12-25 1000 /to 2024-12-25 1200");

        assertEquals(3, result.length);
        assertEquals("team meeting", result[0]);
        assertEquals("2024-12-25 1000", result[1]);
        assertEquals("2024-12-25 1200", result[2]);
    }

    @Test
    public void parseEvent_missingFrom_throwsException() {
        assertThrows(SnowyException.class, () -> {
            Parser.parseEvent("event meeting 2024-12-25 1000 /to 2024-12-25 1200");
        });
    }

    @Test
    public void parseEvent_missingTo_throwsException() {
        assertThrows(SnowyException.class, () -> {
            Parser.parseEvent("event meeting /from 2024-12-25 1000");
        });
    }

    @Test
    public void parseTaskIndex_validIndex_returnsZeroBasedIndex() throws SnowyException {
        assertEquals(0, Parser.parseTaskIndex("mark 1", 5));
        assertEquals(4, Parser.parseTaskIndex("delete 5", 7));
    }

    @Test
    public void parseTaskIndex_missingIndex_throwsException() {
        assertThrows(SnowyException.class, () -> {
            Parser.parseTaskIndex("mark ", 5);
        });
    }

    @Test
    public void parseTaskIndex_invalidIndex_throwsException() {
        assertThrows(SnowyException.class, () -> {
            Parser.parseTaskIndex("mark abc", 5);
        });
    }

    @Test
    public void parseDateTime_validFormat_returnsLocalDateTime() {
        LocalDateTime expected = LocalDateTime.of(2024, 12, 25, 18, 0);
        LocalDateTime actual = Parser.parseDateTime("2024-12-25 1800");

        assertEquals(expected, actual);
    }

    @Test
    public void parseDate_validFormat_returnsLocalDate() {
        LocalDate expected = LocalDate.of(2024, 12, 25);
        LocalDate actual = Parser.parseDate("2024-12-25");

        assertEquals(expected, actual);
    }

    @Test
    public void parseOnDate_validFormat_returnsDateString() throws SnowyException {
        assertEquals("2024-12-25", Parser.parseOnDate("on 2024-12-25"));
    }

    @Test
    public void parseOnDate_missingDate_throwsException() {
        assertThrows(SnowyException.class, () -> {
            Parser.parseOnDate("on");
        });
    }
}