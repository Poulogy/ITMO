package Server.system;


import Common.system.Request;
import Server.commands.*;
import Server.databases.DataBaseConnector;
import Server.databases.DatabaseManager;


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
     * Менеджер коллекции
     */
    CollectionManager collectionManager = new CollectionManager();

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }


    public static DatabaseManager getDbManager() {
        return dbManager;
    }

    /**
     Объект класса DBManager для работы с базой данных.
     */
    public static DatabaseManager dbManager = new DatabaseManager(new DataBaseConnector());

    /**
     * Конструктор менеджера команд
     */
    public CommandManager(){
        commands.put("info", new info(collectionManager));
        commands.put("add", new add(collectionManager, dbManager));
        commands.put("show", new show(collectionManager, dbManager));
        commands.put("print_ascending", new print_ascending(collectionManager));
        commands.put("exit", new exit());
        commands.put("help", new help(commands));
        commands.put("execute_script", new execute_script());
        commands.put("clear", new clear(collectionManager, dbManager));
        commands.put("add_if_min", new add_if_min(collectionManager, dbManager));
        commands.put("head", new head(collectionManager, dbManager));
        commands.put("remove_first", new remove_first(collectionManager, dbManager));
        commands.put("print_unique_house", new print_unique_house(collectionManager));
        commands.put("remove_by_id", new remove_by_id(collectionManager, dbManager));
        commands.put("update_id", new update_id(collectionManager, dbManager));
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

