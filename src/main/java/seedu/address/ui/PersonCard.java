package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.task.ReadOnlyPerson;

//@@author A0164032U
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String PASSED_HEX_COLOR = "#f08080";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label end;
    @FXML
    private Label start;
    @FXML
    private Label group;

    public PersonCard(ReadOnlyPerson person, int displayedIndex) {
        super(FXML);
        
        name.setText(person.getName().fullName);
        id.setText(displayedIndex + ". ");
        
        if (person.getEndDate() == null) {
            end.setVisible(false);
            end.setStyle("-fx-font-size: 0pt;");
        } else {
            end.setText("Ends:   " + person.getEndDate());
        }

        if (person.getStartDate() == null) {
            start.setVisible(false);
            start.setStyle("-fx-font-size: 0pt;");
        } else {
            start.setText("Starts: " + person.getStartDate());
        }
        
        group.setText(person.getGroup().value);
        
        if (person.hasPassed()) {
            cardPane.setStyle("-fx-background-color: " + PASSED_HEX_COLOR);
        }
    }
}
