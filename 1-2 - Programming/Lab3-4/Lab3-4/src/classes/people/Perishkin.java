package classes.people;
import classes.places.Place;
import interfaces.Askable;
import interfaces.Findable;
import interfaces.TalkableLookable;

public class Perishkin extends Character implements Askable, TalkableLookable, Findable {
    public Perishkin(String name, Place place) {
        super(name, place);

    }
    @Override
    public void ask(Character character) {
        System.out.println(getName() + " допрашивает " + character.getName());
    }
    @Override
    public void look(Place place) {
        System.out.println(getName() + " рассматривает разрушения в  " + place.getName());
    }
    @Override
    public void say(Character character){
        System.out.println(getName() + " говорит " + character.getName() + " что ");
    }
    @Override
    public void findOut(Character[] character, Character discovered){
        System.out.print(getName() + " узнаёт от ");
        for (Character peoples: character) {
            System.out.print(peoples.getName() + " ");
        }
        System.out.println("что " + fond(discovered) + " не пропал");
    }

    public String fond(Character character) {
        return character.getName();
    }

    @Override
    public String toString() {
        return getName();
    }
}
