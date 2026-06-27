package interfaces;

import classes.people.Character;
import classes.places.Place;

public interface TalkableLookable {
    void say(Character character);
    void look(Place place);
}
