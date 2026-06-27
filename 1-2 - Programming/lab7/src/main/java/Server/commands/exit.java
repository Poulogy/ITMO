package Server.commands;

import Common.system.*;

/**
 * Команда exit - завершить программу
 *
 * @author Nazerke
 */

public class exit extends Command{
    /**
     * Конструктор класса exit
     */
    public exit() {
        super("exit","завершить программу", 0);
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
