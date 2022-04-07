package shelter;

public class Adopter {

    int id;
    String firstName;
    String surname;
    String phoneNumber;
    String email;

    public Adopter(int id, String firstName, String surname, String phoneNumber, String email) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }





}
