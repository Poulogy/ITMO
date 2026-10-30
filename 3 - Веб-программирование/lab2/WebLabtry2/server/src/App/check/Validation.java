package App.check;

public class Validation {
    public boolean validate(float x, float y, float r) {
        return !(x < -3) && !(x > 5) && !(y < -5) && !(y > 5) && !(r < 1) && !(r > 3);
    }
}
