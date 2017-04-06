package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.ReadOnlyPerson;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<ReadOnlyPerson> getPersonList();

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

    //@@author A0164889E
//    /**
//     * Returns an unmodifiable view of the persons complete list.
//     * This list will not contain any duplicate persons.
//     */
//    ObservableList<ReadOnlyPerson> getPersonListComplete();
}
