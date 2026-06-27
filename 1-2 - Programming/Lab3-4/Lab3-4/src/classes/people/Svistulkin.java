package classes.people;
import classes.places.Place;
import interfaces.Findable;

public class Svistulkin extends Character {
    public Svistulkin(String name, Place place, int missing) {
        super(name, place, missing);
    }
    @Override
    public String toString() {
        return getName();
    }
}
