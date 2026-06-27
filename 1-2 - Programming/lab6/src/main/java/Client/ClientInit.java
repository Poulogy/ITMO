package Client;

import Client.commands.*;
import Client.managers.*;
import Client.system.*;
import Common.system.*;

import java.io.*;
import java.nio.channels.*;
import java.util.*;

/**
 * Класс для установки соединения с сервером.
 */
public class ClientInit {

    /**
     * Сканнер для чтения команд из скрипта.
     */
    private static Scanner scriptScanner;

    /**
     * Создатель запросов для отправки на сервер.
     */
    private static final RequestMaker requestMaker = new RequestMaker();

    /**
     * Запускает бесконечный цикл селектора.
     * @param selector       объект типа Selector для мониторинга состояний канала
     * @param channel        объект типа SocketChannel - канал для передачи данных по сети
     * @param scanner             объект типа Scanner для чтения ввода пользователя с консоли
     * @param scriptMode     флаг режима работы скрипта
     * @throws IOException если произошла ошибка ввода/вывода при работе с каналами или сокетами
     * @throws ClassNotFoundException если класс не найден при десериализации
     * @throws InterruptedException если произошла ошибка в работе потоков
     */
    static void startSelectorLoop(Selector selector, SocketChannel channel, Scanner scanner, boolean scriptMode) throws IOException, ClassNotFoundException, InterruptedException {
        do {
            selector.select();
        } while (startIteratorLoop(selector, channel, scanner, scriptMode));
    }

    /**
     * Запускает итерационный цикл обработки готовых ключей селектора.
     * @param selector    объект типа Selector для мониторинга состояний канала
     * @param channel     объект типа SocketChannel - канал для передачи данных по сети
     * @param scanner          объект типа Scanner для чтения ввода пользователя с консоли
     * @param scriptMode  флаг режима работы скрипта
     * @return true, если нужно продолжать работу цикла, false - если необходимо остановиться
     * @throws IOException если произошла ошибка ввода/вывода при работе с каналами или сокетами
     */
    private static boolean startIteratorLoop(Selector selector, SocketChannel channel, Scanner scanner, boolean scriptMode) throws IOException{
        Set<SelectionKey> readyKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isReadable()) {
                SocketChannel clientChannel = (SocketChannel) key.channel();
                ClientChannelCH socketChannelCH = new ClientChannelCH(clientChannel);
                Response response = (Response) socketChannelCH.receive();
                System.out.println(response.getData());
                clientChannel.register(selector, SelectionKey.OP_WRITE);
            } else if (key.isWritable()) {
                try {
                    CommandsToReceive commandsToReceive = CommandReader.readCommand(scanner, scriptMode);
                    if (commandsToReceive == null) return false;
                    if (commandsToReceive.getCommandName().equalsIgnoreCase("execute_script")) {
                        if (commandsToReceive.getCommandArgs().length != 1) throw new IllegalArgumentException("Arguments exception");
                        ScriptManager scriptReader = new ScriptManager(commandsToReceive);
                        scriptScanner = new Scanner(scriptReader.getPath());
                        SocketChannel client = (SocketChannel) key.channel();
                        ClientChannelCH socketChannelIO = new ClientChannelCH(client);
                        Request request = new Request("execute_script");
                        socketChannelIO.send(request);
                        client.register(selector, SelectionKey.OP_READ);
                        startSelectorLoop(selector, channel, scriptScanner, true);
                        scriptReader.stopScriptReading();
                        startSelectorLoop(selector, channel, scanner, false);
                    }
                    Request request = requestMaker.createRequestOfCommand(commandsToReceive, scanner, scriptMode);
                    SocketChannel client = (SocketChannel) key.channel();
                    ClientChannelCH socketChannelIO = new ClientChannelCH(client);
                    if (commandsToReceive.getCommandName().equalsIgnoreCase("exit") && commandsToReceive.getCommandArgs().length == 0) {
                        try {
                            socketChannelIO.send(request);
                            System.out.println("Goodbye!");
                            System.exit(0);
                        } catch (Exception e) {
                            System.out.println("Server is not available.");
                            System.out.println("Goodbye!");
                            System.exit(0);
                        }
                    } else {
                        if (request == null) throw new NullPointerException("");
                        socketChannelIO.send(request);
                        client.register(selector, SelectionKey.OP_READ);
                    }
                } catch (Exception e) {
                    System.out.println("Error, try again");
                }
            }
        }
        return true;
    }
}
//execute_script