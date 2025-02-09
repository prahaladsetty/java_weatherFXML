package weatherfxml;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label text_data;

    private StringProperty labelText = new SimpleStringProperty("ok buddy");

    public StringProperty labelTextProperty() {
        return labelText;
    }

    public String getLabelText() {
        return labelText.get();
    }

    public void setLabelText(String text) {
        this.labelText.set(text);
    }

    @FXML
    public void initialize() {
        text_data.textProperty().bind(labelText);
    }

    public void updateLabelText(String newText) {
        setLabelText(newText);
    }


}