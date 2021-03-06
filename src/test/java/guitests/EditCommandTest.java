package guitests;

import static org.junit.Assert.assertTrue;
import static seedu.task.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TestPerson;
import seedu.task.commons.core.Messages;
import seedu.task.logic.commands.EditCommand;
import seedu.task.model.task.Date;
import seedu.task.model.task.Group;
import seedu.task.model.task.Name;

// TODO: reduce GUI tests by transferring some tests to be covered by lower level tests.
public class EditCommandTest extends AddressBookGuiTest {

    // The list of task in the person list panel is expected to match this list.
    // This list is updated with every successful call to assertEditSuccess().
    private TestPerson[] expectedPersonsList = td.getTypicalPersons();

    @Test
    public void editAllFieldsSpecifiedSuccess() throws Exception {
        int addressBookIndex = 1;
        //@@author A0164889E
        String detailsToEdit = "Bobby s/01/01 00:00 d/01/01 00:00 g/project";
        //@@author A0164889E
        TestPerson editedPerson = new PersonBuilder()
                .withName("Bobby")
                .withEndDate("01/01 00:00")
                .withStartDate("01/01 00:00")
                .withGroup("project")
                .withTags("incomplete").build();

        assertEditSuccess(addressBookIndex, addressBookIndex, detailsToEdit, editedPerson);
    }
    //@@author A0164032U
    @Test
    public void editNotAllFieldsSpecifiedSuccess() throws Exception {
        String detailsToEdit = "g/examination";
        int addressBookIndex = 2;

        TestPerson personToEdit = expectedPersonsList[addressBookIndex - 1];
        TestPerson editedPerson = new PersonBuilder(personToEdit).withGroup("examination").build();

        assertEditSuccess(addressBookIndex, addressBookIndex, detailsToEdit, editedPerson);
    }
    //@@author

    @Test
    public void editFindThenEditSuccess() throws Exception {
        commandBox.runCommand("find Elle");

        String detailsToEdit = "Belle";
        int filteredPersonListIndex = 1;
        int addressBookIndex = 5;

        TestPerson personToEdit = expectedPersonsList[addressBookIndex - 1];
        TestPerson editedPerson = new PersonBuilder(personToEdit).withName("Belle").build();

        assertEditSuccess(filteredPersonListIndex, addressBookIndex, detailsToEdit, editedPerson);
    }

    @Test
    public void editMissingPersonIndexFailure() {
        commandBox.runCommand("edit Bobby");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
    }

    @Test
    public void editInvalidPersonIndexFailure() {
        commandBox.runCommand("edit 8 Bobby");
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void editNoFieldsSpecifiedFailure() {
        commandBox.runCommand("edit 1");
        assertResultMessage(EditCommand.MESSAGE_NOT_EDITED);
    }
    //@@author A0164032U
    @Test
    public void editInvalidValuesFailure() {
        commandBox.runCommand("edit 1 *&");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);

        commandBox.runCommand("edit 1 s/ssse");
        assertResultMessage(Date.MESSAGE_DATE_CONSTRAINTS);

        commandBox.runCommand("edit 1 d/iiie");
        assertResultMessage(Date.MESSAGE_DATE_CONSTRAINTS);

        commandBox.runCommand("edit 1 g/");
        assertResultMessage(Group.MESSAGE_GROUP_CONSTRAINTS);

    }

    @Test
    public void editDuplicatePersonFailure() {
        //@@author A0164889E
        commandBox.runCommand("edit 3 Alice Paul s/01/01 00:00 d/12/12 00:00 "
                                + "g/group1");
        assertResultMessage(EditCommand.MESSAGE_DUPLICATE_TASK);
    }

    /**
     * Checks whether the edited person has the correct updated details.
     *
     * @param filteredPersonListIndex index of person to edit in filtered list
     * @param addressBookIndex index of person to edit in the address book.
     *      Must refer to the same person as {@code filteredPersonListIndex}
     * @param detailsToEdit details to edit the person with as input to the edit command
     * @param editedPerson the expected person after editing the person's details
     */
    private void assertEditSuccess(int filteredPersonListIndex, int addressBookIndex,
                                    String detailsToEdit, TestPerson editedPerson) {
        commandBox.runCommand("edit " + filteredPersonListIndex + " " + detailsToEdit);

        // confirm the new card contains the right data
        PersonCardHandle editedCard = personListPanel.navigateToPerson(editedPerson.getName().fullName);
        assertMatching(editedPerson, editedCard);

        // confirm the list now contains all previous persons plus the person with updated details
        expectedPersonsList[addressBookIndex - 1] = editedPerson;
        assertTrue(personListPanel.isListMatching(expectedPersonsList));
        System.out.println(String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedPerson));
        assertResultMessage(String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedPerson));
    }
}
