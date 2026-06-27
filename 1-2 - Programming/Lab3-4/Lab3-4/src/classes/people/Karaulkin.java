package classes.people;
import classes.places.Place;

public class Karaulkin extends Character {
    public Karaulkin(String name, Place place) {
        super(name, place);
    }
    @Override
    public String toString() {
        return getName();
    }
}
