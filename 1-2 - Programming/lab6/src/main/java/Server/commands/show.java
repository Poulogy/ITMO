package Server.commands;

import Common.system.Request;
import Common.system.Response;
import Server.managers.CollectionManager;
import Common.modules.Flat;

/**
 * Команда show - вывести в стандартный поток вывода все элементы коллекции в строковом представлении
 *
 * @author Nazerke
 */
public class show extends Command{
    /**
     * Конструктор класса show
     *
     * @param collectionManager менеджер коллекции
     */
    public show(CollectionManager collectionManager) {
        super("show","вывести в стандартный поток вывода все элементы коллекции в строковом представлении", collectionManager);
    }
    /**
     * Вывести в стандартный поток вывода все элементы коллекции в строковом представлении
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request){
        if (getCollectionManager().getCollection().isEmpty()) {
            return new Response("Collection is empty");
        } else {
            return new Response(getCollectionManager().showCollection());
        }
    }

}
