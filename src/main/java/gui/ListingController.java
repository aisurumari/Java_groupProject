package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ListingController implements Initializable {


    ObjectLister objectLister;

    @FXML
    Button backToMainButton;

    @FXML
    Button addItemButton;

    @FXML
    TableView tableView;

    @FXML
    Label listTitle;

    @FXML
    TextField filterTextField;



    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        backToMainButton.setOnAction(backToMain);
        addItemButton.setOnAction(addItem);

        tableView.setPlaceholder(new Label("Brak wyników"));
    }

    public void init(ObjectLister objectLister, String title) {
        this.objectLister = objectLister;
        objectLister.createColumns(tableView, filterTextField);
        listTitle.setText(title);
    }

    EventHandler<ActionEvent> backToMain = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                Parent root = fxmlLoader.load(getClass().getResource("/mainStage.fxml").openStream());
                Scene scene = new Scene(root);
                Stage stage = (Stage)backToMainButton.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    };

    EventHandler<ActionEvent> addItem = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Stage newStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                Parent root = fxmlLoader.load(getClass().getResource("/addItemStage.fxml").openStream());
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
                newStage.setScene(scene);
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.initOwner(addItemButton.getScene().getWindow());

                AddingController controller = fxmlLoader.getController();
                controller.init(objectLister);

                newStage.showAndWait();
                tableView.refresh();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    };
}
