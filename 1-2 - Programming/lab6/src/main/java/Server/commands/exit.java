package Server.commands;

import Common.system.*;
import Server.managers.CollectionManager;

/**
 * Команда exit - завершить программу
 *
 * @author Nazerke
 */

public class exit extends Command{
    /**
     * Конструктор класса exit
     *
     * @param collectionManager менеджер коллекции
     */
    public exit(CollectionManager collectionManager) {
        super("exit","завершить программу", collectionManager);
    }
    /**
     * Завершить программу
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        return new Response("Completion of the client program");
    }
}
