package Server.commands;

import Common.modules.Flat;
import Common.system.*;
import Server.databases.DatabaseManager;
import Server.system.CollectionManager;

/**
 * Команда add - добавить новый элемент в коллекцию
 *
 * @author Nazerke
 */
public class add extends Command {
    /**
     * Менеджер коллекции
     */
    CollectionManager collectionManager;
    DatabaseManager dbManager;

    /**
     * Конструктор класса Add
     *
     * @param collectionManagers менеджер коллекции
     */
    public add(CollectionManager collectionManagers, DatabaseManager dbManager) {
        super("add", "добавить новый элемент в коллекцию", 0, collectionManagers, dbManager);
        this.collectionManager = collectionManagers;
        this.dbManager = dbManager;
    }

    /**
     * Добавить новый элемент в коллекцию
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        try {
            if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                Flat flat = request.getFlatArgument();
                Long id = dbManager.addElement(flat, request.getLogin());
                System.out.println(id);
                flat.setId(id);
                collectionManager.getCollection().add(flat);
                return new Response("Flat was added to collection");
            } else {
                return new Response("Login and password are incorrect");
            }
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}
