package Client.manager;

import Client.Bot;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.*;

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
     * Путь до скрипта.
     */

    private final File path;
    /**
     * Стек для отслеживания рекурсии в скриптах.
     */
    public static Stack<String> callStack = new Stack<>();

    Bot bot = Client.Client.bot;

    /**
     * Конструктор для создания объекта ScriptReader.
     * @throws Exception исключение, которое выбрасывается, если размер скрипта превышает 10Мб.
     * @throws IllegalArgumentException исключение, которое выбрасывается, если файл скрипта не найден или не может быть прочитан.
     */
    public ScriptManager(String File, String ChI) throws Exception, IllegalArgumentException {
        this.filename = File;
        path = new File(new File(System.getProperty("user.dir")), filename);

        if (!path.exists() || !path.canRead()){
            bot.sendMess("The problem with script or script does not exist. Check the file.", ChI);
        }else if (callStack.contains(path.getAbsolutePath())) {
            callStack.clear();
            bot.sendMess("Scripts refer to each other recursively.", ChI);
        }else if (path.equals(new File(System.getProperty("user.dir"), "/dev/random")) || path.length() / (1024 * 1024) > 10) {
            bot.sendMess("The size of the scripts is more than 10MB", ChI);
        }
        else {
            List<String> commands;
            commands = Files.readAllLines(path.toPath());
            bot.sendMess("Executing script.", ChI);
            Pattern pS = Pattern.compile("/execute_script \\S+");

            for (String command : commands) {
                Matcher mS = pS.matcher(command);
                if (mS.find()) {
                    String[] splitInput = command.trim().split(" ");
                    String File2 = splitInput[1];
                    boolean recursion = checkRecursion(File2, File);
                    if (recursion) {
                        bot.sendMess("Scripts refer to each other recursively.", ChI);
                        break;
                    } else {
                        bot.sendMess(command, ChI);
                        String Response = bot.parseMessage(command, ChI);
                        bot.sendMess(Response, ChI);
                    }
                } else {
                    bot.sendMess(command, ChI);
                    String Response = bot.parseMessage(command, ChI);
                    bot.sendMess(Response, ChI);
                }
            }

        }

    }
    /**
     * Метод для остановки чтения скрипта.
     */
    public boolean checkRecursion(String fileName2, String filename) throws IOException {
        boolean result = false;
        File path2 = new File(new File(System.getProperty("user.dir")), fileName2);
        List<String> commands2 = Files.readAllLines(path2.toPath());
        Pattern pS2 = Pattern.compile("/execute_script " + filename);
        for (String command : commands2) {
            Matcher mS = pS2.matcher(command);
            if (mS.find()) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Метод для получения пути к скрипту.
     * @return объект типа File, представляющий путь к скрипту.
     */
    public File getPath() {
        return path;
    }
}