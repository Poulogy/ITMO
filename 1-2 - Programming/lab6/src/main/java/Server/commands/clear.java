package Server.commands;

import Common.system.*;
import Server.managers.CollectionManager;

/**
 * Команда clear - очищает коллекцию
 *
 * @author Nazerke
 */

public class clear extends Command{

    /**
     * Конструктор класса Clear
     *
     * @param collectionManager менеджер коллекции
     */
    public clear(CollectionManager collectionManager) {
        super("clear","очистить коллекцию", collectionManager);
    }
    /**
     * Очищает коллекцию
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        getCollectionManager().getCollection().clear();
        return new Response("Collection cleared");
        }
}
