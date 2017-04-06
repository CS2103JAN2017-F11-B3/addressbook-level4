package seedu.address.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.ReadOnlyPerson;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniquePersonList;
import seedu.address.model.task.UniquePersonList.DuplicatePersonException;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class YTomorrow implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueTagList tags;
    
    //@@author A0164889E
    private final UniquePersonList personsComplete;
    private final UniqueTagList tagsComplete;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        tags = new UniqueTagList();
        
        //@@author A0164889E
        personsComplete = new UniquePersonList();
        tagsComplete = new UniqueTagList();
    }

    public YTomorrow() {}

    /**
     * Creates an AddressBook using the Persons and Tags in the {@code toBeCopied}
     */
    public YTomorrow(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

//// list overwrite operations

    public void setPersons(List<? extends ReadOnlyPerson> persons)
            throws UniquePersonList.DuplicatePersonException {
        this.persons.setPersons(persons);
    }
    
    public void setPersonsComplete(List<? extends ReadOnlyPerson> persons)
            throws UniquePersonList.DuplicatePersonException {
        this.personsComplete.setPersons(persons);
    }

    public void setTags(Collection<Tag> tags) throws UniqueTagList.DuplicateTagException {
        this.tags.setTags(tags);
    }

    public void resetData(ReadOnlyAddressBook newData) {
        assert newData != null;
        try {
            setPersons(newData.getPersonList());
            //@author A0164889E
            //setPersonsComplete(newData.getPersonList());
        } catch (UniquePersonList.DuplicatePersonException e) {
            assert false : "AddressBooks should not have duplicate persons";
        }
        try {
            setTags(newData.getTagList());
        } catch (UniqueTagList.DuplicateTagException e) {
            assert false : "AddressBooks should not have duplicate tags";
        }
        syncMasterTagListWith(persons);
        //syncMasterTagListWithComplete(personsComplete);
    }

//// person-level operations

    /**
     * Adds a person to the address book.
     * Also checks the new person's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the person to point to those in {@link #tags}.
     *
     * @throws UniquePersonList.DuplicatePersonException if an equivalent person already exists.
     */
    public void addPerson(Task p) throws UniquePersonList.DuplicatePersonException {
        syncMasterTagListWith(p);
        persons.add(p);
    }
    
    //@@author A0164889E
    public void addPersonComplete(Task p) throws UniquePersonList.DuplicatePersonException {
        syncMasterTagListWithComplete(p);
        personsComplete.add(p);
    }

    /**
     * Updates the person in the list at position {@code index} with {@code editedReadOnlyPerson}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedReadOnlyPerson}.
     * @see #syncMasterTagListWith(Task)
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws IndexOutOfBoundsException if {@code index} < 0 or >= the size of the list.
     */
    public void updatePerson(int index, ReadOnlyPerson editedReadOnlyPerson)
            throws UniquePersonList.DuplicatePersonException {
        assert editedReadOnlyPerson != null;

        Task editedPerson = new Task(editedReadOnlyPerson);
        syncMasterTagListWith(editedPerson);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        persons.updatePerson(index, editedPerson);        
    }
    
    //@@author A0164889E
    public void updatePersonComplete(int index, ReadOnlyPerson editedReadOnlyPerson)
            throws UniquePersonList.DuplicatePersonException {
        assert editedReadOnlyPerson != null;

        Task editedPerson = new Task(editedReadOnlyPerson);
        syncMasterTagListWithComplete(editedPerson);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        personsComplete.updatePerson(index, editedPerson);      
    }

    /**
     * Ensures that every tag in this person:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     */
    private void syncMasterTagListWith(Task person) {
        final UniqueTagList personTags = person.getTags();
        tags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        personTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        person.setTags(new UniqueTagList(correctTagReferences));
    }
    
    //@@author A0164889E
    private void syncMasterTagListWithComplete(Task person) {
        final UniqueTagList personTags = person.getTags();
        tagsComplete.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tagsComplete.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        personTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        person.setTags(new UniqueTagList(correctTagReferences));
    }

    /**
     * Ensures that every tag in these persons:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     *  @see #syncMasterTagListWith(Task)
     */
    private void syncMasterTagListWith(UniquePersonList persons) {
        persons.forEach(this::syncMasterTagListWith);
    }
    
    //@@author A0164889E
    private void syncMasterTagListWithComplete(UniquePersonList persons) {
        persons.forEach(this::syncMasterTagListWithComplete);
    }

    public boolean removePerson(ReadOnlyPerson key) throws UniquePersonList.PersonNotFoundException {
        if (persons.remove(key)) {
            return true;
        } else {
            throw new UniquePersonList.PersonNotFoundException();
        }
    }
    
    //@@author A0164889E
    public boolean removePersonComplete(ReadOnlyPerson key) throws UniquePersonList.PersonNotFoundException {
        if (personsComplete.remove(key)) {
            return true;
        } else {
            throw new UniquePersonList.PersonNotFoundException();
        }
    }

//// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }
    
    //@@author A0164889E
    public void addTagComplete(Tag t) throws UniqueTagList.DuplicateTagException {
        tagsComplete.add(t);
    }

//// util methods

    @Override
    public String toString() {
        return persons.asObservableList().size() + " persons, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }
    
    //@@author A0164889E
    public String toStringComplete() {
        return personsComplete.asObservableList().size() + " persons, " + tagsComplete.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<ReadOnlyPerson> getPersonList() {
        return new UnmodifiableObservableList<>(persons.asObservableList());
    }
    
    //@@author A0164889E
    //@Override
    //public ObservableList<ReadOnlyPerson> getPersonListComplete() {
    //    return new UnmodifiableObservableList<>(personsComplete.asObservableList());
    //}

    @Override
    public ObservableList<Tag> getTagList() {
        return new UnmodifiableObservableList<>(tags.asObservableList());
    }
    
    //@@author A0164889E
    public ObservableList<Tag> getTagListComplete() {
        return new UnmodifiableObservableList<>(tagsComplete.asObservableList());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof YTomorrow // instanceof handles nulls
                && this.persons.equals(((YTomorrow) other).persons)
                && this.tags.equalsOrderInsensitive(((YTomorrow) other).tags));
    }
    
    //@@author A0164889E
    public boolean equalsComplete(Object other) {
        return other == this // short circuit if same object
                || (other instanceof YTomorrow // instanceof handles nulls
                && this.personsComplete.equals(((YTomorrow) other).personsComplete)
                && this.tagsComplete.equalsOrderInsensitive(((YTomorrow) other).tagsComplete));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, tags);
    }
    
    //@@author A0164889E
    public int hashCodeComplete() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(personsComplete, tagsComplete);
    }
}
