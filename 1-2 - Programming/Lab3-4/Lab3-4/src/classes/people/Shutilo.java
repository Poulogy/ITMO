package classes.people;
import classes.places.Place;

public class Shutilo extends Character {
    public Shutilo(String name, Place place) {
        super(name, place);
    }

    @Override
    public String toString() {
        return getName();
    }
}