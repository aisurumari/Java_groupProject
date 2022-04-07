package shelter.employee;

public class Volunteer extends Employee {

    public Volunteer(int id, String name, String surname, int salary) {
        super(id, name, surname, salary);
    }

    public String getJob() {
        return "Wolontariusz";
    }
}
