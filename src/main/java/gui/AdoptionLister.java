package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import shelter.Adoption;
import shelter.Shelter;

import java.util.ArrayList;

public class AdoptionLister implements ObjectLister {

    private final ObservableList<AdoptionRow> adoptionsList = FXCollections.observableArrayList();

    @Override
    public void createColumns(TableView table, TextField filterField) {
        ListerCreator<AdoptionRow> creator = new ListerCreator<>();

        String[] labels = {"ID", "Adoptujący", "Zwierzę", "Data adopcji"};
        String[] binds = {"id", "adopter", "animal", "date"};
        ListerCreator.FilterTester<AdoptionRow> filter = (a, text) ->
                String.valueOf(a.getId()).contains(text)        ||
                a.getFullName().toLowerCase().contains(text)    ||
                a.getPhoneNumber().toLowerCase().contains(text) ||
                a.getEmail().toLowerCase().contains(text)       ||
                a.getAnimalName().toLowerCase().contains(text)  ||
                a.getDate().toLowerCase().contains(text);

        creator.createTable(labels, binds, adoptionsList, table, this::getAdoptionRows, filterField, filter);
    }

    ObservableList<Node> formNodes1;
    ObservableList<Node> formNodes2;
    TextField[] formTextFields;

    @Override
    public TextField[] createForm(HBox row) {

        VBox parent = (VBox)row.getParent();
        parent.getChildren().remove(row);

        Button btn1 = new Button("Dodaj nowego adoptującego");
        Button btn2 = new Button("Podaj ID istniejącego adoptującego");

        String[] l1 = { "ID adoptującego", "ID zwierzęcia" };
        TextField[] t1 = ListerCreator.createForm(l1, parent);
        parent.getChildren().addAll(btn1);
        formNodes1 = FXCollections.observableArrayList(parent.getChildren());
        parent.getChildren().clear();

        String[] l2 = { "Imię", "Nazwisko", "Adres e-mail", "Numer tel.", "ID zwierzęcia" };
        TextField[] t2 = ListerCreator.createForm(l2, parent);
        parent.getChildren().addAll(btn2);
        formNodes2 = FXCollections.observableArrayList(parent.getChildren());

        formTextFields = t2;

        btn1.setOnAction(e -> { parent.getChildren().setAll(formNodes2); formTextFields = t2; });
        btn2.setOnAction(e -> { parent.getChildren().setAll(formNodes1); formTextFields = t1; });

        return formTextFields;
    }

    @Override
    public void addItem(String[] inputs) throws Exception {

        AdoptionRow a;

        if (formTextFields.length == 2) {
            //adopter exists
            int adopterID, animalID;
            try {
                adopterID = Integer.parseInt(formTextFields[0].getText());
                animalID  = Integer.parseInt(formTextFields[1].getText());
            } catch (Exception e) {
                throw new Exception("Nieprawidłowe ID");
            }

            a = new AdoptionRow(Shelter.addAdoption(adopterID, animalID, false));
        } else {
            //new adopter
            a = new AdoptionRow(Shelter.addAdoption(new String[] {
                    formTextFields[0].getText(),
                    formTextFields[1].getText(),
                    formTextFields[2].getText(),
                    formTextFields[3].getText(),
                    formTextFields[4].getText()
            }));
        }

        adoptionsList.add(a);
    }

    public class AdoptionRow {

        Adoption adoption;

        public AdoptionRow(Adoption a) {
            adoption = a;
        }

        public ComboBox<String> getAdopter() {
            ComboBox<String> box = new ComboBox<>();
            box.setMaxWidth(200);
            box.setPromptText(getFullName());
            box.getItems().addAll(
                    getFullName(),
                    adoption.getAdopter().getEmail(),
                    adoption.getAdopter().getPhoneNumber(),
                    "ID: " + adoption.getAdopter().getId());
            box.setOnAction(event -> box.setValue(getFullName()));
            return box;
        }

        public ComboBox<String> getAnimal() {
            ComboBox<String> box = new ComboBox<>();
            box.setMaxWidth(100);
            box.setPromptText(getAnimalName());
            box.getItems().addAll(
                    getAnimalName(),
                    adoption.getAnimal().getType(),
                    adoption.getAnimal().getBreed(),
                    "ID: " + adoption.getAnimal().getId());
            box.setOnAction(event -> box.setValue(getAnimalName()));
            return box;
        }

        public Integer getId() {
            return adoption.getId();
        }

        public String getFirstName() {
            return adoption.getAdopter().getFirstName();
        }

        public String getSurname() {
            return adoption.getAdopter().getSurname();
        }

        public String getFullName() {
            return getFirstName() + " " + getSurname();
        }

        public String getPhoneNumber() {
            return adoption.getAdopter().getPhoneNumber();
        }

        public String getEmail() {
            return adoption.getAdopter().getEmail();
        }

        public String getAnimalName() {
            return adoption.getAnimal().getName();
        }

        public String getDate() {
            return adoption.getDateString();
        }
    }

    private ArrayList<AdoptionRow> getAdoptionRows() {
        ArrayList<AdoptionRow> list = new ArrayList<>();
        for (var a : Shelter.getAdoptions()) {
            list.add(new AdoptionRow(a));
        }
        return list;
    }
}
