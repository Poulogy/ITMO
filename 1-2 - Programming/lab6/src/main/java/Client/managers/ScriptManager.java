package Client.managers;

import Client.commands.CommandsToReceive;

import java.io.*;
import java.util.*;

/**
 * Класс, который управляет скриптом
 * @author Nazerke
 */
public class ScriptManager {
    /**
     * Имя скрипта.
     */
    private final String filename;
    /**
     * Путь до скрипта скрипта.
     */

    private final File path;
    /**
     * Стек для отслеживания рекурсии в скриптах.
     */
    public static Stack<String> callStack = new Stack<>();

    /**
     * Конструктор для создания объекта ScriptReader.
     * @param commandsToReceive объект типа CommandToSend, содержащий имя скрипта.
     * @throws Exception исключение, которое выбрасывается, если размер скрипта превышает 10Мб.
     * @throws IllegalArgumentException исключение, которое выбрасывается, если файл скрипта не найден или не может быть прочитан.
     */
    public ScriptManager(CommandsToReceive commandsToReceive) throws Exception, IllegalArgumentException {
        this.filename = commandsToReceive.getCommandArgs()[0];
        path = new File(new File(System.getProperty("user.dir")), filename);
        if (!path.exists() || !path.canRead())
            throw new IllegalArgumentException("The problem with script. Check the file.");
        if (callStack.contains(path.getAbsolutePath())) {
            callStack.clear();
            System.out.println("Scripts refer to each other recursively.");
            System.exit(1);
        }
        callStack.push(path.getAbsolutePath());
        if (path.equals(new File(System.getProperty("user.dir"), "/dev/random")) || path.length() / (1024 * 1024) > 10) {
            throw new Exception("The size of the scripts is more than 10MB");
        }
    }
    /**
     * Метод для остановки чтения скрипта.
     */
    public void stopScriptReading() {
        callStack.clear();
    }

    /**
     * Метод для получения пути к скрипту.
     * @return объект типа File, представляющий путь к скрипту.
     */
    public File getPath() {
        return path;
    }
}