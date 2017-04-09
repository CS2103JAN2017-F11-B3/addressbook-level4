package guitests;

import org.junit.Test;

import seedu.address.logic.Logic;
import seedu.address.model.YTomorrow;
import seedu.address.testutil.TestUtil;

public class SampleDataTest extends AddressBookGuiTest {

    Logic logic;

    @Override
    protected YTomorrow getInitialData() {
        // return null to force test app to load data from file only
        return null;
    }

    @Override
    protected String getDataFileLocation() {
        // return a non-existent file location to force test app to load sample data
        return TestUtil.getFilePathInSandboxFolder("SomeFileThatDoesNotExist1234567890.xml");
    }

    //@@author A0164889E
    @Test
    public void addressBook_dataFileDoesNotExist_genrateSampleData() throws Exception {
        //Comparator<ReadOnlyTask> comparator = Comparator.comparing(ReadOnlyTask::getEndTime);
        //Arrays.sort(expectedList, comparator);
        assertListSize(50);
    }
}
