package shelter.employee;

public abstract class Employee {

    int id;
    String firstName;
    String secondName;
    int salary;

    public Employee(int id, String name, String surname, int salary) {
        this.id = id;
        firstName = name;
        secondName = surname;
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public Integer getSalary() {
        return salary;
    }

    public String getJob() {
        return "";
    }
}
