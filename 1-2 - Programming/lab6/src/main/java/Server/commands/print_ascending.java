package Server.commands;

import Common.system.Request;
import Common.system.Response;
import Server.managers.CollectionManager;

/**
 * Команда print_ascending - вывести элементы коллекции в порядке возрастания
 *
 * @author Nazerke
 */

public class print_ascending extends Command {
    /**
     * Конструктор класса print_ascending
     *
     * @param collectionManager менеджер коллекции
     */
    public print_ascending(CollectionManager collectionManager) {
        super("print_ascending","вывести элементы коллекции в порядке возрастания", collectionManager);
    }
    /**
     * Вывести элементы коллекции в порядке возрастания
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        if (getCollectionManager().getCollection().isEmpty()) {
            return new Response("Collection is empty");
        } else {
            return new Response(getCollectionManager().printAscending());
        }
    }
}
