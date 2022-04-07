package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import shelter.animal.Animal;
import shelter.Shelter;

public class AnimalLister implements ObjectLister {

    private final ObservableList<Animal> animalList = FXCollections.observableArrayList();

    @Override
    public void createColumns(TableView tableView, TextField filterField) {

        ListerCreator<Animal> creator = new ListerCreator<>();

        String[] labels = {"ID", "Nazwa", "Typ", "Rasa"};
        String[] binds = {"id", "name", "type", "breed"};
        ListerCreator.FilterTester<Animal> filter = (animal, text) ->
                animal.getName().toLowerCase().contains(text) ||
                animal.getType().toLowerCase().contains(text) ||
                animal.getBreed().toLowerCase().contains(text);

        creator.createTable(labels, binds, animalList, tableView, Shelter::getAnimals, filterField, filter);
    }

    @Override
    public TextField[] createForm(HBox row) {

        VBox parent = (VBox)row.getParent();
        parent.getChildren().remove(row);

        String[] labels = { "Nazwa", "Typ", "Rasa" };

        return ListerCreator.createForm(labels, parent);
    }

    @Override
    public void addItem(String[] inputs) throws Exception {
        animalList.add(Shelter.addAnimal(inputs));
    }
}
