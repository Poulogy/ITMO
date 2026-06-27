package Common.modules.Comparators;

import Common.modules.Flat;

import java.util.Comparator;

/**
 * Компаратор по количеству ванн
 *
 * @author Nazerke
 */
public class FlatComparatorByNOBR implements Comparator<Flat> {
    @Override
    public int compare(Flat o1, Flat o2){
        return (int) (o2.getNumberOfBathrooms() - o1.getNumberOfBathrooms());
    }
}
