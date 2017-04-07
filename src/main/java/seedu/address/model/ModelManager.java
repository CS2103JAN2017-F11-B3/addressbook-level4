package seedu.address.model;

import java.util.HashSet;
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
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;
import seedu.address.model.task.ReadOnlyPerson;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniquePersonList;
import seedu.address.model.task.UniquePersonList.DuplicatePersonException;
import seedu.address.model.task.UniquePersonList.PersonNotFoundException;
import seedu.address.ui.MainWindow;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final YTomorrow addressBook;    
    //@@author A0164889E
    //private YTomorrow addressBookComplete;
    
    private final History<ReadOnlyAddressBook> history;
    
    private final FilteredList<ReadOnlyPerson> filteredPersons;
    //@@author A0164889E
    private FilteredList<ReadOnlyPerson> filteredPersonsComplete;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        assert !CollectionUtil.isAnyNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new YTomorrow(addressBook);
        //@@author A0164889E
        initialCompleteList();

        this.history = new History<ReadOnlyAddressBook>();

        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        //@@author A0164889E
        filteredPersonsComplete = new FilteredList<>(this.addressBook.getPersonList());
       
        indicateCompleteListToChange();
//        Set<String> set = new HashSet<String>();
//        set.add("incomplete");
//        updateFilteredPersonListTag(set);
//        //@@author A0164889E
//        indicateCompleteListToChange();
        //@@author A0163848R
        history.push(addressBook);
    }

    public ModelManager() {
        this(new YTomorrow(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);

        //@@author A0164889E
        indicateCompleteListToChange(); 
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
        //@@author A0164889E
        //raise(new AddressBookChangedEvent(addressBookComplete));
    }

    @Override
    public synchronized void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException {
        addressBook.removePerson(target);

        //@@author A0164889E
        indicateCompleteListToChange();
        indicateAddressBookChanged();            
    }

    @Override
    public synchronized void addPerson(Task person) throws UniquePersonList.DuplicatePersonException {
        addressBook.addPerson(person);

        updateFilteredListToShowAll();
        //@@author A0164889E
        indicateCompleteListToChange(); 
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(int filteredPersonListIndex, ReadOnlyPerson editedPerson)
            throws UniquePersonList.DuplicatePersonException {
        assert editedPerson != null;

        int addressBookIndex = filteredPersons.getSourceIndex(filteredPersonListIndex);
        addressBook.updatePerson(addressBookIndex, editedPerson);

        //@@author A0164889E
        indicateCompleteListToChange(); 
        indicateAddressBookChanged();       
    }

    //@@author A0163848R
    @Override
    public boolean undoLastModification() {
        ReadOnlyAddressBook undone = history.undo();
        if (undone != null) {
            addressBook.resetData(undone);
            //@@author A0164889E
            indicateCompleteListToChange(); 
            return true;
        }
        return false;
    }

    @Override
    public boolean redoLastModification() {
        ReadOnlyAddressBook redone = history.redo();
        if (redone != null) {
            addressBook.resetData(redone);
            //@@author A0164889E
            indicateCompleteListToChange(); 
            return true;
        }
        return false;
    }
    
    @Override
    public void mergeYTomorrow(ReadOnlyAddressBook add) {
        for (ReadOnlyPerson readOnlyTask : add.getPersonList()) {
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
         //@@author A0164889E
        indicateCompleteListToChange(); 
        indicateAddressBookChanged();       
    }
    //@@author

    @Override
    public void addToHistory(ReadOnlyAddressBook state) {
        history.push(state);
    }
    
    //@@author A0164889E
    public void indicateCompleteListToChange() {
//        addressBookComplete = new YTomorrow();
//        UniqueTagList tags;
//        try {
//            tags = new UniqueTagList("complete");
//            for(int i=0; i<addressBook.getPersonList().size(); i++) {
//                if(addressBook.getPersonList().get(i).getTags().equals(tags)) {
//                    Task task = new Task(addressBook.getPersonList().get(i));
//                    addressBookComplete.addPerson(task);
//                }
//            }  
            //@@author A0164889E
            //filteredPersonsComplete = new FilteredList<>(this.addressBookComplete.getPersonList());
            Set<String> set = new HashSet<String>();
            set.add("complete");
            updateFilteredPersonListTag(set);
//        } catch (DuplicateTagException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalValueException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }       
    }
    
    //@@author A0164889E
    public void initialCompleteList() {
//        addressBookComplete = new YTomorrow();
//        UniqueTagList tags;
//        try {
//            tags = new UniqueTagList("complete");
//            for(int i=0; i<addressBook.getPersonList().size(); i++) {
//                if(addressBook.getPersonList().get(i).getTags().equals(tags)) {
//                    Task task = new Task(addressBook.getPersonList().get(i));
//                    addressBookComplete.addPerson(task);
//                }
//            }  
//        } catch (DuplicateTagException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalValueException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }       
    }

    //=========== Filtered Person List Accessors =============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyPerson> getFilteredPersonList() {
        return new UnmodifiableObservableList<>(filteredPersons);
    }
    
    //@@author A0164889E
    @Override
    public UnmodifiableObservableList<ReadOnlyPerson> getFilteredPersonListComplete() {
        return new UnmodifiableObservableList<>(filteredPersonsComplete);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredPersons.setPredicate(null);
        //@@author A0164889E
        filteredPersonsComplete.setPredicate(null);
    }

    @Override
    public void updateFilteredPersonList(Set<String> keywords) {
        updateFilteredPersonList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredPersonList(Expression expression) {
        filteredPersons.setPredicate(expression::satisfies);
    }
  
    //@@author A0164889E
    @Override
    public void updateFilteredPersonListGroup(Set<String> keywords) {
        updateFilteredPersonListGroup(new PredicateExpression(new GroupQualifier(keywords)));
    }

    private void updateFilteredPersonListGroup(Expression expression) {
        filteredPersons.setPredicate(expression::satisfies);
    }
    
    public void updateFilteredPersonListTag(UniqueTagList keywords) {
        updateFilteredPersonListTag(new PredicateExpression(new TagQualifier(keywords)));
    }

    private void updateFilteredPersonListTag(Expression expression) {
        filteredPersonsComplete.setPredicate(expression::satisfies);
    }
    //@@author

    //========== Inner classes/interfaces used for filtering =================================================

    interface Expression {
        boolean satisfies(ReadOnlyPerson person);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyPerson person) {
            return qualifier.run(person);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyPerson person);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyPerson person) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }
    
    //@@author A0164889E
    private class GroupQualifier implements Qualifier {
        private Set<String> groupKeyWords;

        GroupQualifier(Set<String> groupKeyWords) {
            this.groupKeyWords = groupKeyWords;
        }

        @Override
        public boolean run(ReadOnlyPerson person) {
            return groupKeyWords.stream()
                    .filter(keyword -> StringUtil.containsWordIgnoreCase(person.getGroup().value, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", groupKeyWords);
        }
    }

    private class TagQualifier implements Qualifier {
        private UniqueTagList tagKeyWords;

        TagQualifier(UniqueTagList tagKeyWords) {
            this.tagKeyWords = tagKeyWords;
        }

        @Override
        public boolean run(ReadOnlyPerson person) {
            return tagKeyWords.stream()
                    .filter(keyword -> StringUtil.containsWordIgnoreCase(person.getTags(). keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "tag=" + String.join(", ", tagKeyWords);
        }
    }
    //@@author

}
