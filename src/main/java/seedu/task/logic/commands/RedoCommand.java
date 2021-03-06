package seedu.task.logic.commands;

import seedu.task.logic.commands.exceptions.CommandException;

//@@author A0163848R
/**
 * Command that redoes changes caused by the last command.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undoes the changes made by the last command.\n";
    public static final String REDO_SUCCESS = "Redo!";
    public static final String REDO_FAILURE = "Nothing to redo!";

    public RedoCommand() {
    }

    @Override
    public CommandResult execute() throws CommandException {
        boolean result = model.redoLastModification();

        return new CommandResult(result ? REDO_SUCCESS : REDO_FAILURE);
    }

}
