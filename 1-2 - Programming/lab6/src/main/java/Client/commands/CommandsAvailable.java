package Client.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

/**
 * Класс, хранящий доступные команды для диспетчера команд.
 */
public class CommandsAvailable {
    /**
     * Множество команд без аргументов.
     */
    public static final Set<String> commands_without_args = new HashSet<>();
    /**
     * Множество команд с аргументом ID.
     */
    public static final Set<String> commands_with_idArgs = new HashSet<>();
    /**
     * Множество команд с аргументом квартиры.
     */
    public static final Set<String> commands_with_flatArgs = new HashSet<>();
    /**
     * Множество команд с аргументами ID и квартиры.
     */
    public static final Set<String> commands_with_flatIdArgs = new HashSet<>();
    /**
     * Множество команд, принимающих аргументом скрипт.
     */
    public static final Set<String> script_command = new HashSet<>();

    static {
        Collections.addAll(commands_without_args, "info", "show", "print_ascending", "exit", "clear", "head", "remove_first", "print_unique_house", "help");
        Collections.addAll(commands_with_idArgs, "remove_by_id");
        Collections.addAll(commands_with_flatArgs, "add", "add_if_min");
        Collections.addAll(commands_with_flatIdArgs, "update_id");
        script_command.add("execute_script");
    }
}
