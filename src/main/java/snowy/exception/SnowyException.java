package snowy.exception;

/**
 * Represents exceptions specific to the Snowy application.
 * This exception is thrown when there are errors in user input,
 * task operations, or file I/O operations within Snowy.
 */
public class SnowyException extends RuntimeException {
    public SnowyException(String message) {
        super(message);
    }
}
