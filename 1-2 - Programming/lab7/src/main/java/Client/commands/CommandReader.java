package Client.commands;

import java.util.*;

/**
 * Класс для чтения команд из консоли или файла скрипта и создания объекта CommandToSend с информацией о команде.
 */
public class CommandReader {
    /**
     * Считывает команду из консоли или файла скрипта, создает объект CommandToSend с информацией о команде.
     *
     * @param scanner             сканер для чтения из консоли или файла скрипта
     * @param scriptMode     режим считывания команд, в котором находится приложение
     * @return объект CommandToSend, содержащий имя команды и ее аргументы
     */
    public static CommandsToReceive readCommand(Scanner scanner, boolean scriptMode) {
        try {
            if (!scriptMode) System.out.print("$ ");
            String[] splitInput = scanner.nextLine().trim().split("\\s+");
            if (splitInput.length == 0 || splitInput[0].isEmpty()) {
                System.out.println("Enter command");
                return readCommand(scanner, scriptMode);
            }
            String commandName = splitInput[0].toLowerCase(Locale.ROOT);
            String[] commandsArgs = Arrays.copyOfRange(splitInput, 1, splitInput.length);
            if (scriptMode) {
                String arguments = String.join(" ", commandsArgs);
                System.out.println("$ " + commandName + " " + arguments);
            }
            return new CommandsToReceive(commandName, commandsArgs);

        } catch (NoSuchElementException e) {
            if (!scriptMode) {
                System.out.println("Console exiting.");
                System.exit(1);
            }
            return null;
        }
    }
}