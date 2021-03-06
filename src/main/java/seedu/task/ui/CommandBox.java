package seedu.task.ui;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import seedu.task.commons.events.ui.NewResultAvailableEvent;
import seedu.task.commons.util.FxViewUtil;
import seedu.task.logic.Logic;
import seedu.task.logic.commands.CommandResult;
import seedu.task.logic.commands.exceptions.CommandException;

public class CommandBox extends UiPart<Region> {

    private static final String FXML = "CommandBox.fxml";
    private final Logic logic;

    @FXML
    private TextField commandTextField;

    private String lastCommand = null;

    public CommandBox(AnchorPane commandBoxPlaceholder, Logic logic) {
        super(FXML);
        this.logic = logic;
        addToPlaceholder(commandBoxPlaceholder);
        setAccelerators();
    }

    //@@author A0163848R
    /**
     * Sets hot-keys for this window:
     * Up arrow - write last command to command box
     * Down arrow - clears command box
     */
    private void setAccelerators() {
        commandTextField.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
            case UP:
                handleLast();
                break;
            case DOWN:
                handleClear();
                break;
            default:
            }
        });
    }
    //@@author

    private void addToPlaceholder(AnchorPane placeHolderPane) {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextField);
        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    @FXML
    private void handleCommandInputChanged() {
        lastCommand = commandTextField.getText();
        //@@author A0164032U
        try {
            CommandResult commandResult = logic.execute(lastCommand);

            // process result of the command
            setStyleToIndicateCommandSuccess();
            commandTextField.setText("");
            LOGGER.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser, false));

        } catch (CommandException e) {
            // handle command failure
            setStyleToIndicateCommandFailure();
            LOGGER.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage(), true));
        }
        //@@author
    }


    /**
     * Sets the command box style to indicate a successful command.
     */
    private void setStyleToIndicateCommandSuccess() {
        commandTextField.getStyleClass().remove(Ui.ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        commandTextField.getStyleClass().add(Ui.ERROR_STYLE_CLASS);
    }

    //@@author A0163848R
    /**
     * Writes the last command string to the command box
     */
    private void handleLast() {
        if (lastCommand != null) {
            commandTextField.setText(lastCommand);
        }
    }

    /**
     * Clears the command box
     */
    private void handleClear() {
        commandTextField.clear();
    }
    //@@author
}
