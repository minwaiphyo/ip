package snowy;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI window of Snowy.
 * Manages the main chat interface including the scroll pane for chat history,
 * the text input field, and the send button. Handles user input events and
 * coordinates with the Snowy chatbot to generate and display responses.
 */
public class MainWindow extends AnchorPane {
    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/tintin.png"));
    private final Image snowyImage = new Image(this.getClass().getResourceAsStream("/images/snowy.png"));
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    private Snowy snowy;

    /**
     * Initializes the main window components.
     * Sets up auto-scrolling behavior for the scroll pane to always show
     * the most recent messages at the bottom.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Snowy instance into the controller and displays the welcome message.
     * This method is called by Main after the FXML is loaded to provide the
     * controller with access to the Snowy chatbot.
     *
     * @param s The Snowy chatbot instance to use for processing commands.
     */
    public void setSnowy(Snowy s) {
        snowy = s;
        // Show welcome message
        dialogContainer.getChildren().add(
                DialogBox.getSnowyDialog(snowy.getWelcome(), snowyImage)
        );
    }

    /**
     * Handles user input from the text field or send button.
     * Creates dialog boxes for both the user's input and Snowy's response,
     * adds them to the chat display, and clears the input field for the next message.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = snowy.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getSnowyDialog(response, snowyImage)
        );
        userInput.clear();
    }
}
