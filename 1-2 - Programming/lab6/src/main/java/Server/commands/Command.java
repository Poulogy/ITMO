package Server.commands;

import Common.system.Request;
import Common.system.Response;
import Server.managers.CollectionManager;

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
     * Менеджер коллекции
     */
    private CollectionManager collectionManager;

    /**
     * Конструктор класса Command
     *
     * @param collectionManager    менеджер коллекции
     * @param name                 название команды
     * @param description          описание команды
     */
    public Command(String name, String description, CollectionManager collectionManager) {
        this.name = name;
        this.description = description;
        this.collectionManager = collectionManager;
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
