package seedu.task.model.task;

import seedu.task.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Task in the task manager.
 * Implementations should guarantee: details are present and not null, field
 * values are validated.
 */
public interface ReadOnlyTask {

    Name getName();

    StartDate getStartDate();

    EndDate getEndDate();

    Group getGroup();

    /**
     * The returned TagList is a deep copy of the internal TagList, changes on
     * the returned list will not affect the task's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override
     * .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().equals(this.getName())
                );
    }

    /**
     * Formats the task as text, showing all contact details.
     */
  //@@author A0164032U
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();

        boolean hasStartDate = getStartDate() != null;
        boolean hasEndDate = getEndDate() != null;
        boolean hasGroup = !getGroup().value.equals(Group.GROUP_ID_HIDDEN);

        builder
            .append(getName() + ":")
            .append(hasStartDate ? " Start Date: " : "")
            .append(hasStartDate ? "[" + getStartDate() + "]" : "")
            .append(hasEndDate ? " End Date: " + "]" : "")
            .append(hasEndDate ? "[" + getEndDate() + "]" : "")
            .append(hasGroup ? " Group: " : "")
            .append(hasGroup ? "[" + getGroup() + "]" : "")
            .append(" Status: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

    //@@author A0163848R
    /**
     * @return Whether the task end date has passed the time of calling.
     */
    boolean hasPassed();
    //@author

    java.util.Date getEndTime();

    java.util.Date getStartTime();

}
