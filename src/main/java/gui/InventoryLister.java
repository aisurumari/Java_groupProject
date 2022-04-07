package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import shelter.Shelter;
import shelter.inventory.Item;

public class InventoryLister implements ObjectLister {

    private final ObservableList<Item> itemList = FXCollections.observableArrayList();

    public void createColumns(TableView tableView, TextField filterField) {

        ListerCreator<Item> creator = new ListerCreator<>();

        String[] labels = {"Nazwa", "Ilość"};
        String[] binds = {"name", "count"};
        ListerCreator.FilterTester<Item> filter = (i, text) -> i.getName().toLowerCase().contains(text);

        creator.createTable(labels, binds, itemList, tableView, Shelter::getItems, filterField, filter);
    }

    public TextField[] createForm(HBox row) {

        VBox parent = (VBox)row.getParent();
        parent.getChildren().remove(row);

        String[] labels = { "Nazwa", "Ilość" };

        return ListerCreator.createForm(labels, parent);
    }

    public void addItem(String[] inputs) throws Exception {
        Item item = Shelter.addItem(inputs);
        if (item != null ) {
            itemList.add(item);
        }
    }
}
