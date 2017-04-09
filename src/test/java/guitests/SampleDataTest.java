package guitests;

import org.junit.Test;

import seedu.address.testutil.TestUtil;
import seedu.task.model.YTomorrow;

public class SampleDataTest extends AddressBookGuiTest {
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
    public void addressBook_dataFileDoesNotExist_loadSampleData() throws Exception {
        assertListSize(50);
    }
}
