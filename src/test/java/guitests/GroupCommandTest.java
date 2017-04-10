package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.TestPerson;
//@@author A0164032U
public class GroupCommandTest extends AddressBookGuiTest {

    @Test
    public void Group() {

        //verify a non-empty list can be grouped
        assertTrue(personListPanel.isListMatching(td.getTypicalPersons()));
        
        //group with only one correspond person
        assertGroupResult("group1", td.alice);
        
        //group with multiple correspond persons
        assertGroupResult("street", new TestPerson[]{td.daniel, td.george, td.carl});
        
        //group an empty group
        assertGroupResult("emptyGroup");
        
        //group after delete one person
        commandBox.runCommand("find Daniel");
        commandBox.runCommand("delete 1");
        assertGroupResult("street" , new TestPerson[]{td.george, td.carl});

    }
    
    private void assertGroupResult(String group, TestPerson... expectedHits) {
        commandBox.runCommand("group " + group);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " tasks listed!");
        assertTrue(personListPanel.isListMatching(expectedHits));
    }
}
