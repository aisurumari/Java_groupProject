package shelter.animal;

public abstract class Animal {

    private int id;
    private String name;
    private String breed;

    public Animal(int id, String name, String breed) {
        this.id = id;
        this.name = name;
        this.breed = breed;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return "";
    }

    public String getBreed() {
        return breed;
    }
}
