package snowy;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box in the chat interface.
 * A dialog box consists of an ImageView displaying the speaker's avatar
 * and a Label containing the speaker's message text. This custom control
 * loads its layout from an FXML file and can be flipped to distinguish
 * between user and Snowy messages.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;


    /**
     * Creates a dialog box with the specified text and image.
     * Loads the dialog box layout from DialogBox.fxml, sets this instance
     * as both the controller and root node, and populates the label and
     * image view with the provided content.
     *
     * @param text The message text to display in the dialog box.
     * @param img  The avatar image to display next to the message.
     */
    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Creates a dialog box for displaying user messages.
     * The avatar appears on the right side with text on the left.
     *
     * @param text The user's message text.
     * @param img  The user's avatar image.
     * @return A DialogBox configured for user messages.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Creates a dialog box for displaying Snowy's messages.
     * The avatar appears on the left side with text on the right,
     * achieved by flipping the standard layout.
     *
     * @param text Snowy's response text.
     * @param img  Snowy's avatar image.
     * @return A DialogBox configured for Snowy's messages.
     */
    public static DialogBox getSnowyDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     * This is used to visually distinguish Snowy's messages (flipped) from user
     * messages (not flipped).
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }
}
