package Client;

import Client.managers.ScriptManager;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;


/**
 * Главный класс, запускающий приложение клиента.
 * Содержит метод main, отвечающий за запуск приложения.
 */
public final class Client {

    /**
     * Порт по умолчанию
     */
    private static int PORT = 65435;

    /**
     * Хост, к которому пытается подключиться клиент.
     */
    private static String HOST;

    /**
     * Максимально возможный номер порта.
     */
    private static final int maxPort = 65535;

    /**
     * Сканнер для считывания ввода с консоли.
     */
    private static final Scanner console = new Scanner(System.in);

    /**
     * Селектор, используемый для отслеживания событий в сокете.
     */
    private static Selector selector;

    /**
     * Режим переподключения, установленный в режим ожидания.
     */
    private static boolean reconnectionMode = false;

    /**
     * Количество неудачных попыток подключения к серверу.
     */
    private static int attempts = 0;
    public static void main(String[] args) {
        try {
            if (!reconnectionMode) {
                inputPort();
            } else {
                Thread.sleep(7 * 1000); // 7 секунд на переподключение
            }
            SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            System.out.println("Client connected");
            attempts = 0;
            clientChannel.configureBlocking(false);
            selector = Selector.open();
            clientChannel.register(selector, SelectionKey.OP_WRITE);
            ClientInit.startSelectorLoop(selector, clientChannel, console, false);
        } catch (ClassNotFoundException e) {
            System.out.println("This object can not be serialized");
        } catch (InterruptedException e) {
            System.out.println("Connection is interrupted in case of inactivity. Restart client");
            main(args);
        } catch (UnresolvedAddressException e) {
            System.out.println("There is no server with such Host name, try again");
            main(args);
        } catch (IOException e) {
            System.out.println();
            System.out.println("Server is not available. Attempt of connecting N" + (attempts + 1));
            reconnectionMode = true;
            if (attempts == 4) {
                System.out.println("Reconnection failed, please try again");
                System.exit(0);
            }
            attempts++;
            ScriptManager.callStack.clear();
            main(args);
        } catch (NoSuchElementException e) {
            System.out.println("Exiting client.");
            System.exit(1);
        }
    }

    /**
     * Метод inputPort запрашивает у пользователя данные для создания соединения.
     */
    private static void inputPort() {
        System.out.println("Enter Host name: ");
        try {
            HOST = console.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println("Exiting client.");
            System.exit(1);
        }
        System.out.println("Do you want to use default port? [y/n]");
        try {
            String s = console.nextLine();
            if ("n".equals(s)) {
                System.out.println("Enter port (1-65535):");
                String port = console.nextLine();
                try {
                    int portInt = Integer.parseInt(port);
                    if (portInt > 0 && portInt <= maxPort) {
                        PORT = portInt;
                    } else {
                        System.out.println("Out of range port error, try again!");
                        inputPort();
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Illegal port value, try again!");
                    inputPort();
                }
            } else if (!"y".equals(s)) {
                System.out.println("You have entered an invalid value, please try again!");
                inputPort();
            }
        } catch (NoSuchElementException e) {
            System.out.println("Exiting client.");
            System.exit(1);
        }
    }
}
