package classes.people;
import classes.places.Place;
import enums.Profesion;
import records.WhistlerMissing;
import java.util.Objects;

public abstract class Character {
        protected String name;
        protected Place whereIsPerson;

        public Character(String name, Place whereIsPerson) {

            this.name = name;
            this.whereIsPerson = whereIsPerson;
        }
        public Character(String name, Place whereIsPerson, int missing) {
            this.name = name;
            this.whereIsPerson = whereIsPerson;
        }
        public void getJob(Profesion profesion) {
            switch (profesion){
                case DOCTOR:
                    System.out.println(getName() + " является доктором");
                    break;
                case POLICE:
                    System.out.println(getName() + " явлется полицейским");
                    break;
                case REPORTER:
                    System.out.println(getName() + " является репортёром");
                    break;
                default:
                    System.out.println(getName() +"");
        }

    }

        public void moveToRoom(Place place) {
            if (this.whereIsPerson != null) {
                this.whereIsPerson.exit(this);
            }
            this.whereIsPerson = place;
            place.enter(this);
        }
        public String getName() {
            return name;
        }
        public String getLoc() {
            return String.valueOf(whereIsPerson);
        }
        public Place getpl() {
            return whereIsPerson;
        }
    public int know(String name, Character character, int missing) {
        WhistlerMissing Wm = new WhistlerMissing(name, character);
        if (Math.random() <= 0.4 & missing == 1) {
            System.out.println(character.toString() + " знает где находится " + name);
            missing = 0;
            return missing;
        } else if (missing == 1) {
            System.out.println(Wm.toString());

        }
        return missing;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Character character = (Character) obj;
        return Objects.equals(name, character.name) && Objects.equals(whereIsPerson, character.whereIsPerson);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, whereIsPerson);
    }

}
