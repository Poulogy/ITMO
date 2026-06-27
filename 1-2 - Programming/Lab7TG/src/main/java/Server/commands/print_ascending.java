package Server.commands;

import Common.system.*;
import Server.databases.DatabaseManager;
import Server.system.CollectionManager;

import java.util.List;

/**
 * Команда print_ascending - вывести элементы коллекции в порядке возрастания
 *
 * @author Nazerke
 */

public class print_ascending extends Command {
    CollectionManager collectionManager;
    /**
     * Конструктор класса print_ascending
     *
     * @param collectionManager менеджер коллекции
     */
    public print_ascending(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("/print_ascending","вывести элементы коллекции в порядке возрастания площади", 0, collectionManager, databaseManager);
        this.collectionManager = collectionManager;
    }
    /**
     * Вывести элементы коллекции в порядке возрастания
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        try{
            if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                if (collectionManager.getCollection().isEmpty()) {
                    return new Response("Collection is empty");
                } else {
                    List<Long> ids = dbManager.getIdsOfUsersElements(request.getLogin());
                    return new Response("Collection elements: \n" + collectionManager.printAscending(ids));
                }
            } else {
                return new Response("Login or password is incorrect");
            }
        }catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}
