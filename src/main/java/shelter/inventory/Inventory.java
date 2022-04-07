package shelter.inventory;

import shelter.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Inventory {


    public static ArrayList<Item> getItems() {

        ArrayList<Item> list = new ArrayList<Item>();

        Connection connection = new DatabaseConnection().getConnection();

        String query = "SELECT * FROM items";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            if (!statement.execute()) {
                throw new SQLException();
            }

            try (ResultSet results = statement.getResultSet()) {

                while (results.next()) {

                    Item item = new Item(results.getString(1), results.getInt(2));
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static Item addItem(String name, int quantity) throws Exception {

        Connection connection = new DatabaseConnection().getConnection();

        String query = "INSERT INTO items VALUES (?, ?) ON CONFLICT (type) DO UPDATE SET quantity=items.quantity + ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setInt(2, quantity);
            statement.setInt(3, quantity);

            statement.executeUpdate();
            connection.commit();

            return new Item(name, quantity);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Nieoczekiwany błąd bazy danych");
        }
    }

    public static ArrayList<Order> getOrders() {

        ArrayList<Order> list = new ArrayList<>();

        Connection connection = new DatabaseConnection().getConnection();

        String query = "SELECT * FROM orders";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            if (!statement.execute()) {
                throw new SQLException();
            }

            try (ResultSet results = statement.getResultSet()) {

                while (results.next()) {

                    int id = results.getInt(1);
                    Item item = new Item(results.getString(2), results.getInt(3));
                    Timestamp dateFinished = results.getTimestamp(5);
                    LocalDateTime dateDelivered = null;
                    if (dateFinished != null) {
                        dateDelivered = dateFinished.toLocalDateTime();
                    }

                    Order order = new Order(id, item, results.getTimestamp(4).toLocalDateTime(), dateDelivered);
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static Order addOrder(String name, int quantity) {
        Connection connection = new DatabaseConnection().getConnection();

        String query = "INSERT INTO orders (item_type, quantity, date_ordered, date_delivery) VALUES (?,?,?,NULL)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setInt(2, quantity);
            LocalDateTime now = LocalDateTime.now();
            statement.setTimestamp(3, Timestamp.valueOf(now));

            statement.executeUpdate();
            connection.commit();

            int id = 0;
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }

            return new Order(id, new Item(name, quantity), now, null);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void updateOrder(Order order) {
        Connection connection = new DatabaseConnection().getConnection();

        String query = "UPDATE orders SET date_delivery = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setTimestamp(1, Timestamp.valueOf(order.getDeliveryTimestamp()));
            statement.setInt(2, order.getId());

            statement.executeUpdate();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
