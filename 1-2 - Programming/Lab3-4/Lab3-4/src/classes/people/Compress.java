package classes.people;
import classes.places.Place;
import enums.Profesion;
import exeptions.InvalidNotWhistlerInHospital;
import interfaces.JobGetable;
import interfaces.Reportable;
import interfaces.Thinkable;

public class Compress extends Character implements Reportable, Thinkable {
    private Place job;

    public Compress(String name, Place place) {
        super(name, place);
        job = getpl();
    }


    public void call(Place a, Character character) {
            try {
                if (job.getPeoples().contains(character)){}
                else
                    throw new InvalidNotWhistlerInHospital() {
                    };
            }
            catch (InvalidNotWhistlerInHospital e){
                System.out.println(character.getName() + e.getMessage());
            }
            finally {
                System.out.println(getName() + " позвонил в " + a.getName());
            }
        }

    @Override
    public void think() {
        System.out.println(getName() + " задумался");
    }
    @Override
    public String toString() {
        return getName();
    }
}

