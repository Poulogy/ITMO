package Server.commands;

import Common.system.*;
import Server.system.CollectionManager;

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
    public print_ascending(CollectionManager collectionManager) {
        super("print_ascending","вывести элементы коллекции в порядке возрастания", 0);
        this.collectionManager = collectionManager;
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
            return new Response(collectionManager.printAscending());
        }
    }
}
