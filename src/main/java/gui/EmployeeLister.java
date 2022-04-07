package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import shelter.employee.Employee;
import shelter.Shelter;

public class EmployeeLister implements ObjectLister {

    private final ObservableList<Employee> empList = FXCollections.observableArrayList();;

    @Override
    public void createColumns(TableView tableView, TextField filterField) {

        ListerCreator<Employee> creator = new ListerCreator<>();

        String[] labels = {"ID", "Imię", "Nazwisko", "Zarobki", "Stanowisko"};
        String[] binds  = {"id", "firstName", "secondName", "salary", "job" };
        ListerCreator.FilterTester<Employee> filter = (e, text) ->
                        e.getFirstName().toLowerCase().contains(text)  ||
                        e.getSecondName().toLowerCase().contains(text) ||
                        e.getJob().toLowerCase().contains(text);

        creator.createTable(labels, binds, empList, tableView, Shelter::getEmployees, filterField, filter);
    }

    @Override
    public TextField[] createForm(HBox row) {

        VBox parent = (VBox)row.getParent();
        parent.getChildren().remove(row);

        String[] labels = { "Imię", "Nazwisko", "Wynagrodzenie", "Stanowisko" };

        return ListerCreator.createForm(labels, parent);
    }

    @Override
    public void addItem(String[] inputs) throws Exception {
        try {
            empList.add(Shelter.addEmployee(inputs));
        } catch (Exception e) {
            throw e;
        }
    }
}
