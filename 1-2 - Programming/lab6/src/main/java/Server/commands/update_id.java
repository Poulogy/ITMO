package Server.commands;

import Common.system.Request;
import Common.system.Response;
import Server.managers.CollectionManager;
import Common.modules.Flat;

/**
 * Команда update_id - обновить значение элемента коллекции, id которого равен заданному
 *
 * @author Nazerke
 */

public class update_id extends Command{
    /**
     * Менеджер коллекции
     */
    CollectionManager collectionManager;
    /**
     * Конструктор класса update_id
     *
     * @param collectionManagers менеджер коллекции
     */
    public update_id(CollectionManager collectionManagers) {
        super("update_id","обновить значение элемента коллекции, id которого равен заданному", collectionManagers);
        this.collectionManager = collectionManagers;
    }
    /**
     * Обновить значение элемента коллекции, id которого равен заданному
     *
     * @param request вводимая в консоль строка
     */
    @Override
    public Response execute(Request request){
        try {
            long id = request.getIDArgument();
            Flat flat = getCollectionManager().getFlatById(id);
            if (flat == null){
                return new Response("There is no flat with such id");
            } else {
                getCollectionManager().remove(id);
                Flat UpdFlat = request.getFlatArgument();;
                UpdFlat.setId(id);
                getCollectionManager().getCollection().add(UpdFlat);
                return new Response("Flats id is " + flat.getId() +"\nFlat was updated");
            }
        } catch (Exception e){
            return new Response("Wrong Id");
        }
    }

}
