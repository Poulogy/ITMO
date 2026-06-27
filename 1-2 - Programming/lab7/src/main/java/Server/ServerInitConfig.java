package Server;

import Server.databases.*;
import Server.system.*;

import java.io.IOException;

import java.util.Scanner;
import java.util.logging.*;

/**
 Класс, представляющий конфигурацию сервера.
 */
public class ServerInitConfig {



    /**
     * Логгер для записи сообщений о работе сервера.
     */
    public static final Logger logger = Logger.getLogger(Server.class.getName());
    static {
        try {
            FileHandler fileHandler = new FileHandler("log.log", true);
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(new SimpleFormatter());
            ServerInitConfig.logger.addHandler(fileHandler);
        } catch (IOException e) {
            ServerInitConfig.logger.warning("Could not open file:  " + e.getMessage());
        }
    }
    /**
     Переменная, указывающая на то, работает ли сервер.
     */
    public static boolean isRunning = true;

    /**
     Объект класса Scanner для считывания ввода пользователя.
     */
    public static final Scanner scanner = new Scanner(System.in);
    /**
     * * Объект класса CommandManager для работы с командами.
     */
    public static CommandManager commandManager = new CommandManager();

    /**
     Объект класса CollectionManager для работы с коллекцией элементов.
     */
    public static CollectionManager collectionManager = commandManager.getCollectionManager();

    /**
     Порт, используемый сервером.
     */
    public static int PORT = 65435;
    /**
     Объект класса DBManager для работы с базой данных.
     */
    public static DatabaseManager dbManager = commandManager.getDbManager();

    /**
     Объект класса UsersManager для работы с пользователями.
     */
    public static UserManagers usersManager = new UserManagers(dbManager);
    static {
        try {
            collectionManager.setCollection(dbManager.loadCollection());
        } catch (Exception e) {
            logger.warning(e.getMessage());
            System.exit(1);
        }
    }




    /**
     * Метод для изменения переменной isRunning, указывающего на то, работает ли сервер.
     */
    public static void toggleStatus() {
            isRunning = !isRunning;
        }
    }

