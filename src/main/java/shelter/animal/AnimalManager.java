package shelter.animal;

import shelter.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

public class AnimalManager {

    private static final String nameRegex = "[a-zą-żA-ZĄ-ŻóÓ]*";
    private static final Set<String> dogNames = Set.of("dog", "pies");
    private static final Set<String> catNames = Set.of("cat", "kot");

    public static ArrayList<Animal> getAnimals() {

        Connection connection = new DatabaseConnection().getConnection();

        ArrayList<Animal> animals = new ArrayList<Animal>();
        String query = "SELECT * FROM animals LEFT JOIN adoptions B USING (animal_id) WHERE B.animal_id IS NULL";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            if (!statement.execute()) {
                throw new SQLException();
            }

            try (ResultSet results = statement.getResultSet()) {

                while (results.next()) {

                    Animal animal = null;

                    int id = Integer.parseInt(results.getString(1));
                    String name = results.getString(2);
                    String breed = results.getString(4);

                    switch (results.getString(3)) {
                        case "dog" -> animal = new Dog(id, name, breed);
                        case "cat" -> animal = new Cat(id, name, breed);
                    }

                    if (animal != null) {
                        animals.add(animal);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return animals;
    }

    public static Animal addAnimal(String name, String type, String breed) throws Exception {

        Connection connection = new DatabaseConnection().getConnection();

        if (!name.matches(nameRegex)) {
            throw new Exception("Nieprawidłowe imię zwierzęcia");
        }

        type = type.toLowerCase();
        if (dogNames.contains(type)) {
            type = "dog";
        } else if (catNames.contains(type)) {
            type = "cat";
        } else {
            throw new Exception("Nieprawidłowy gatunek zwierzęcia");
        }

        String query = "INSERT INTO animals(name, type, breed) VALUES(?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            statement.setString(1, name);
            statement.setString(2, type);
            statement.setString(3, breed);

            statement.executeUpdate();
            connection.commit();

            int id = 0;
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }

            Animal animal = null;

            switch (type) {
                case "dog" -> animal = new Dog(id, name, breed);
                case "cat" -> animal = new Cat(id, name, breed);
            }

            if (animal != null) {
                return animal;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
