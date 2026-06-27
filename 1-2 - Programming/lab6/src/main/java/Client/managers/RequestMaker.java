package Client.managers;

import Client.commands.CommandsAvailable;
import Client.commands.CommandsToReceive;
import Client.managers.Generators.FlatGenerator;
import Common.system.Request;

import java.util.Scanner;

public class RequestMaker {

    /**
     * Создает запрос по переданной команде.
     * @param command команда
     * @param scanner сканер
     * @param scriptMode режим скрипта
     * @return запрос
     * @throws NullPointerException если команда не найдена
     */
    public Request createRequestOfCommand(CommandsToReceive command, Scanner scanner, boolean scriptMode) throws NullPointerException {
        String name = command.getCommandName();
        Request request;
        if (CommandsAvailable.commands_without_args.contains(name)) {
            request = createRequestWithoutArgs(command);
        } else if (CommandsAvailable.commands_with_idArgs.contains(name)) {
            request = createRequestWithID(command);
        } else if (CommandsAvailable.commands_with_flatArgs.contains(name)) {
            request = createRequestWithFlat(command);
        } else if (CommandsAvailable.commands_with_flatIdArgs.contains(name)) {
            request = createRequestWithFlatId(command);
        } else if (CommandsAvailable.script_command.contains(name)) {
            request = createRequestWithFlatId(command);
        } else {
            throw new NullPointerException("Команда не найдена. Напишите 'help' для просмотра всех доступных команд.");
        }
        return request;
    }

    /**
     * Создает запрос без аргументов.
     * @param command команда
     * @return запрос
     */
    private Request createRequestWithoutArgs(CommandsToReceive command) {
        try {
            return new Request(command.getCommandName());
        } catch (Exception e) {
            System.out.println("Arguments exception");
            return null;
        }
    }

    /**
     * Создает запрос с аргументом ID.
     * @param command команда
     * @return запрос
     */
    private Request createRequestWithID(CommandsToReceive command) {
        try {
            if (command.getCommandArgs().length != 1) throw new IllegalArgumentException("Arguments exception");
            long id = Long.parseLong(command.getCommandArgs()[0]);
            if (id <= 0) throw new IllegalArgumentException("Arguments exception");
            return new Request(command.getCommandName(), id);
        } catch (Exception e) {
            System.out.println("Arguments exception");
            return null;
        }
    }

    /**
     * Класс для создания запроса с аргументом типа Flat.
     * @param command команда
     * @return запрос
     */
    private Request createRequestWithFlat(CommandsToReceive command) {
        try {
            if (command.getCommandArgs().length != 0) throw new IllegalArgumentException("Arguments exception");
            return new Request(command.getCommandName(), FlatGenerator.generateFlat());
        } catch (Exception e) {
            System.out.println("Check data accuracy. Exiting.");
            System.exit(1);
            return null;
        }
    }

    /**
     * Класс для создания запроса с аргументом типа Organization и ID.
     * @param command команда
     * @return запрос
     */
    private Request createRequestWithFlatId(CommandsToReceive command) {
        try {
            if (command.getCommandArgs().length != 1) throw new IllegalArgumentException("Arguments exception");
            long id = Long.parseLong(command.getCommandArgs()[0]);
            return new Request(command.getCommandName(), id, FlatGenerator.generateFlat());
        } catch (Exception e) {
            System.out.println("Check data accuracy. Exiting.");
            System.exit(1);
            return null;
        }
    }
}