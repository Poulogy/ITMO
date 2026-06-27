package Server.commands;

import Common.system.*;
import Server.databases.DatabaseManager;
import Server.system.CollectionManager;

import java.util.List;

/**
 * Команда show - вывести в стандартный поток вывода все элементы коллекции в строковом представлении
 *
 * @author Nazerke
 */
public class show extends Command{
    CollectionManager collectionManager;
    /**
     * Конструктор класса show
     *
     * @param collectionManager менеджер коллекции
     */
    public show(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("/show","вывести в стандартный поток вывода все элементы коллекции в строковом представлении", 0, collectionManager, databaseManager);
        this.collectionManager = collectionManager;
    }
    /**
     * Вывести в стандартный поток вывода все элементы коллекции в строковом представлении
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request){
        try {
            if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                if (collectionManager.getCollection().isEmpty()) {
                    return new Response("Collection is empty");
                } else {
                    List<Long> ids = dbManager.getIdsOfUsersElements(request.getLogin());
                    return new Response("Collection elements: \n" + collectionManager.getUsersElements(ids));
                }
            } else {
                return new Response("Login or password is incorrect");
            }

        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}
