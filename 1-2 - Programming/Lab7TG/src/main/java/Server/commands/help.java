package Server.commands;

import Common.system.*;

import java.util.HashMap;
/**
 * Команда help - вывести справку по доступным командам
 *
 * @author Nazerke
 */
public class help extends Command {
    /**
     * Словарь команд
     */
    private final HashMap<String, Command> commands;
    /**
     * Конструктор класса help
     *
     * @param commands           словарь команд
     */
    public help(HashMap<String, Command> commands) {
        super("/help","вывести справку по доступным командам", 0);
        this.commands = commands;
    }
    /**
     * Вывести справку по доступным командам
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request){
        StringBuilder sb = new StringBuilder();
        for (Command command : commands.values()) {
            sb.append(command.getName()).append(" - ").append(command.getDescription()).append("\n\n");
        }
        sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        return new Response("Available commands:\n" + sb);

    }
}
