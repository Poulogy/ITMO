package Server.managers;
import Common.modules.Comparators.FlatComparatorByNOBR;
import Common.modules.Flat;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
     * Конструктор менеджера коллекции
     * @param dumpManager менеджер ввода-вывода
     */
    public CollectionManager(DumpManager dumpManager) {
        this.dumpManager = dumpManager;
    }

    /**
     * Получение списка элементов коллекции
     * @return Список элементов коллекции
     */

    public PriorityQueue<Flat> getCollection() {
        return collection;
    }

    /**
     * Менеджер ввода-вывода
     */
    private final DumpManager dumpManager;

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
     * Генерация ид
     * @return ид
     */
    public Long generateNewId() {
        long id = collection.stream()
                .mapToLong(Flat::getId)
                .max().orElse(0L);
        return id + 1;
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
     * Метод возвращает информацию о коллекции организаций.
     * @return строка с типом коллекции, датой инициализации и количеством элементов
     */
    public String getInfo() {
        return "Тип коллекции: " + collection.getClass()
                + "\nКоличество элементов: " + collection.size();
    }

    /**
     * Сохранение коллекции в файл
     * @param FileName имя файла
     */

    public void saveCollection(String FileName) {
        dumpManager.setFileName(FileName);
        dumpManager.writeCollection(collection);
        ZonedDateTime lastSaveTime = ZonedDateTime.now();
    }
    public void loadCollection(String FileName) {
        dumpManager.setFileName(FileName);
        this.collection = (PriorityQueue<Flat>) dumpManager.readCollection();
        ZonedDateTime lastSaveTime = ZonedDateTime.now();
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
    public String printAscending() {
        if (collection.isEmpty()) return "Collection is Empty";
        return collection.stream().sorted()
                .map(organization -> organization.toString() + "\n")
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
                .map(organization -> organization.toString() + "\n")
                .collect(Collectors.joining());
    }

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
