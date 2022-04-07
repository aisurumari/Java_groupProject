package shelter.employee;

public class Janitor extends Employee {

    public Janitor(int id, String name, String surname, int salary) {
        super(id, name, surname, salary);
    }

    public String getJob() {
        return "Woźny";
    }
}
