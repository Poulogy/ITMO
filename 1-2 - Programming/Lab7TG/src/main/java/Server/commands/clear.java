package Server.commands;

import Common.system.*;
import Server.databases.DatabaseManager;
import Server.system.CollectionManager;

/**
 * Команда clear - очищает коллекцию
 *
 * @author Nazerke
 */

public class clear extends Command{
    CollectionManager collectionManager;

    /**
     * Конструктор класса Clear
     *
     * @param collectionManager менеджер коллекции
     */
    public clear(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("/clear","очистить коллекцию", 0, collectionManager, databaseManager);
        this.collectionManager = collectionManager;
    }
    /**
     * Очищает коллекцию
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        try {
            getDbManager().clear(request.getLogin());
            if (collectionManager.getCollection().isEmpty())
                return new Response("Collection is empty");
            else {
                getCollectionManager().getCollection().clear();
                collectionManager.setCollection(dbManager.loadCollection());
                return new Response("Collection has been cleared");
            }
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}
