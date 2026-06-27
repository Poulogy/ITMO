package classes.places;
import java.util.ArrayList;
import classes.people.Character;
public abstract class Place {
    protected String name;
    protected ArrayList<Character> peoples;
    public Place(String name) {
        this.name = name;
        this.peoples = new ArrayList<>();
    }
    protected abstract String changeLocation(Character character, String loc);
    public void enter(Character character)  {
        if(!peoples.contains(character)) {
            peoples.add(character);
        }
        String action = changeLocation(character, "ENTER");
        System.out.println(action);
    }
    public void exit(Character character) {
        if(peoples.contains(character)) {
            peoples.remove(character);
        }
        String action = changeLocation(character, "EXIT");
        System.out.println(action);
    }
    public String getName() {
        return name;
    }

    public ArrayList<Character> getPeoples() {
        return peoples;
    }

    public Character showCharactersInPlace() {
        if(!peoples.isEmpty()) {
            System.out.println("В " + getName() + " находятся ");
            for(Character character: peoples) {
                return character;
            }
        } else {
            System.out.println("В " + getName() + " никого нет");
            return null;
        }
        return null;
    }
    @Override
    public String toString() {
        return "Комната: " + getName() + ", количество персонажей: " + peoples.size();
    }
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

