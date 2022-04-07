package gui;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ListerCreator<T> {

    public interface FilterTester<T> {
        boolean filter(T obj, String text);
    }

    public void createTable(String[] labels, String[] binds, ObservableList<T> list,
                            TableView<T> table, Callable<ArrayList<T>> getter,
                            TextField filterField, FilterTester<T> filter) {

        for (int i = 0; i < labels.length; i++) {
            TableColumn<T, String> col = new TableColumn<>(labels[i]);
            col.setCellValueFactory(new PropertyValueFactory<>(binds[i]));
            table.getColumns().add(col);
        }

        try {
            list.addAll(getter.call());
        } catch (Exception e) {
            e.printStackTrace();
        }

        FilteredList<T> filteredList = new FilteredList<>(list, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(item -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String filterText = newValue.toLowerCase();

                return filter.filter(item, filterText);
            });
        });

        SortedList<T> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedList);
    }

    public static TextField[] createForm(String[] labels, VBox parent) {
        TextField[] inputs = new TextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            Label label = new Label(labels[i]);
            TextField txtField = new TextField();
            inputs[i] = txtField;
            HBox newBox = new HBox();
            newBox.getChildren().addAll(label, txtField);
            parent.getChildren().add(newBox);
        }

        return inputs;
    }

}
