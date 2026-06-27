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
            /*System.out.println(InetAddress.getLocalHost().getHostName());*/
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
        try {
            server.socket().bind(new InetSocketAddress(ServerInitConfig.PORT));
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