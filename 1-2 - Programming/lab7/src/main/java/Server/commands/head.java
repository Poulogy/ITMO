package Server.commands;

import Common.system.*;
import Server.databases.DatabaseManager;
import Server.system.CollectionManager;

/**
 * Команда head - вывести первый элемент коллекции
 *
 * @author Nazerke
 */

public class head extends Command{
    CollectionManager collectionManager;
    /**
     * Конструктор класса Head
     *
     * @param collectionManager менеджер коллекции
     */
    public head(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("head","вывести первый элемент коллекции", 0, collectionManager, databaseManager);
        this.collectionManager = collectionManager;
    }
    /**
     * Вывести первый элемент коллекции
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request){
        try{
            if (collectionManager.getCollection().isEmpty()) {
                return new Response("Collection is empty");
            } else {
                return new Response("First element in collection: ", getCollectionManager().getFirst());
            }
        } catch(Exception e){
            return new Response("Head command failed");
        }
    }
}

