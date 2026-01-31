package snowy.exception;

/**
 * Represents exceptions specific to the Snowy chatbot.
 * This exception is thrown when there are errors in user input,
 * task operations, or file I/O operations within Snowy.
 */
public class SnowyException extends RuntimeException {
    /**
     * Creates a new SnowyException with the specified error message.
     * This exception is used to signal errors specific to Snowy chatbot operations,
     * such as invalid user input, file I/O errors, or task operation failures.
     *
     * @param message A descriptive error message explaining what went wrong.
     */
    public SnowyException(String message) {
        super(message);
    }
}
