package Server.managers;


import Common.system.Request;
import Server.commands.*;
import Server.system.Server;


import java.util.HashMap;

/**
 * Менеджер команд
 *
 * @author Nazerke
 */

public class CommandManager {
    /**
     * Список команд
     */
    private final HashMap<String, Command> commands = new HashMap<>();

    /**
     * Получения списка команд
     * @return список команды
     */

    public HashMap<String, Command> getCommands() {
        return commands;
    }
    /**
     * Имя файла
     */
    String Filename = "1.txt";
    /**
     * Мусорный менеджер
     */
    DumpManager dumpManager = new DumpManager(Filename);


    public CollectionManager getCollectionMan() {
        return collectionManager;
    }

    /**
     * Менеджер коллекции
     */
    CollectionManager collectionManager = new CollectionManager(dumpManager);

    /**
     * Конструктор менеджера команд
     */
    public CommandManager(){
        commands.put("info", new info(collectionManager));
        commands.put("show", new show(collectionManager));
        commands.put("add", new add(collectionManager));
        commands.put("print_ascending", new print_ascending(collectionManager));
        commands.put("exit", new exit(collectionManager));
        commands.put("help", new help(collectionManager, commands));
        commands.put("execute_script", new execute_script(collectionManager));
        commands.put("clear", new clear(collectionManager));
        commands.put("add_if_min", new add_if_min(collectionManager));
        commands.put("head", new head(collectionManager));
        commands.put("remove_first", new remove_first(collectionManager));
        commands.put("print_unique_house", new print_unique_house(collectionManager));
        commands.put("remove_by_id", new remove_by_id(collectionManager));
        commands.put("update_id", new update_id(collectionManager));
    }


    /**
     * Метод для получения объекта команды по имени команды из запроса.
     * @param request запрос от клиента
     * @return объект команды, соответствующий имени команды из запроса
     */
    public Command initCommand(Request request) {
        String commandName = request.getCommandName();
        return commands.get(commandName);
    }
}

