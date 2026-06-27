package Server.commands;

import Common.system.*;
import Server.managers.CollectionManager;

/**
 * Команда add - добавить новый элемент в коллекцию
 *
 * @author Nazerke
 */
public class add extends Command{
    /**
     * Менеджер коллекции
     */
    CollectionManager collectionManager;
    /**
     * Конструктор класса Add
     *
     * @param collectionManagers менеджер коллекции
     */
    public add(CollectionManager collectionManagers) {
        super("add","добавить новый элемент в коллекцию", collectionManagers);
        this.collectionManager = collectionManagers;
    }
    /**
     * Добавить новый элемент в коллекцию
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request){
        collectionManager.getCollection().add(request.getFlatArgument());
        return new Response("\nFlat was added to collection");
    }
}
