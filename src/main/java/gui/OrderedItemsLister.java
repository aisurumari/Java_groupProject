package gui;

import com.sun.javafx.event.EventHandlerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import shelter.Shelter;
import shelter.inventory.Item;
import shelter.inventory.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderedItemsLister implements ObjectLister {

    private final ObservableList<OrderRow> itemList = FXCollections.observableArrayList();

    public void createColumns(TableView tableView, TextField filterField) {

        ListerCreator<OrderRow> creator = new ListerCreator<>();

        String[] labels = {"ID", "Nazwa", "Ilość", "Data zamówienia", "Data dostarczenia"};
        String[] binds = {"id", "name", "quantity", "dateOrdered", "dateDeliveredNode"};
        ListerCreator.FilterTester<OrderRow> filter = (o, text) ->
                o.getName().toLowerCase().contains(text) ||
                o.getDateDelivered().contains(text)      ||
                o.getDateOrdered().contains(text);

        creator.createTable(labels, binds, itemList, tableView, this::getOrders, filterField, filter);
    }

    public TextField[] createForm(HBox row) {
        VBox parent = (VBox)row.getParent();
        parent.getChildren().remove(row);

        String[] labels = { "Nazwa", "Ilość" };

        return ListerCreator.createForm(labels, parent);
    }

    public void addItem(String[] inputs) throws Exception {
        System.out.println("Zamowiono: " + inputs[0] + ", sztuk - " + inputs[1]);
        itemList.add(new OrderRow(Shelter.addOrder(inputs)));
    }

    EventHandler<ActionEvent> confirm = actionEvent -> {

        Button button = (Button)actionEvent.getSource();

        for (var c : itemList) {
            if (c.node != null && c.node.equals(button)) {
                c.node = null;
                try {
                    Shelter.finishOrder(c.order);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Wystąpił błąd");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                }
                break;
            }
        }

        TableRow row = (TableRow)button.getParent().getParent();
        row.getTableView().refresh();
    };

    public class OrderRow {

        Order order;
        Node node;

        public OrderRow(Order order) {
            this.order = order;
        }

        public Integer getId() {
            return order.getId();
        }

        public String getName() {
            return order.getName();
        }

        public Integer getQuantity() {
            return order.getQuantity();
        }

        public String getDateOrdered() {
            return order.getDateOrdered();
        }

        public String getDateDelivered() {
            return order.getDateDelivered();
        }

        public Node getDateDeliveredNode() {
            String date = order.getDateDelivered();
            if (date.equals("")) {
                Button button = new Button("Potwierdź dostarczenie");
                button.setOnAction(confirm);
                node = button;
                return button;
            } else {
                return new Label(date);
            }
        }
    }

    private ArrayList<OrderRow> getOrders() {
        ArrayList<OrderRow> list = new ArrayList<>();
        for(var o : Shelter.getOrderedItems()) {
            list.add(new OrderRow(o));
        }
        return list;
    }
}
