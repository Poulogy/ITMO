package Server.commands;

import Common.system.*;
import Server.databases.DatabaseManager;
import Server.system.CollectionManager;
import Common.modules.Flat;

/**
 * Команда remove_by_id - удалить элемент из коллекции по его id
 *
 * @author Nazerke
 */
public class remove_by_id extends Command {
    CollectionManager collectionManager;

    /**
     * Конструктор класса remove_by_id
     *
     * @param collectionManagers менеджер коллекции
     */

    public remove_by_id(CollectionManager collectionManagers, DatabaseManager databaseManager) {
        super("/remove_by_id [id]", "удалить элемент из коллекции по его id", 1, collectionManagers, databaseManager);
        this.collectionManager = collectionManagers;
    }

    /**
     * Удалить элемент из коллекции по его id
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        try {
            if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                long id = request.getIDArgument();
                Flat flat = collectionManager.getFlatById(id);
                if (dbManager.removeById(request.getIDArgument(), request.getLogin())) {
                    if (flat == null) {
                        return new Response("There is no flat with such id");
                    } else {
                        collectionManager.remove(id);
                        return new Response("Flat was removed");
                    }
                }
            } else {
                return new Response("Login and password are incorrect");
            }
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        return null;
    }
}
