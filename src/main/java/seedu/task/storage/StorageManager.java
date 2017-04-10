package seedu.task.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.task.commons.core.ComponentManager;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.events.model.TaskManagerChangedEvent;
import seedu.task.commons.events.storage.DataSavingExceptionEvent;
import seedu.task.commons.events.storage.ExportRequestEvent;
import seedu.task.commons.events.storage.ImportRequestEvent;
import seedu.task.commons.events.storage.ImportResultAvailableEvent;
import seedu.task.commons.events.storage.LoadRequestEvent;
import seedu.task.commons.events.storage.LoadResultAvailableEvent;
import seedu.task.commons.events.storage.TargetFileRequestEvent;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.model.ReadOnlyTaskManager;
import seedu.task.model.UserPrefs;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskManagerStorage addressBookStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(TaskManagerStorage addressBookStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.addressBookStorage = addressBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(String addressBookFilePath, String userPrefsFilePath) {
        this(new XmlAddressBookStorage(addressBookFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public String getTaskManagerFilePath() {
        return addressBookStorage.getTaskManagerFilePath();
    }

    @Override
    public Optional<ReadOnlyTaskManager> readTaskManager() throws DataConversionException, IOException {
        return readTaskManager(addressBookStorage.getTaskManagerFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskManager> readTaskManager(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return addressBookStorage.readTaskManager(filePath);
    }

    @Override
    public void saveTaskManager(ReadOnlyTaskManager addressBook) throws IOException {
        saveTaskManager(addressBook, addressBookStorage.getTaskManagerFilePath());
    }

    @Override
    public void saveTaskManager(ReadOnlyTaskManager addressBook, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        addressBookStorage.saveTaskManager(addressBook, filePath);
    }


    @Override
    @Subscribe
    public void handleTaskManagerChangedEvent(TaskManagerChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskManager(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    //@@author A0163848R
    @Override
    @Subscribe
    public void handleExportRequestEvent(ExportRequestEvent ere) {
        try {
            saveTaskManager(ere.getYTomorrowToExport(), ere.getTargetFile().getPath());
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    @Override
    @Subscribe
    public void handleLoadRequestEvent(LoadRequestEvent ire) {
        Optional<ReadOnlyTaskManager> loaded = null;

        try {
            loaded = readTaskManager(ire.getTargetFile().getPath());
        } catch (DataConversionException | IOException e) {
        }

        raise(new LoadResultAvailableEvent(loaded, ire.getTargetFile()));
    }

    @Override
    @Subscribe
    public void handleImportRequestEvent(ImportRequestEvent ire) {
        Optional<ReadOnlyTaskManager> loaded = null;

        try {
            loaded = readTaskManager(ire.getTargetFile().getPath());
        } catch (DataConversionException | IOException e) {
        }

        raise(new ImportResultAvailableEvent(loaded));
    }

    @Override
    @Subscribe
    public void handleTargetFileRequestEvent(TargetFileRequestEvent tfre) {
        addressBookStorage.setTaskManagerFilePath(tfre.getTargetFile().getPath());
        tfre.getUserPrefs().getGuiSettings().setLastLoadedYTomorrow(tfre.getTargetFile().getPath());
    }

    @Override
    public void setTaskManagerFilePath(String path) {
        addressBookStorage.setTaskManagerFilePath(path);
    }
    //@@author
}
