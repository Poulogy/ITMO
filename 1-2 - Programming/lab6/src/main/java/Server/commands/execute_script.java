package Server.commands;

import Common.system.Request;
import Common.system.Response;
import Server.managers.CollectionManager;

/**
 * Команда execute_script - считать и исполнить скрипт из указанного файла.
 *
 * @author Nazerke
 */

public class execute_script extends Command {
    /**
     * Словарь команд
     */
    /**
     * Конструктор класса execute_script
     *
     * @param collectionManagers менеджер коллекции
     */
    public execute_script(CollectionManager collectionManagers) {
        super("execute_script","считать и исполнить скрипт из указанного файла.", collectionManagers);
    }
    /**
     * Исполнить скрипт из указанного файла
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        return new Response("Исполнение скрипта.");
    }
}
