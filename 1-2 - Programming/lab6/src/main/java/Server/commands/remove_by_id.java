package Server.commands;

import Common.system.Request;
import Common.system.Response;
import Server.managers.CollectionManager;
import Common.modules.Flat;

/**
 * Команда remove_by_id - удалить элемент из коллекции по его id
 *
 * @author Nazerke
 */
public class remove_by_id extends Command {
    /**
     * Конструктор класса remove_by_id
     *
     * @param collectionManagers менеджер коллекции
     */
    public remove_by_id(CollectionManager collectionManagers) {
        super("remove_by_id","удалить элемент из коллекции по его id", collectionManagers);
    }
    /**
     * Удалить элемент из коллекции по его id
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        try {
            long id = request.getIDArgument();
            Flat flat = getCollectionManager().getFlatById(id);
            if (flat == null) {
                return new Response("There is no flat with such id");
            } else {
                getCollectionManager().remove(id);
                return new Response("Flat was removed");
            }
        } catch (Exception e) {
            return new Response("Wrong Id");
        }
    }
}
