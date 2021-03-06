package seedu.task.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.task.commons.core.ComponentManager;
import seedu.task.commons.core.LogsCenter;
import seedu.task.logic.commands.Command;
import seedu.task.logic.commands.CommandResult;
import seedu.task.logic.commands.exceptions.CommandException;
import seedu.task.logic.parser.Parser;
import seedu.task.model.Model;
import seedu.task.model.ReadOnlyTaskManager;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new Parser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        return command.execute();
    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }

    //@@author A0164889E
    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskListComplete() {
        return model.getFilteredTaskListComplete();
    }

    //@@author A0163848R
    @Override
    public ReadOnlyTaskManager getYTomorrow() {
        return model.getTaskManager();
    }

    @Override
    public void setYTomorrow(ReadOnlyTaskManager set) {
        model.resetData(set);
    }

    @Override
    public void importYTomorrow(ReadOnlyTaskManager add) {
        model.mergeYTomorrow(add);
    }
    //@@author
}
