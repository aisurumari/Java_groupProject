package gui;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public interface ObjectLister {
    void createColumns(TableView tableView, TextField filteredField);
    TextField[] createForm(HBox row);
    void addItem(String[] inputs) throws Exception;
}
