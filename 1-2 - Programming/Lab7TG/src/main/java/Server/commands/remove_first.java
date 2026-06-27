package Server.commands;

import Common.system.*;
import Server.databases.DatabaseManager;
import Server.system.CollectionManager;
import Common.modules.Flat;

/**
 * Команда remove_first - удалить первый элемент из коллекции
 *
 * @author Nazerke
 */
public class remove_first extends Command {
    CollectionManager collectionManager;
    /**
     * Конструктор класса remove_first
     *
     * @param collectionManager менеджер коллекции
     */
    public remove_first(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("/remove_first", "удалить первый элемент из коллекции", 0, collectionManager, databaseManager);
        this.collectionManager = collectionManager;
    }

    /**
     * Удалить первый элемент из коллекции
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        try {
            if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                if (dbManager.removeById(collectionManager.getFirst().getId(), request.getLogin())) {
                    if (collectionManager.getCollection().isEmpty()) {
                        return new Response("Collection is empty");
                    } else {
                        Flat flat = getCollectionManager().getFirst();
                        collectionManager.getCollection().remove(flat);
                        return new Response("Element deleted");
                    }
                } else {
                    return new Response("Login and password do not match");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
