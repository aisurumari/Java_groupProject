package shelter.employee;

public class Vet extends Employee {

    public Vet(int id, String name, String surname, int salary) {
        super(id, name, surname, salary);
    }

    public String getJob() {
        return "Weterynarz";
    }
}
