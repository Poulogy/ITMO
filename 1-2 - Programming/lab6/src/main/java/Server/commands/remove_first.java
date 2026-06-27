package Server.commands;

import Common.system.Request;
import Common.system.Response;
import Server.managers.CollectionManager;
import Common.modules.Flat;

/**
 * Команда remove_first - удалить первый элемент из коллекции
 *
 * @author Nazerke
 */
public class remove_first extends Command{
    /**
     * Конструктор класса remove_first
     *
     * @param collectionManager менеджер коллекции
     */

    public remove_first(CollectionManager collectionManager) {
        super("remove_first","удалить первый элемент из коллекции", collectionManager);
    }
    /**
     * Удалить первый элемент из коллекции
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        if (getCollectionManager().getCollection().isEmpty()) {
            return new Response("Collection is empty");
        } else {
            Flat flat = getCollectionManager().getFirst();
            getCollectionManager().getCollection().remove(flat);
            return new Response("Element deleted");
        }
    }

}
