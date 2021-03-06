package seedu.task.logic;

import javafx.collections.ObservableList;
import seedu.task.logic.commands.CommandResult;
import seedu.task.logic.commands.exceptions.CommandException;
import seedu.task.model.ReadOnlyTaskManager;
import seedu.task.model.task.ReadOnlyTask;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     */
    CommandResult execute(String commandText) throws CommandException;

    /** Returns the filtered list of tasks */
    ObservableList<ReadOnlyTask> getFilteredTaskList();

    //@@author A0164889E
    /** Returns the filtered list of tasks for Complete tasks*/
    ObservableList<ReadOnlyTask> getFilteredTaskListComplete();

    //@@author A0163848R
    /**
     * Retrieves the current YTomorrow state.
     */
    ReadOnlyTaskManager getYTomorrow();

    /**
     * Overwrites the current YTomorrow.
     * @param YTomorrow to overwrite with
     */
    void setYTomorrow(ReadOnlyTaskManager set);

    /**
     * Adds entries to the current YTomorrow.
     * @param YTomorrow to add entries from
     */
    void importYTomorrow(ReadOnlyTaskManager add);
    //@@author
}
