package shelter.employee;

import shelter.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

public class EmployeeManager {

    private static final String nameRegex = "[a-zą-żA-ZĄ-ŻóÓ]*(-[a-zą-żA-ZĄ-ŻóÓ]*)?";
    private static final Set<String> vetNames = Set.of("vet", "weterynarz", "lekarz");
    private static final Set<String> janitorNames = Set.of("janitor", "woźny", "sprzątający", "sprzątacz");
    private static final Set<String> volunteerNames = Set.of("volunteer", "wolontariusz" );

//    private static ArrayList<String> jobs;
//
//    public EmployeeManager() {
//        Connection connection = new DatabaseConnection().getConnection();
//
//        try {
//            PreparedStatement statement = connection.prepareStatement("SELECT * FROM jobs");
//            if (!statement.execute()) {
//                throw new SQLException();
//            }
//
//            ResultSet results = statement.getResultSet();
//            while (results.next()) {
//                jobs.add(results.getString(1));
//            }
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public static ArrayList<Employee> getEmployees() {

        Connection connection = new DatabaseConnection().getConnection();

        ArrayList<Employee> employees = new ArrayList<Employee>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees");

            if (!statement.execute()) {
                throw new SQLException();
            }

            try (ResultSet results = statement.getResultSet()) {

                while (results.next()) {

                    Employee emp = null;

                    switch (results.getString(5)) {
                        case "vet" -> emp = new Vet(results.getInt(1), results.getString(2), results.getString(3), results.getInt(4));
                        case "janitor" -> emp = new Janitor(results.getInt(1), results.getString(2), results.getString(3), results.getInt(4));
                        case "volunteer" -> emp = new Volunteer(results.getInt(1), results.getString(2), results.getString(3), results.getInt(4));
                    }

                    if (emp != null) {
                        employees.add(emp);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public static Employee addEmployee(String name, String surname, String job, int salary) throws Exception {

        Connection connection = new DatabaseConnection().getConnection();

        if(!name.matches(nameRegex) || !surname.matches(nameRegex)) {
            throw new Exception("Nieprawidłowe imię pracownika");
        }

        job = job.toLowerCase();
        if (vetNames.contains(job)) {
            job = "vet";
        } else if (janitorNames.contains(job)) {
            job = "janitor";
        } else if (volunteerNames.contains(job)) {
            job = "volunteer";
        }
        else {
            throw new Exception("Nieprawidłowy zawód pracownika");
        }

        String query = "INSERT INTO employees(first_name, last_name, salary, job) VALUES(?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setInt(3, salary);
            statement.setString(4, job);

            statement.executeUpdate();
            connection.commit();

            int id = 0;
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }

            Employee emp = null;

            switch (job) {
                case "vet" -> emp = new Vet(id, name, surname, salary);
                case "janitor" -> emp = new Janitor(id, name, surname, salary);
                case "volunteer" -> emp = new Volunteer(id, name, surname, salary);
            }

            if (emp != null) {
                return emp;
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Problem z bazą danych");
        }

        return null;
    }

}
