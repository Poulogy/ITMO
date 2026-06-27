package Server.system;
import Common.modules.Flat;


import java.util.*;
import java.util.stream.Collectors;

/**
 * Менеджер коллекции
 *
 * @author Nazerke
 */
public class CollectionManager {

    /**
     * Список элементов коллекции
     */
    public PriorityQueue<Flat> collection = new PriorityQueue<>();

    /**
     * Получение списка элементов коллекции
     * @return Список элементов коллекции
     */

    public PriorityQueue<Flat> getCollection() {
        return collection;
    }

    /**
     * Установить коллекцию
     * @param collection Коллекция
     */

    public void setCollection(PriorityQueue<Flat> collection) {
        this.collection = collection;
    }

    /**
     * Получение первого элемента
     * @return Первый элемент коллекции
     */
    public Flat getFirst() {
        if (collection.isEmpty()) return null;
        return collection.peek();
    }

    /**
     * Получение Квартиры по ид
     * @param id ид
     * @return элемент Квартира
     */
    public Flat getFlatById(Long id){
        for (Flat flat : collection){
            if (flat.getId().equals(id)){
                return flat;
            }
        }
        return null;
    }
    /**
     * Метод возвращает информацию о коллекции квартир.
     * @return строка с типом коллекции, датой инициализации и количеством элементов
     */
    public String getInfo() {
        return "Collection type: " + collection.getClass()
                + "\nElements quantity: " + collection.size();
    }

    /**
     * Удалить элемента по ид
     * @param id ид
     */

    public void remove(Long id){
        Flat remFlat = getFlatById(id);
        collection.remove(remFlat);
    }

    /**
     * Возвращает все объекты коллекции по возрастанию.
     * Если коллекция пуста, возвращается строка "Коллекция пуста".
     * @return все объекты коллекции по возрастанию.
     */
    /*public String printAscending() {
        if (collection.isEmpty()) return "Collection is Empty";
        return collection.stream().sorted()
                .map(flat -> flat.toString() + "\n")
                .collect(Collectors.joining());
    }*/
    public String printAscending(List<Long> ids) {
        PriorityQueue<Flat> coll = collection.stream().filter(p -> ids.contains(p.getId())).collect(Collectors.toCollection(PriorityQueue::new));
        return coll.stream().sorted()
                .map(flat -> flat.toString() + "\n")
                .collect(Collectors.joining());
    }
    /**
     * Возвращает все объекты коллекции.
     * Если коллекция пуста, возвращается строка "Коллекция пуста".
     * @return все объекты коллекции.
     */
    public String showCollection() {
        if (collection.isEmpty()) return "Collection is Empty";
        return collection.stream()
                .map(flat -> flat.toString() + "\n")
                .collect(Collectors.joining());
    }
    public String  getUsersElements(List<Long> ids) {
        PriorityQueue<Flat> coll =  collection.stream().filter(flat -> ids.contains(flat.getId())).collect(Collectors.toCollection(PriorityQueue::new));
        return coll.stream()
                .map(flat -> flat.toString() + "\n\n")
                .collect(Collectors.joining());

    }
    public String  getAlienElements(List<Long> ids) {
        PriorityQueue<Flat> colle =  collection.stream().filter(flat -> !ids.contains(flat.getId())).collect(Collectors.toCollection(PriorityQueue::new));
        return colle.stream()
                .map(flat -> flat.toString() + "\n\n")
                .collect(Collectors.joining());

    }
    /*public PriorityQueue<Flat> getAlienElements(List<Long> ids) {
        return collection.stream().filter(flat -> !ids.contains(flat.getId())).collect(Collectors.toCollection(PriorityQueue::new));
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionManager that)) return false;
        return Objects.equals(collection, that.collection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection);
    }
    @Override
    public String toString() {
        return "CollectionManager{" +
                "collection=" + collection +
                '}';
    }

}
