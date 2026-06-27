package Server.commands;

import Common.system.*;
import Server.managers.CollectionManager;

/**
 * Команда head - вывести первый элемент коллекции
 *
 * @author Nazerke
 */

public class head extends Command{
    /**
     * Конструктор класса Head
     *
     * @param collectionManager менеджер коллекции
     */
    public head(CollectionManager collectionManager) {
        super("head","вывести первый элемент коллекции", collectionManager);
    }
    /**
     * Вывести первый элемент коллекции
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request){
        try{
            if (getCollectionManager().getCollection().isEmpty()) {
                return new Response("Collection is empty");
            } else {
                return new Response("First element in collection: ", getCollectionManager().getFirst());
            }
        } catch(Exception e){
            return new Response("Something is wrong");
        }
    }
}

