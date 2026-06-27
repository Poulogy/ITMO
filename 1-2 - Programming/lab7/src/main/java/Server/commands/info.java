package Server.commands;

import Common.system.*;
import Server.system.CollectionManager;

/**
 * Команда info - вывести в стандартный поток вывода информацию о коллекции
 *
 * @author Nazerke
 */
public class info extends Command{
    CollectionManager collectionManager;
    /**
     * Конструктор класса info
     *
     * @param collectionManager менеджер коллекции
     */
    public info(CollectionManager collectionManager){
        super("info", "вывести в стандартный поток вывода информацию о коллекции", 0);
        this.collectionManager = collectionManager;
    }
    /**
     * Вывести в стандартный поток вывода информацию о коллекции
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request){
        return new Response(collectionManager.getInfo());
    }
}
