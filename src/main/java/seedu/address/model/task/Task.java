package seedu.address.model.task;

import java.util.Objects;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.UniqueTagList;

public class Task implements ReadOnlyTask{
	
//	private Name name;
//    private Phone phone;
//    private Email email;
//    private Address address;

    private UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
//    public Person(Name name, Phone phone, Email email, Address address, UniqueTagList tags) {
//        assert !CollectionUtil.isAnyNull(name, phone, email, address, tags);
//        this.name = name;
//        this.phone = phone;
//        this.email = email;
//        this.address = address;
//        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
//    }
    public Task(UniqueTagList tags){
    	assert !CollectionUtil.isAnyNull(tags);
    	this.tags = new UniqueTagList(tags);
    }

    /**
     * Creates a copy of the given ReadOnlyPerson.
     */
//    public Person(ReadOnlyPerson source) {
//        this(source.getName(), source.getPhone(), source.getEmail(), source.getAddress(), source.getTags());
//    }
    public Task(ReadOnlyTask source) {
    	this(source.getTags());
    }

//    public void setName(Name name) {
//        assert name != null;
//        this.name = name;
//    }
//
//    @Override
//    public Name getName() {
//        return name;
//    }
//
//    public void setPhone(Phone phone) {
//        assert phone != null;
//        this.phone = phone;
//    }
//
//    @Override
//    public Phone getPhone() {
//        return phone;
//    }
//
//    public void setEmail(Email email) {
//        assert email != null;
//        this.email = email;
//    }
//
//    @Override
//    public Email getEmail() {
//        return email;
//    }
//
//    public void setAddress(Address address) {
//        assert address != null;
//        this.address = address;
//    }
//
//    @Override
//    public Address getAddress() {
//        return address;
//    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }

    /**
     * Replaces this person's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }

    /**
     * Updates this person with the details of {@code replacement}.
     */
    public void resetData(ReadOnlyTask replacement) {
        assert replacement != null;

//        this.setName(replacement.getName());
//        this.setPhone(replacement.getPhone());
//        this.setEmail(replacement.getEmail());
//        this.setAddress(replacement.getAddress());
        this.setTags(replacement.getTags());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        //return Objects.hash(name, phone, email, address, tags);
    	return Objects.hash(tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }

}