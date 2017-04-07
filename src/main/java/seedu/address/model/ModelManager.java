package seedu.address.model;

import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniquePersonList;
import seedu.address.model.task.UniquePersonList.DuplicatePersonException;
import seedu.address.model.task.UniquePersonList.PersonNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final YTomorrow addressBook;
    private final History<ReadOnlyAddressBook> history;
    private final FilteredList<ReadOnlyTask> filteredTasks;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        assert !CollectionUtil.isAnyNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new YTomorrow(addressBook);
        this.history = new History<ReadOnlyAddressBook>();
        filteredTasks = new FilteredList<>(this.addressBook.getPersonList());

        //@@author A0163848R
        history.push(addressBook);
        //@@author
    }

    public ModelManager() {
        this(new YTomorrow(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        //@@author A0163848R-reused
        addToHistory(new YTomorrow(addressBook));
        //@@author
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void deletePerson(ReadOnlyTask target) throws PersonNotFoundException {
        addressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(Task person) throws UniquePersonList.DuplicatePersonException {
        addressBook.addPerson(person);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(int filteredPersonListIndex, ReadOnlyTask editedPerson)
            throws UniquePersonList.DuplicatePersonException {
        assert editedPerson != null;

        int addressBookIndex = filteredTasks.getSourceIndex(filteredPersonListIndex);
        addressBook.updatePerson(addressBookIndex, editedPerson);
        indicateAddressBookChanged();
    }

    //@@author A0163848R
    @Override
    public boolean undoLastModification() {
        ReadOnlyAddressBook undone = history.undo();
        if (undone != null) {
            addressBook.resetData(undone);
            return true;
        }
        return false;
    }

    @Override
    public boolean redoLastModification() {
        ReadOnlyAddressBook redone = history.redo();
        if (redone != null) {
            addressBook.resetData(redone);
            return true;
        }
        return false;
    }
    
    @Override
    public void mergeYTomorrow(ReadOnlyAddressBook add) {
        for (ReadOnlyTask readOnlyTask : add.getPersonList()) {
            Task task = new Task(readOnlyTask);
            try {
                addressBook.addPerson(task);
            } catch (DuplicatePersonException e) {
                try {
                    addressBook.removePerson(task);
                    addressBook.addPerson(task);
                } catch (PersonNotFoundException | DuplicatePersonException el) {
                }
                
            }
        }
        indicateAddressBookChanged();
    }
    //@@author

    @Override
    public void addToHistory(ReadOnlyAddressBook state) {
        history.push(state);
    }

    //=========== Filtered Person List Accessors =============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredPersonList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);
    }

    @Override
    public void updateFilteredPersonList(Set<String> keywords) {
        updateFilteredPersonList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredPersonList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }
    
    //@@author A0164466X
    @Override
    public void updateFilteredListToShowComplete() {
        // TODO Auto-generated method stub
        try {
            updateFilteredPersonList(new PredicateExpression(new TagQualifier(new UniqueTagList(Tag.TAG_COMPLETE))));
        } catch (DuplicateTagException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalValueException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    @Override
    public void updateFilteredListToShowIncomplete() {
        // TODO Auto-generated method stub
        try {
            updateFilteredPersonList(new PredicateExpression(new TagQualifier(new UniqueTagList(Tag.TAG_INCOMPLETE))));
        } catch (DuplicateTagException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalValueException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //========== Inner classes/interfaces used for filtering =================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask person);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask person);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsWordIgnoreCase(task.getName().fullName, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }
    
    //@@author A0164466X
    private class TagQualifier implements Qualifier {
        private UniqueTagList tags;
        
        TagQualifier(UniqueTagList tags){
            this.tags = tags;
        }
        
        @Override
        public boolean run(ReadOnlyTask task) {
            return task.getTags().equals(tags);
        }
        
        //Default toString() method used
        
    }

}
