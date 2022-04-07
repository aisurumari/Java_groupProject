package shelter;

import shelter.animal.Animal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Adoption {

    int id;
    Adopter adopter;
    Animal animal;
    LocalDateTime date;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Adoption(int id, Adopter adopter, Animal animal, LocalDateTime date) {
        this.id = id;
        this.adopter = adopter;
        this.animal = animal;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Adopter getAdopter() {
        return adopter;
    }

    public Animal getAnimal() {
        return animal;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDateString() {
        return dtf.format(date);
    }



}
