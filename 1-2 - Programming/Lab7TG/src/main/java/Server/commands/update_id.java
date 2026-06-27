package Server.commands;

import Common.system.*;
import Server.databases.DatabaseManager;
import Server.system.CollectionManager;
import Common.modules.Flat;

/**
 * Команда update_id - обновить значение элемента коллекции, id которого равен заданному
 *
 * @author Nazerke
 */

public class update_id extends Command{
    /**
     * Менеджер коллекции
     */
    CollectionManager collectionManager;
    /**
     * Конструктор класса update_id
     *
     * @param collectionManagers менеджер коллекции
     */
    public update_id(CollectionManager collectionManagers, DatabaseManager databaseManager) {
        super("/update_id [id]","обновить значение элемента коллекции, id которого равен заданному", 1, collectionManagers, databaseManager);
        this.collectionManager = collectionManagers;
    }
    /**
     * Обновить значение элемента коллекции, id которого равен заданному
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request){
        try {
            if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                Long id = request.getIDArgument();
                if (dbManager.checkFlatExistence(id)) {
                    if (dbManager.updateById(request.getFlatArgument(), id, request.getLogin())) {
                        Flat updatedFlat = request.getFlatArgument();
                        updatedFlat.setId(id);
                        collectionManager.remove(id);
                        collectionManager.getCollection().add(request.getFlatArgument());
                        return new Response("Element with id " + id + " was updated");
                    } else {
                        return new Response("Element was created by another user. You don't have any access to this element");
                    }
                } else {
                    return new Response("There is no element with id " + id);
                }
            } else {
                return new Response("Login and password are incorrect");
            }
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}
