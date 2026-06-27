package Server;

import java.io.*;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Главный класс, запускающий приложение сервера.
 * Содержит метод main, отвечающий за запуск приложения.
 */
public final class Server {

    private Server() {
        throw new UnsupportedOperationException("This class, which example can not be created.");
    }

    public static void main(String[] args) {
        try {
            System.out.println(InetAddress.getLocalHost().getHostName());
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            askForPort(serverSocketChannel);
            ServerApp app = new ServerApp(serverSocketChannel);
            app.start();
        } catch (IOException e) {
            ServerInitConfig.logger.warning("Error in logger.");
            System.exit(1);
        }
    }


    /**
     * Метод inputPort запрашивает данные для создания соединения.
     */
    public static void askForPort(ServerSocketChannel server) {
        Scanner sc = ServerInitConfig.scanner;
        System.out.println("Do you want to use default port.[y/n]");
        try {
            String s = sc.nextLine().trim().toLowerCase(Locale.ROOT);
            if ("n".equals(s)) {
                System.out.println("Enter port (1-65535): ");
                String port = sc.nextLine();
                try {
                    int portInt = Integer.parseInt(port);
                    if (portInt > 0 && portInt <= 65535) {
                        ServerInitConfig.PORT = portInt;
                        server.socket().bind(new InetSocketAddress(ServerInitConfig.PORT));
                    } else {
                        System.out.println("Number is out of range. Try again");
                        askForPort(server);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error with number processing, try again.");
                    askForPort(server);
                }
            } else if ("y".equals(s)) {
                System.out.println("Default port is used");
                server.socket().bind(new InetSocketAddress(ServerInitConfig.PORT));
            } else {
                System.out.println("You've entered an invalid symbol. Try again");
                askForPort(server);
            }
        } catch (NoSuchElementException e) {
            System.out.println("Error exitting...");
            ServerInitConfig.logger.info("Server exitting");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println("Error with number processing, try again.");
            askForPort(server);
        } catch (BindException e) {
            System.out.println("This port is not available. Try again.");
            askForPort(server);
        } catch (IOException e) {
            System.out.println("IO error");
            askForPort(server);
        }
    }
}