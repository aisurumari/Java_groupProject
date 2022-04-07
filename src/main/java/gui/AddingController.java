package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddingController implements Initializable {



    ObjectLister objectLister;

    @FXML
    Button commitButton;

    @FXML
    HBox itemRow;

    TextField[] inputs;


    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        commitButton.setOnAction(onButtonCommit);
    }

    public void init(ObjectLister objectLister) {
        this.objectLister = objectLister;
        inputs = objectLister.createForm(itemRow);
    }

    EventHandler<ActionEvent> onButtonCommit = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            String[] data = new String[inputs.length];
            for (int i = 0; i < inputs.length; i++) {
                data[i] = inputs[i].getText();
            }

            try {
                objectLister.addItem(data);

                Stage stage = (Stage)commitButton.getScene().getWindow();
                stage.close();
            } catch (Exception e) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Niepowodzenie");
                alert.setHeaderText(e.getMessage());
                //alert.setContentText("Ooops, there was an error!");

                alert.showAndWait();

            }
        }
    };
}

