package shelter.animal;

public class Cat extends Animal {

    public Cat(int id, String name, String breed) {
        super(id, name, breed);
    }

    public String getType() {
        return "Kot";
    }
}
