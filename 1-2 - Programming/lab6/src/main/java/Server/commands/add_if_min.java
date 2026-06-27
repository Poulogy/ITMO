package Server.commands;

import Common.system.*;
import Server.managers.CollectionManager;
import Common.modules.Flat;

/**
 * Команда add_if_min - добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
 *
 * @author Nazerke
 *
 */
public class add_if_min extends Command{
    /**
     * Менеджер коллекции
     */
    CollectionManager collectionManagers;

    /**
     * Конструктор класса Add_if_min
     *
     * @param collectionManager менеджер коллекции
     */
    public add_if_min(CollectionManager collectionManager) {
        super("add_if_min", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции", collectionManager);
        this.collectionManagers = collectionManager;
    }
    /**
     * Добавить новый элемент в коллекцию
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        Flat minFlat = new Flat();
        if (getCollectionManager().getCollection().size() == 1) {
            minFlat = getCollectionManager().getCollection().element();
        } else {
            for (Flat flat: getCollectionManager().getCollection()){
                for (Flat flat1: getCollectionManager().getCollection()){
                    if (flat.getArea() > flat1.getArea()){
                        minFlat = flat1;
                    }}
            }
        }
        Flat Flat = request.getFlatArgument();
        if (minFlat.getArea() > Flat.getArea()){
           getCollectionManager().getCollection().add(Flat);
            return new Response("Flat was added to collection");
        } else {
            return new Response("Flat was not added to collection");
        }
    }
}
