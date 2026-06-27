package Server.commands;

import Common.system.*;
import Server.databases.DatabaseManager;
import Server.system.CollectionManager;

/**
 * Абстрактный класс команд
 *
 * @author Nazerke
 */
public abstract class Command {
    /**
     * Название команды
     */
    private final String name;
    /**
     * Описание команды
     */
    private final String description;
    /**
     * Количество аргументов у команды
     */
    private final int amountOfArgs;

    public DatabaseManager getDbManager() {
        return dbManager;
    }

    /**
     * Менеджер БД
     */
    protected DatabaseManager dbManager;
    /**
     * Менеджер коллекции
     */
    private CollectionManager collectionManager;

    /**
     * Конструктор класса Command
     *
     * @param amountOfArgs         количество аргументов
     * @param name                 название команды
     * @param description          описание команды
     */
    public Command(String name, String description, int amountOfArgs) {
        this.name = name;
        this.description = description;
        this.amountOfArgs = amountOfArgs;
    }
    /**
     * Конструктор класса Command
     *
     * @param dbManager            Менеджер баз данных
     * @param collectionManager    Менеджер коллекций
     * @param amountOfArgs         количество аргументов
     * @param name                 название команды
     * @param description          описание команды
     */
    public Command(String name, String description, int amountOfArgs, CollectionManager collectionManager, DatabaseManager dbManager) {
        this.name = name;
        this.description = description;
        this.amountOfArgs = amountOfArgs;
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }
    /**
     * Получить менеджер коллекции
     *
     * @return менеджер коллекции
     */
    public CollectionManager getCollectionManager() {
        return collectionManager;
    }
    /**
     * Установить менеджер коллекции
     *
     * @param collectionManager менеджер коллекции
     */

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    /**
     * Получить название команды
     *
     * @return название команды
     */
    public String getName() {
        return name;
    }
    /**
     * Получить описание команды
     *
     * @return описание команды
     */
    public String getDescription() {
        return description;
    }
    /**
     * Метод для выполнения команды
     * @param request запрос пользователя
     * @return ответ сервера
     */
    public abstract Response execute(Request request);
}
