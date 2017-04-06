package seedu.address.logic.commands;


//@@author A0164466X
/**
 * Lists all tasks in the task manager to the user.
 */
public class ListCompleteCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all tasks";


    @Override
    public CommandResult execute() {
        model.updateFilteredListToShowAll();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
