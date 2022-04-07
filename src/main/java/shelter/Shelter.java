package shelter;

import shelter.animal.Animal;
import shelter.animal.AnimalManager;
import shelter.employee.Employee;
import shelter.employee.EmployeeManager;
import shelter.inventory.Inventory;
import shelter.inventory.Item;
import shelter.inventory.Order;

import java.util.ArrayList;

public class Shelter {

    private static Shelter instance;
    private static ArrayList<Employee> employees;
    private static ArrayList<Animal> animals;
    private static ArrayList<Adoption> adoptions;
    private static ArrayList<Item> items;
    private static ArrayList<Order> itemsOrdered;

    private Shelter() {
        if (instance == null) {
            instance = this;
        }

    }

    public static Shelter getInstance() {
        if (instance == null) {
            instance = new Shelter();
        }
        return instance;
    }

    public static ArrayList<Employee> getEmployees() {
        if (employees == null) {
            employees = EmployeeManager.getEmployees();
        }
        return employees;
    }

    public static Employee addEmployee(String[] empData) throws Exception {
        checkData(empData, 4);

        int salary;
        try {
            salary = Integer.parseInt(empData[2]);
        } catch (Exception e) {
            throw new Exception("Nieprawidłowe zarobki pracownika");
        }

        Employee emp = EmployeeManager.addEmployee(empData[0], empData[1], empData[3], salary);
        employees.add(emp);
        return emp;
    }

    public static ArrayList<Animal> getAnimals() {
        if (animals == null) {
            animals = AnimalManager.getAnimals();
        }
        return animals;
    }

    public static Animal addAnimal(String[] data) throws Exception {
        checkData(data, 3);

        Animal animal = AnimalManager.addAnimal(data[0], data[1], data[2]);
        animals.add(animal);
        return animal;
    }

    public static ArrayList<Adoption> getAdoptions() {
        if (adoptions == null) {
            adoptions = AdoptionManager.getAdoptions();
        }
        return adoptions;
    }

    public static Adoption addAdoption(int idAdopter, int idAnimal, boolean newAdopter) throws Exception {

        if (!newAdopter && (adoptions.stream().noneMatch(a -> a.getAdopter().getId() == idAdopter))) {
            throw new Exception("Nie znaleziono adoptującego o podanym ID");
        } else if (getAnimals().stream().noneMatch(a -> a.getId() == idAnimal)) {
            throw new Exception("Nie znaleziono zwierzęcia o podanym ID");
        }

        Adoption adoption = AdoptionManager.addAdoption(idAdopter, idAnimal);
        animals.removeIf(a -> a.getId() == idAnimal);
        adoptions.add(adoption);

        return adoption;
    }

    public static Adoption addAdoption(String[] data) throws Exception {
        checkData(data, 5);
        //data - "Imię", "Nazwisko", "Adres e-mail", "Numer tel.", "ID zwierzęcia"

        if (adoptions.stream().anyMatch(a -> a.getAdopter().getPhoneNumber().equals(data[3]))) {
            throw new Exception("Istnieje już adoptujący o podanym numerze telefonu");
        }

        int adopterId = AdoptionManager.addAdopter(data[0], data[1], data[3], data[2]);
        int animalId;
        try {
            animalId = Integer.parseInt(data[4]);
        } catch (Exception e) {
            throw new Exception("Nieprawidłowe ID zwierzęcia");
        }

        return addAdoption(adopterId, animalId, true);
    }

    public static ArrayList<Item> getItems() {
        if (items == null) {
            items = Inventory.getItems();
        }
        return items;
    }

    public static ArrayList<Order> getOrderedItems() {
        if (itemsOrdered == null) {
            itemsOrdered = Inventory.getOrders();
        }
        return itemsOrdered;
    }

    public static Item addItem(String[] data) throws Exception {
        checkData(data, 2);

        int count;
        try {
            count = Integer.parseInt(data[1]);
        } catch (Exception e) {
            throw new Exception("Nieprawidłowa ilość");
        }

        return addItem(new Item(data[0], count));
    }

    public static Item addItem(Item item) throws Exception {

        Item alreadyExists = getItems().stream().filter(i -> {
            assert item != null;
            return i.getName().equals(item.getName());
        }).findAny().orElse(null);

        if (alreadyExists != null) {
            if (alreadyExists.getCount() < -item.getCount()) {
                throw new Exception("W inwentarzu znajduje się mniej przedmiotów niż próbowano odjąć");
            }
            alreadyExists.addCount(item.getCount());
            Inventory.addItem(item.getName(), item.getCount());
            return null;
        } else if (item.getCount() < 0) {
            throw new Exception("Dodano ujemną liczbę przedmiotów");
        }

        return Inventory.addItem(item.getName(), item.getCount());
    }

    public static Order addOrder(String[] data) throws Exception {
        checkData(data, 2);

        int count;
        try {
            count = Integer.parseInt(data[1]);
        } catch (Exception e) {
            throw new Exception("Nieprawidłowa ilość");
        }

        Order order = Inventory.addOrder(data[0], count);
        itemsOrdered.add(order);
        return order;
    }

    public static void finishOrder(Order order) throws Exception {
        order.finishOrder();
        Inventory.updateOrder(order);
        addItem(order.getItem());
    }

    private static void checkData(String[] data, int desiredCount) throws Exception {
        if (data.length != desiredCount) {
            throw new Exception("Programista źle coś zrobił");
        }

        for (String s : data) {
            if (s.equals("")) {
                throw new Exception("Pozostawione puste pola");
            }
        }
    }
}
