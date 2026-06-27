package classes.places;

import classes.people.Character;


public class PoliceStation extends Place {
    public PoliceStation(String name) {
        super(name);
    }
    @Override
    protected String changeLocation(Character character, String loc) {
        switch (loc) {
            case "ENTER":
                return character.getName() + " заходит в " + getName();
            case "EXIT":
                return character.getName() + " уходт из " + getName();
            default:
                return "";
        }
    }
    @Override
    public String toString() {
        return getName();
    }
}
