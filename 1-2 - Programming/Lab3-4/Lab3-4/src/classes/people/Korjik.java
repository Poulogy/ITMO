package classes.people;
import classes.places.Place;


public class Korjik extends Character{
    public Korjik(String name, Place place) {
        super(name, place);
    }

    @Override
    public String toString() {
        return getName();
    }

}
