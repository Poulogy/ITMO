package Client;

import Client.system.ClientChannelCH;
import Client.commands.*;
import Client.managers.*;
import Client.system.RequestMaker;
import Common.system.*;

import java.io.*;
import java.nio.channels.*;
import java.util.*;


/**
 * Класс для установки соединения с сервером.
 */
public class ClientInit {
    /**
     * Объект для создания запросов к серверу.
     */
    static final RequestMaker REQUEST_MAKER = new RequestMaker();

    /**
     * Сканнер для чтения команд из скрипта.
     */
    private static Scanner scriptScanner;
    /**
     * Логин.
     */
    public static String login;

    /**
     * Пароль.
     */
    public static String password;

    /**
     * Запускает бесконечный цикл селектора.
     *
     * @param selector   объект типа Selector для мониторинга состояний канала
     * @param channel    объект типа SocketChannel - канал для передачи данных по сети
     * @param scanner    объект типа Scanner для чтения ввода пользователя с консоли
     * @param scriptMode флаг режима работы скрипта
     * @throws IOException            если произошла ошибка ввода/вывода при работе с каналами или сокетами
     * @throws ClassNotFoundException если класс не найден при десериализации
     * @throws InterruptedException   если произошла ошибка в работе потоков
     */
    static void startSelectorLoop(Selector selector, SocketChannel channel, Scanner scanner, boolean scriptMode) throws IOException, ClassNotFoundException, InterruptedException {
        do {
            selector.select();
        } while (startIteratorLoop(channel, scanner, scriptMode, selector));
    }

    /**
     * Запускает итерационный цикл обработки готовых ключей селектора.
     *
     * @param selector   объект типа Selector для мониторинга состояний канала
     * @param channel    объект типа SocketChannel - канал для передачи данных по сети
     * @param scriptMode флаг режима работы скрипта
     * @return true, если нужно продолжать работу цикла, false - если необходимо остановиться
     * @throws IOException если произошла ошибка ввода/вывода при работе с каналами или сокетами
     */
    private static boolean startIteratorLoop(SocketChannel channel, Scanner sc, boolean scriptMode, Selector selector) throws IOException, ClassNotFoundException {
        Set<SelectionKey> readyKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isReadable()) {
                SocketChannel clientChannel = (SocketChannel) key.channel();
                ClientChannelCH socketChannelIO = new ClientChannelCH(clientChannel);
                Response response = (Response) socketChannelIO.receive();
                if (response.getInfo() != null) {
                    Client.authorization.validateRegistration(response);
                } else {
                    System.out.println(response.getData());
                }
                clientChannel.register(selector, SelectionKey.OP_WRITE);
            } else if (key.isWritable()) {
                try {
                    if (!Client.authorization.isAuthorizationDone()) {
                        Request request = Client.authorization.askForRegistration();
                        SocketChannel client = (SocketChannel) key.channel();

                        ClientChannelCH socketChannelIO = new ClientChannelCH(client);
                        socketChannelIO.send(request);

                        client.register(selector, SelectionKey.OP_READ);
                        continue;
                    }
                    CommandsToReceive commandToSend = CommandReader.readCommand(sc, scriptMode);
                    if (commandToSend == null) {
                        return false;}
                    if (commandToSend.getCommandName().equalsIgnoreCase("execute_script")) {
                        CommandValidate.validateAmountOfArgs(commandToSend.getCommandArgs(), 1);
                        ScriptManager scriptReader = new ScriptManager(commandToSend);
                        scriptScanner = new Scanner(scriptReader.getPath());
                        SocketChannel client = (SocketChannel) key.channel();
                        ClientChannelCH socketChannelIO = new ClientChannelCH(client);
                        Request request = new Request("execute_script", RequestType.COMMAND);
                        socketChannelIO.send(request);
                        client.register(selector, SelectionKey.OP_READ);
                        startSelectorLoop(selector, channel, scriptScanner, true);
                        scriptReader.stopScriptReading();
                        startSelectorLoop(selector, channel, sc, false);
                    }
                    Request request = REQUEST_MAKER.createRequestOfCommand(commandToSend, sc, scriptMode);
                    if (request == null) throw new NullPointerException("");
                    SocketChannel client = (SocketChannel) key.channel();
                    ClientChannelCH socketChannelIO = new ClientChannelCH(client);
                    request.setLogin(login);
                    request.setPassword(password);
                    if (commandToSend.getCommandName().equalsIgnoreCase("exit")) {
                        try {
                            System.out.println("Goodbye!");
                            socketChannelIO.send(request);
                            System.exit(0);
                        } catch (Exception e) {
                            System.out.println("Server is not available" + "Goodbye!");
                            System.exit(0);
                        }
                    } else {
                        socketChannelIO.send(request);
                        client.register(selector, SelectionKey.OP_READ);
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return true;
    }
}