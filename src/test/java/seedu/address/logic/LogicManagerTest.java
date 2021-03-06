package seedu.address.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.task.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.task.commons.core.Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
import static seedu.task.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.eventbus.Subscribe;

import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.events.model.TaskManagerChangedEvent;
import seedu.task.commons.events.ui.JumpToListRequestEvent;
import seedu.task.commons.events.ui.ShowHelpRequestEvent;
import seedu.task.logic.Logic;
import seedu.task.logic.LogicManager;
import seedu.task.logic.commands.AddCommand;
import seedu.task.logic.commands.ClearCommand;
import seedu.task.logic.commands.Command;
import seedu.task.logic.commands.CommandResult;
import seedu.task.logic.commands.DeleteCommand;
import seedu.task.logic.commands.ExitCommand;
import seedu.task.logic.commands.FindCommand;
import seedu.task.logic.commands.HelpCommand;
import seedu.task.logic.commands.ListCommand;
import seedu.task.logic.commands.ListCompleteCommand;
import seedu.task.logic.commands.ListIncompleteCommand;
import seedu.task.logic.commands.SelectCommand;
import seedu.task.logic.commands.exceptions.CommandException;
import seedu.task.model.Model;
import seedu.task.model.ModelManager;
import seedu.task.model.ReadOnlyTaskManager;
import seedu.task.model.YTomorrow;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.EndDate;
import seedu.task.model.task.Group;
import seedu.task.model.task.Name;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.StartDate;
import seedu.task.model.task.Task;
import seedu.task.storage.StorageManager;


public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    //These are for checking the correctness of the events raised
    private ReadOnlyTaskManager latestSavedAddressBook;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    private void handleLocalModelChangedEvent(TaskManagerChangedEvent abce) {
        latestSavedAddressBook = new YTomorrow(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }

    @Before
    public void setUp() {
        model = new ModelManager();
        String tempAddressBookFile = saveFolder.getRoot().getPath() + "TempAddressBook.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempAddressBookFile, tempPreferencesFile));
        EventsCenter.getInstance().registerHandler(this);

        latestSavedAddressBook = new YTomorrow(model.getTaskManager()); // last saved assumed to be up to date
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @After
    public void tearDown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_invalid() {
        String invalidCommand = "       ";
        assertCommandFailure(invalidCommand, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command, confirms that a CommandException is not thrown and that the result message is correct.
     * Also confirms that both the 'address book' and the 'last shown list' are as specified.
     * @see #assertCommandBehavior(boolean, String, String, ReadOnlyTaskManager, List)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            ReadOnlyTaskManager expectedAddressBook,
            List<? extends ReadOnlyTask> expectedShownList) {
        assertCommandBehavior(false, inputCommand, expectedMessage, expectedAddressBook, expectedShownList);
    }

    //@@author A0164466X
    /**
     * Executes the command, confirms that a CommandException is not thrown and that the result message is correct.
     */
    private void assertCommandMessageSuccess(String inputCommand, String expectedMessage) {
        assertCommandBehavior(false, inputCommand, expectedMessage);
    }
    //@@author

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * Both the 'address book' and the 'last shown list' are verified to be unchanged.
     * @see #assertCommandBehavior(boolean, String, String, ReadOnlyTaskManager, List)
     */
    private void assertCommandFailure(String inputCommand, String expectedMessage) {
        YTomorrow expectedAddressBook = new YTomorrow(model.getTaskManager());
        List<ReadOnlyTask> expectedShownList = new ArrayList<>(model.getFilteredTaskList());
        assertCommandBehavior(true, inputCommand, expectedMessage, expectedAddressBook, expectedShownList);
    }

    /**
     * Executes the command, confirms that the result message is correct
     * and that a CommandException is thrown if expected
     * and also confirms that the following three parts of the LogicManager object's state are as expected:<br>
     *      - the internal address book data are same as those in the {@code expectedAddressBook} <br>
     *      - the backing list shown by UI matches the {@code shownList} <br>
     *      - {@code expectedAddressBook} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(boolean isCommandExceptionExpected, String inputCommand, String expectedMessage,
            ReadOnlyTaskManager expectedAddressBook,
            List<? extends ReadOnlyTask> expectedShownList) {

        try {
            CommandResult result = logic.execute(inputCommand);
            assertFalse("CommandException expected but was not thrown.", isCommandExceptionExpected);
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException e) {
            assertTrue("CommandException not expected but was thrown.", isCommandExceptionExpected);
            assertEquals(expectedMessage, e.getMessage());
        }

        //Confirm the ui display elements should contain the right data
        assertEquals(expectedShownList, model.getFilteredTaskList());

        //Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedAddressBook, model.getTaskManager());
        assertEquals(expectedAddressBook, latestSavedAddressBook);
    }
    //@@author A0164466X
    /**
     * Executes the command, confirms that the result message is correct
     * and that a CommandException is thrown if expected
     */
    private void assertCommandBehavior(boolean isCommandExceptionExpected,
            String inputCommand,
            String expectedMessage) {

        try {
            CommandResult result = logic.execute(inputCommand);
            assertFalse("CommandException expected but was not thrown.", isCommandExceptionExpected);
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException e) {
            assertTrue("CommandException not expected but was thrown.", isCommandExceptionExpected);
            assertEquals(expectedMessage, e.getMessage());
        }
    }
    //@@author

    @Test
    public void execute_unknownCommandWord() {
        String unknownCommand = "uicfhmowqewca";
        assertCommandFailure(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_help() {
        assertCommandSuccess("help", HelpCommand.SHOWING_HELP_MESSAGE, new YTomorrow(), Collections.emptyList());
        assertTrue(helpShown);
    }

    @Test
    public void execute_exit() {
        assertCommandSuccess("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT,
                new YTomorrow(), Collections.emptyList());
    }
    //@@author A0164032U
    @Test
    public void execute_clear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generatePerson(1));
        model.addTask(helper.generatePerson(2));
        model.addTask(helper.generatePerson(3));

        assertCommandSuccess("clear all", ClearCommand.MESSAGE_SUCCESS_ALL, new YTomorrow(), Collections.emptyList());
    }
    //@@author

    //@@author A0164032U
    @Test
    public void execute_add_invalidPersonData() {
        assertCommandFailure("add ??? in valid group",
                AddCommand.MESSAGE_NONAME + String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

    }

    //@@author A0164032U
    @Test
    public void execute_add_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.adam();
        YTomorrow expectedAB = new YTomorrow();
        expectedAB.addTask(toBeAdded);

        // execute command and verify result
        assertCommandSuccess(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS
                        + (toBeAdded.hasPassed() ? "\n" + AddCommand.MESSAGE_PASSEDDATE : ""), toBeAdded),
                expectedAB,
                expectedAB.getTaskList());

    }
    //@@author

    @Test
    public void execute_addDuplicate_notAllowed() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.adam();

        // setup starting state
        model.addTask(toBeAdded); // person already in internal address book

        // execute command and verify result
        assertCommandFailure(helper.generateAddCommand(toBeAdded),  AddCommand.MESSAGE_DUPLICATE_TASK);

    }


    @Test
    public void execute_list_showsAllPersons() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        YTomorrow expectedAB = helper.generateAddressBook(2);
        List<? extends ReadOnlyTask> expectedList = expectedAB.getTaskList();

        // prepare address book state
        helper.addToModel(model, 2);

        assertCommandSuccess("list",
                ListCommand.MESSAGE_SUCCESS,
                expectedAB,
                expectedList);
    }

    //@@author A0164466X
    @Test
    public void execute_listIncomplete_messageTest() throws Exception {
        assertCommandMessageSuccess("li", ListIncompleteCommand.MESSAGE_SUCCESS);
    }

    @Test
    public void execute_listComplete_messageTest() throws Exception {
        assertCommandMessageSuccess("lc", ListCompleteCommand.MESSAGE_SUCCESS);
    }
    //@@author


    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single person in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single person in the last shown list
     *                    based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage)
            throws Exception {
        assertCommandFailure(commandWord , expectedMessage); //index missing
        assertCommandFailure(commandWord + " +1", expectedMessage); //index should be unsigned
        assertCommandFailure(commandWord + " -1", expectedMessage); //index should be unsigned
        assertCommandFailure(commandWord + " 0", expectedMessage); //index cannot be 0
        assertCommandFailure(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single person in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single person in the last shown list
     *                    based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Task> personList = helper.generatePersonList(2);

        // set AB state to 2 persons
        model.resetData(new YTomorrow());
        for (Task p : personList) {
            model.addTask(p);
        }

        assertCommandFailure(commandWord + " 3", expectedMessage);
    }

    @Test
    public void execute_selectInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("select", expectedMessage);
    }

    @Test
    public void execute_selectIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("select");
    }

    @Test
    public void execute_select_jumpsToCorrectPerson() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threePersons = helper.generatePersonList(3);

        YTomorrow expectedAB = helper.generateAddressBook(threePersons);
        helper.addToModel(model, threePersons);

        assertCommandSuccess("select 2",
                String.format(SelectCommand.MESSAGE_SELECT_TASK_SUCCESS, 2),
                expectedAB,
                expectedAB.getTaskList());
        assertEquals(1, targetedJumpIndex);
        assertEquals(model.getFilteredTaskList().get(1), threePersons.get(1));
    }

    //@@author A0164032U
    @Test
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertCommandFailure("delete", expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete");
    }

    @Test
    public void execute_delete_removesCorrectPerson() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threePersons = helper.generatePersonList(3);

        YTomorrow expectedAB = helper.generateAddressBook(threePersons);
        expectedAB.removeTask(threePersons.get(1));
        helper.addToModel(model, threePersons);

        assertCommandSuccess("delete 2",
                String.format(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, threePersons.get(1)),
                expectedAB,
                expectedAB.getTaskList());
    }


    @Test
    public void execute_find_invalidArgsFormat() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertCommandFailure("find ", expectedMessage);
    }

    @Test
    public void execute_find_onlyMatchesFullWordsInNames() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generatePersonWithName("bla bla KEY bla");
        Task pTarget2 = helper.generatePersonWithName("bla KEY bla bceofeia");
        Task p1 = helper.generatePersonWithName("KE Y");
        Task p2 = helper.generatePersonWithName("KEYKEYKEY sduauo");

        List<Task> fourPersons = helper.generatePersonList(p1, pTarget1, p2, pTarget2);
        YTomorrow expectedAB = helper.generateAddressBook(fourPersons);
        List<Task> expectedList = helper.generatePersonList(pTarget1, pTarget2);
        helper.addToModel(model, fourPersons);

        assertCommandSuccess("find KEY",
                Command.getMessageForTaskListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_find_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task p1 = helper.generatePersonWithName("bla bla KEY bla");
        Task p2 = helper.generatePersonWithName("bla KEY bla bceofeia");
        Task p3 = helper.generatePersonWithName("key key");
        Task p4 = helper.generatePersonWithName("KEy sduauo");

        List<Task> fourPersons = helper.generatePersonList(p3, p1, p4, p2);
        YTomorrow expectedAB = helper.generateAddressBook(fourPersons);
        List<Task> expectedList = fourPersons;
        helper.addToModel(model, fourPersons);

        assertCommandSuccess("find KEY",
                Command.getMessageForTaskListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_find_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generatePersonWithName("bla bla KEY bla");
        Task pTarget2 = helper.generatePersonWithName("bla rAnDoM bla bceofeia");
        Task pTarget3 = helper.generatePersonWithName("key key");
        Task p1 = helper.generatePersonWithName("sduauo");

        List<Task> fourPersons = helper.generatePersonList(pTarget1, p1, pTarget2, pTarget3);
        YTomorrow expectedAB = helper.generateAddressBook(fourPersons);
        List<Task> expectedList = helper.generatePersonList(pTarget1, pTarget2, pTarget3);
        helper.addToModel(model, fourPersons);

        assertCommandSuccess("find key rAnDoM",
                Command.getMessageForTaskListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }


    /**
     * A utility class to generate test data.
     */
    class TestDataHelper {
        //@@author A0164889E
        Task adam() throws Exception {
            Name name = new Name("Adm Brown");
            EndDate privateEndDate = new EndDate("12.11");
            StartDate privateStartDate = new StartDate("12.00");
            Group privateGroup = new Group("leisure time");
            return new Task(name, privateStartDate, privateEndDate,
                    privateGroup, UniqueTagList.build(Tag.TAG_INCOMPLETE));
        }

        //@@author A0164889E
        /**
         * Generates a valid person using the given seed.
         * Running this function with the same parameter values guarantees the returned person will have the same state.
         * Each unique seed will generate a unique Person object.
         *
         * @param seed used to generate the person data field values
         */
        Task generatePerson(int seed) throws Exception {
            return new Task(
                    new Name("Person " + seed),
                    new StartDate("0" + Math.abs(seed) + ".0" + Math.abs(seed)),
                    new EndDate("0" + Math.abs(seed) + ".0" + Math.abs(seed)),
                    new Group("list of " + seed),
                    new UniqueTagList(new Tag("tag" + Math.abs(seed)), new Tag("tag" + Math.abs(seed + 1)))
                    );
        }

        //@@author A0164032U
        /** Generates the correct add command based on the task given */
        String generateAddCommand(ReadOnlyTask p) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("add ");

            cmd.append(p.getName().toString());
            cmd.append(" from ").append(p.getStartDate());
            cmd.append(" to ").append(p.getEndDate());
            cmd.append(" in ").append(p.getGroup());

            return cmd.toString();
        }
        //@@author

        //@@author A0164466X
        /** Generates the complete list */
        String generateListCompleteCommand(Model model) {
            model.updateFilteredListToShowComplete();
            return "lc";
        }

        /** Generates the incomplete list */
        String generateListIncompleteCommand(Model model) {
            model.updateFilteredListToShowIncomplete();
            return "li";
        }

        /** Generates the correct mark command based on the index given */
        String generateMarkCommand(int index) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("mark ");
            cmd.append(index);

            return cmd.toString();
        }

        /** Generates the correct unmark command based on the index given */
        String generateUnmarkCommand(int index) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("unmark ");
            cmd.append(index);

            return cmd.toString();
        }

        /**
         * Generates an AddressBook with auto-generated persons.
         */
        YTomorrow generateAddressBook(int numGenerated) throws Exception {
            YTomorrow addressBook = new YTomorrow();
            addToAddressBook(addressBook, numGenerated);
            return addressBook;
        }

        /**
         * Generates an AddressBook based on the list of Persons given.
         */
        YTomorrow generateAddressBook(List<Task> persons) throws Exception {
            YTomorrow addressBook = new YTomorrow();
            addToAddressBook(addressBook, persons);
            return addressBook;
        }

        /**
         * Adds auto-generated Person objects to the given AddressBook
         * @param addressBook The AddressBook to which the Persons will be added
         */
        void addToAddressBook(YTomorrow addressBook, int numGenerated) throws Exception {
            addToAddressBook(addressBook, generatePersonList(numGenerated));
        }

        /**
         * Adds the given list of Persons to the given AddressBook
         */
        void addToAddressBook(YTomorrow addressBook, List<Task> personsToAdd) throws Exception {
            for (Task p: personsToAdd) {
                addressBook.addTask(p);
            }
        }

        /**
         * Adds auto-generated Person objects to the given model
         * @param model The model to which the Persons will be added
         */
        void addToModel(Model model, int numGenerated) throws Exception {
            addToModel(model, generatePersonList(numGenerated));
        }

        /**
         * Adds the given list of Persons to the given model
         */
        void addToModel(Model model, List<Task> personsToAdd) throws Exception {
            for (Task p: personsToAdd) {
                model.addTask(p);
            }
        }

        /**
         * Generates a list of Persons based on the flags.
         */
        List<Task> generatePersonList(int numGenerated) throws Exception {
            List<Task> persons = new ArrayList<>();
            for (int i = 1; i <= numGenerated; i++) {
                persons.add(generatePerson(i));
            }
            return persons;
        }

        List<Task> generatePersonList(Task... persons) {
            return Arrays.asList(persons);
        }

        //@@author A0164889E
        /**
         * Generates a Person object with given name. Other fields will have some dummy values.
         */
        Task generatePersonWithName(String name) throws Exception {
            return new Task(
                    new Name(name),
                    new StartDate("12.21"),
                    new EndDate("12.11"),
                    new Group("list of 1"),
                    new UniqueTagList(new Tag("tag"))
                    );
        }
    }
}
