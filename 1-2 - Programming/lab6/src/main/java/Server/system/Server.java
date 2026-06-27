package Server.system;

import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Главный класс, запускающий приложение сервера.
 * Содержит метод main, отвечающий за запуск приложения.
 */
public final class Server {
    /**
     * Имя файла, в котором хранится коллекция.
     */
    static String fileName;
    /**
     * Объект, который используется для прослушивания входящих клиентских подключений на сервере.
     */
    private static ServerSocket serverSocket;

    public static void main(String[] args) {

        try {
            System.out.println(InetAddress.getLocalHost().getHostName());
            fileName = args[0];
            inputPort();
            ServerInit.collectionManager.loadCollection(fileName);
            Console console = new Console();
            console.start();
            ServerInit.startServer(args, serverSocket);
        } catch (Exception e) {
            ServerInit.logger.warning("Error " + e.getMessage());
        }

    }

    /**
     * Метод inputPort запрашивает данные для создания соединения.
     */
    private static void inputPort() {
        Scanner sc = ServerInit.scanner;
        System.out.println("Do you want to use default port? [y/n]");
        try {
            String s = sc.nextLine().trim().toLowerCase(Locale.ROOT);
            if ("n".equals(s)) {
                System.out.println("Enter port (1-65535):");
                String port = sc.nextLine();
                try {
                    int portInt = Integer.parseInt(port);
                    if (portInt > 0 && portInt <= 65535) {
                        ServerInit.PORT = portInt;
                        serverSocket = new ServerSocket(portInt);
                    } else {
                        System.out.println("Out of range port error, try again!");
                        inputPort();
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Illegal port value, try again!");
                    inputPort();
                }
            } else if ("y".equals(s)) {
                serverSocket = new ServerSocket(ServerInit.PORT);
            } else {
                System.out.println("You have entered an invalid value, please try again!");
                inputPort();
            }
        } catch (NoSuchElementException e) {
            System.out.println("Exiting client.");
            ServerInit.logger.info("Server exiting");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal port value, try again!");
            inputPort();
        } catch (BindException e) {
            System.out.println("This port is not available, please try again");
            inputPort();
        } catch (IOException e) {
            System.out.println("IO Error");
            inputPort();
        }
    }
}
