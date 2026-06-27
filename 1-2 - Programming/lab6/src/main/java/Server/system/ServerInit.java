package Server.system;


import Server.commands.*;
import Common.system.*;
import Server.managers.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

public class ServerInit {
    /**
     * Сканер для чтения ввода.
     */
    public static final Scanner scanner = new Scanner(System.in);

    /**
     * Командный менеджер, содержащий список команд для обработки запросов клиентов.
     */
    public static CommandManager commandManager = new CommandManager();


    /**
     * Файловый менеджер.
     */
    public static final DumpManager dumpManager = new DumpManager(Server.fileName);

    /**
     * Менеджер коллекции.
     */
    public static CollectionManager collectionManager = commandManager.getCollectionMan();

    /**
     * Состояние сервера.
     */
    public static boolean running = true;

    /**
     * Логгер для записи сообщений о работе сервера.
     */
    public static final Logger logger = Logger.getLogger(Server.class.getName());
    static {
        try {
            FileHandler fileHandler = new FileHandler("log.log", true);
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(new SimpleFormatter());
            ServerInit.logger.addHandler(fileHandler);
        } catch (IOException e) {
            ServerInit.logger.warning("Could not open file:  " + e.getMessage());
        } catch (SecurityException e) {
            ServerInit.logger.warning("Have no access to file: " + e.getMessage());
        } catch (Exception e) {
            ServerInit.logger.warning("Could not open file:  " + e.getMessage());
        }
    }
    /**
     * Стандартный порт.
     */
    protected static int PORT = 65435;

    /**
     * Метод для запуска сервера.
     * @param args аргументы командной строки.
     * @param serverSocket объект ServerSocket, прослушивающий порт.
     * @throws IOException если возникли проблемы с вводом-выводом.
     */
    static void startServer(String[] args, ServerSocket serverSocket) {
        try {
            ServerInit.logger.info("Server is running");
            ExecutorService executorService = Executors.newCachedThreadPool();
            while (running) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> {
                    try {
                        ServerInit.logger.info("Client connection");
                        startSelectorLoop(clientSocket, serverSocket);
                    } catch (Exception e) {
                        if (e.getMessage().contains("Connection reset"));
                        else { ServerInit.logger.severe(e.getMessage()); }
                    }
                });
            }
        } catch (Exception e) {
            ServerInit.logger.warning("Can not receive client connection:  " + e.getMessage());
        }
    }

    /**
     * Метод, который запускает бесконечный цикл обработки запросов клиента.
     * @param socket сокет для подключения клиента.
     * @param serverSocket серверный сокет для принятия клиентских подключений.
     */
    private static void startSelectorLoop(Socket socket, ServerSocket serverSocket) {
        try {
            while (socket.isConnected()) {
                startIteratorLoop(socket, serverSocket);
            }
        }catch (Exception e){
            ServerInit.logger.warning("Can not receive client connection: ");
        }

    }

    /**
     * Метод, который обрабатывает запросы клиента.
     * @param socket сокет для подключения клиента.
     * @param serverSocket серверный сокет для принятия клиентских подключений.
     * @throws IOException если возникает ошибка ввода/вывода при работе с сокетами.
     */
    private static void startIteratorLoop(Socket socket, ServerSocket serverSocket) throws Exception{
        boolean isClientDisconnected = false;
        try {
            ServerSocketCH socketIO = new ServerSocketCH(socket);
            Request request = (Request) socketIO.receive();
            Command command = ServerInit.commandManager.initCommand(request);
            ServerInit.logger.info("Server received a command: [" + request.getCommandName() + "].");
            if (request.getCommandName().equals("exit")) {
                ServerInit.logger.info("Exiting client");
                try {
                    socket.close();
                } catch (IOException e) {
                    ServerInit.logger.warning("Socket closing error: " + e.getMessage());
                }
            } else {
                Response response = RequestBuilder.build(command, request, collectionManager);
                socketIO.send(response);
                ServerInit.logger.info("Server sent response to client.");
            }
        } catch (Exception e) {
            socket.close();
            serverSocket.close();
            ServerInit.logger.severe("Server exiting");
            System.exit(1);
        } finally {
            if (isClientDisconnected) {
                try {
                    socket.close();
                } catch (Exception e) {
                    ServerInit.logger.warning("Socket closing error: " + e.getMessage());
                }
            }
        }
    }
}
