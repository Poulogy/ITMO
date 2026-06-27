package exeptions;
import classes.people.Character;


public class InvalidNotWhistlerInHospital extends Exception{
    public InvalidNotWhistlerInHospital() {
    }
    @Override
    public String getMessage() {
        return String.format(" нет в больнице, но ");
    }
}

