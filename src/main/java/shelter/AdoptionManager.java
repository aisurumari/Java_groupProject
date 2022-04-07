package shelter;

import shelter.animal.Animal;
import shelter.animal.Dog;
import shelter.animal.Cat;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AdoptionManager {

    private static final String nameRegex  = "[a-zą-żA-ZĄ-ŻóÓ]*(-[a-zą-żA-ZĄ-ŻóÓ]*)?";
    private static final String phoneRegex = "(\\+\\d\\d)? ?((\\d\\d\\d(-| )?){3}|\\d\\d\\d(-| )(\\d\\d(-| )?){3})";
    private static final String emailRegex = "([a-z0-9]+\\.)?[a-z0-9]+(\\+[a-z0-9]*)?@[a-z]+\\.[a-z]+";

    public static ArrayList<Adoption> getAdoptions() {

        ArrayList<Adoption> list = new ArrayList<>();

        Connection connection = new DatabaseConnection().getConnection();

        String query = "SELECT * FROM adoptions JOIN animals USING (animal_id) JOIN adopters USING (adopter_id)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.execute();
            ResultSet results = statement.getResultSet();

            while (results.next()) {

                int id = results.getInt(3);
                int adopterId = results.getInt(1);
                int animalId = results.getInt(2);

                LocalDateTime date = results.getTimestamp(4).toLocalDateTime();

                String firstName = results.getString(8);
                String lastName  = results.getString(9);
                String phone     = results.getString(10);
                String email     = results.getString(11);

                Animal animal = null;
                String name  = results.getString(5);
                String breed = results.getString(7);

                switch (results.getString(6)) {
                    case "dog" -> animal = new Dog(animalId, name, breed);
                    case "cat" -> animal = new Cat(animalId, name, breed);
                }

                list.add(new Adoption(id, new Adopter(adopterId, firstName, lastName, phone, email), animal, date));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static Adoption addAdoption(int adopterID, int animalID) throws Exception {

        Connection connection = new DatabaseConnection().getConnection();

        String query = "INSERT INTO adoptions (date, animal_id, adopter_id) VALUES (?,?,?)";
        try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            LocalDateTime now = LocalDateTime.now();

            statement.setTimestamp(1, Timestamp.valueOf(now));
            statement.setInt(2, animalID);
            statement.setInt(3, adopterID);

            statement.executeUpdate();


            int id;
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            } else {
                throw new Exception();
            }

            PreparedStatement getInfo = connection.prepareStatement("SELECT * FROM adopters, animals WHERE adopter_id=? AND animal_id=?");
            getInfo.setInt(1, adopterID);
            getInfo.setInt(2, animalID);
            getInfo.execute();

            ResultSet results = getInfo.getResultSet();
            if (results.next()) {
                connection.commit();

                int adopterId = results.getInt(1);
                int animalId = results.getInt(6);

                String firstName = results.getString(2);
                String lastName  = results.getString(3);
                String phone     = results.getString(4);
                String email     = results.getString(5);

                Animal animal = null;
                String name  = results.getString(7);
                String breed = results.getString(9);

                switch (results.getString(8)) {
                    case "dog" -> animal = new Dog(animalId, name, breed);
                    case "cat" -> animal = new Cat(animalId, name, breed);
                }

                return new Adoption(id, new Adopter(adopterId, firstName, lastName, phone, email), animal, now);
            } else {
                throw new Exception();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Nieoczekiwany błąd bazy danych");
        }
    }

    public static int addAdopter(String firstName, String lastName, String phone, String mail) throws Exception {

        if (!firstName.matches(nameRegex) || !lastName.matches(nameRegex)) {
            throw new Exception("Nieprawidłowe imię adoptującego");
        }

        if (!phone.matches(phoneRegex)) {
            throw new Exception("Nieprawidłowy numer telefonu");
        }

        if (!mail.matches(emailRegex)) {
            throw new Exception("Nieprawidłowy adres email");
        }

        Connection connection = new DatabaseConnection().getConnection();
        String query = "INSERT INTO adopters (first_name, last_name, phone_number, email) VALUES (?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, phone);
            statement.setString(4, mail);

            statement.executeUpdate();
            connection.commit();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            } else {
                throw new Exception();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Nieoczekiwany błąd bazy danych");
        }
    }
}
