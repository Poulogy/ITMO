package Server.commands;

import Common.system.*;

/**
 * Команда execute_script - считать и исполнить скрипт из указанного файла.
 *
 * @author Nazerke
 */

public class execute_script extends Command {
    /**
     * Конструктор класса execute_script
     *
     */
    public execute_script() {
        super("/execute_script [FileName]","считать и исполнить скрипт из указанного файла. [в наличии 3 скрипта: 1, 2, 3]", 0);
    }
    /**
     * Исполнить скрипт из указанного файла
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request) {
        return new Response("Script execution");
    }
}
