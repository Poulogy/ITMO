package Server.commands;

import Common.system.*;
import Server.managers.CollectionManager;

/**
 * Команда info - вывести в стандартный поток вывода информацию о коллекции
 *
 * @author Nazerke
 */
public class info extends Command{
    /**
     * Конструктор класса info
     *
     * @param collectionManager менеджер коллекции
     */
    public info(CollectionManager collectionManager){
        super("info", "вывести в стандартный поток вывода информацию о коллекции", collectionManager);
    }
    /**
     * Вывести в стандартный поток вывода информацию о коллекции
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request){
        return new Response(getCollectionManager().getInfo());
    }
}
