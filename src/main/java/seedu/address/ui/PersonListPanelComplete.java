package seedu.address.ui;

import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;
import seedu.address.model.task.ReadOnlyPerson;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniquePersonList;

//@@author A0164889E
/**
 * Panel containing the list of persons.
 */
public class PersonListPanelComplete extends UiPart<Region> {
    private static final String FXML = "PersonListPanelComplete.fxml";

    @FXML
    private ListView<ReadOnlyPerson> personListViewComplete;

    public PersonListPanelComplete(AnchorPane personListCompletePlaceholder, ObservableList<ReadOnlyPerson> personList) {
        super(FXML);
        setConnections(personList);
        addToPlaceholder(personListCompletePlaceholder);
    }

    private void setConnections(ObservableList<ReadOnlyPerson> personList) {
        //@@author A0164889E
//        UniqueTagList tags;
//        
//        try {
//            tags = new UniqueTagList("complete");
//            for (int i=0; i<personList.size(); i++){
//                if (personList.get(i).getTags().equals(tags)) {
//                    Task toAdd = new Task(personList.get(i));
//                  //  completeList.add(toAdd);
//                }
//            }
//        } catch (DuplicateTagException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalValueException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        //@@author
        personListViewComplete.setItems(personList);
        personListViewComplete.setCellFactory(listView -> new PersonListViewCellComplete());
        setEventHandlerForSelectionChangeEvent();
    }

    private void addToPlaceholder(AnchorPane placeHolderPane) {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
        placeHolderPane.getChildren().add(getRoot());
    }

    private void setEventHandlerForSelectionChangeEvent() {
        personListViewComplete.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        LOGGER.fine("Selection in person list panel changed to : '" + newValue + "'");
                        raise(new PersonPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            personListViewComplete.scrollTo(index);
            personListViewComplete.getSelectionModel().clearAndSelect(index);
        });
    }

    class PersonListViewCellComplete extends ListCell<ReadOnlyPerson> {

        @Override
        protected void updateItem(ReadOnlyPerson person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                //@@author A0164889E
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
//                UniqueTagList tags;
//                try {
//                    tags = new UniqueTagList("complete");
//                    if (person.getTags() == tags) {
//                        setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
//                    }
//                } catch (DuplicateTagException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IllegalValueException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }   
                //@@author
            }
        }
    }

}
