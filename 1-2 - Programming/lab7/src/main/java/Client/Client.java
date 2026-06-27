package Client;

import Client.managers.ScriptManager;
import Client.system.Authorization;
import Client.system.RequestMaker;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;


/**
 * Главный класс, запускающий приложение клиента.
 * Содержит метод main, отвечающий за запуск приложения.
 */
public final class Client {
    private Client() {
        throw new UnsupportedOperationException("This is class, which object/example can not be created");
    }

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
     * Канал для обмена данными с сервером.
     */
    private static SocketChannel clientChannel;


    /**
     * Модуль авторизации.
     */
    static final Authorization authorization = new Authorization(console);

    /**
     * Режим переподключения.
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
                Thread.sleep(7 * 1000);
            }
            clientChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            System.out.println("Client connected");
            clientChannel.configureBlocking(false);
            Selector selector = Selector.open();
            clientChannel.register(selector, SelectionKey.OP_WRITE);
            attempts = 0;
            ClientInit.startSelectorLoop(selector, clientChannel, console, false);
        } catch (ClassNotFoundException e) {
            System.out.println("Attempt to serialize unserializable object");
        } catch (InterruptedException e) {
            System.out.println("Connection failed during inactivity, client reconnection");
        } catch (UnresolvedAddressException e) {
            System.out.println("There is no server with such host, try again");
            main(args);
        } catch (IOException e) {
            System.out.println("Server is not available, attempt #" + (attempts + 1));
            reconnectionMode = true;
            if (attempts == 4) {
                System.out.println("Reconnection failed, try again later");
                System.exit(1);
            }
            attempts++;
            ScriptManager.callStack.clear();
            main(args);
        } catch (NoSuchElementException e) {
            System.out.println("Error exiting...");
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
            System.out.println("Server is not available");
            inputPort();
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
