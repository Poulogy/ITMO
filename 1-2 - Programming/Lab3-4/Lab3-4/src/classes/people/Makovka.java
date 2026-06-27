package classes.people;
import classes.places.Place;

public class Makovka extends Character {
    public Makovka(String name, Place place) {
        super(name, place);
    }

    @Override
    public String toString() {
        return getName();
    }
}
