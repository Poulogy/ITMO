package Server.commands;

import Common.system.*;
import Server.databases.DatabaseManager;
import Server.system.CollectionManager;
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
    public add_if_min(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("add_if_min", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции", 0, collectionManager, databaseManager);
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
        if (collectionManagers.getCollection().size() == 1) {
            minFlat = collectionManagers.getCollection().element();
        } else {
            for (Flat flat: collectionManagers.getCollection()){
                for (Flat flat1: collectionManagers.getCollection()){
                    if (flat.getArea() > flat1.getArea()){
                        minFlat = flat1;
                    }}
            }
        }
        try {
            if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                Flat flat = request.getFlatArgument();
                Long id = dbManager.addElement(flat, request.getLogin());
                flat.setId(id);
                if (minFlat.getArea() > flat.getArea()) {
                    collectionManagers.getCollection().add(flat);
                    return new Response("Flat was added to collection");
                } else {
                    return new Response("Flat was not added to collection");
                }
            } else{
                return new Response("Login and password are incorrect");
            }
        }catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}
