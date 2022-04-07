package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shelter.Shelter;

import java.net.URL;
import java.util.ResourceBundle;

public class ShelterController implements Initializable {

    @FXML
    Button showEmployeesButton;
    @FXML
    Button showAnimalsButton;
    @FXML
    Button showAdoptionsButton;
    @FXML
    Button showInventoryButton;

    Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        showEmployeesButton.setOnAction(showEmployees);
        showAnimalsButton.setOnAction(showAnimals);
        showInventoryButton.setOnAction(selectInventory);
        showAdoptionsButton.setOnAction(showAdoptions);

        Shelter shelter = Shelter.getInstance();
    }

    private void showList(ObjectLister objLister, String title) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            Parent root = fxmlLoader.load(getClass().getResource("/listStage.fxml").openStream());
            Scene scene = new Scene(root);
            if (stage == null) {
                stage = (Stage)showEmployeesButton.getScene().getWindow();
            }
            stage.setScene(scene);
            stage.show();
            ListingController controller = fxmlLoader.getController();
            controller.init(objLister, title);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    EventHandler<ActionEvent> showEmployees     = actionEvent -> showList(new EmployeeLister()    , "Lista pracowników"    );
    EventHandler<ActionEvent> showAnimals       = actionEvent -> showList(new AnimalLister()      , "Lista zwierząt"       );
    EventHandler<ActionEvent> showInventory     = actionEvent -> showList(new InventoryLister()   , "Inwentarz"            );
    EventHandler<ActionEvent> showOrderedItems  = actionEvent -> showList(new OrderedItemsLister(), "Zamówione przedmioty" );
    EventHandler<ActionEvent> showAdoptions     = actionEvent -> showList(new AdoptionLister()    , "Lista adopcji"        );

    EventHandler<ActionEvent> selectInventory = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            stage = (Stage)showEmployeesButton.getScene().getWindow();

            VBox container = (VBox)showInventoryButton.getParent();
            container.getChildren().clear();

            Button showInv    = new Button("Inwentarz schroniska");
            Button showOrders = new Button("Zamówione przedmioty");
            showInv.setPrefWidth(150);
            showOrders.setPrefWidth(150);
            container.getChildren().addAll(showInv, showOrders);

            showInv.setOnAction(showInventory);
            showOrders.setOnAction(showOrderedItems);
        }
    };
}
