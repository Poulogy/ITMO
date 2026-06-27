package Server.managers;

import Common.system.Request;
import Common.system.Response;
import Server.commands.Command;

/**
 * Класс для построения ответа на запрос клиента, используя переданную команду и запрос.
 */
public class RequestBuilder {
    /**
     * Строит ответ на запрос клиента, используя переданную команду и запрос.
     * Если команда требует аргумента организации, устанавливает у организации новый id, сгенерированный менеджером коллекции.
     * @param command команда для выполнения
     * @param request запрос клиента
     * @return ответ на запрос клиента
     */
    public static Response build(Command command, Request request, CollectionManager collectionManager) {
        try {
            if (request.getFlatArgument() != null) {
                request.getFlatArgument().setId(collectionManager.generateNewId());
                new Response("Flat ID is " + request.getFlatArgument().getId());
            }
            return command.execute(request);
        } catch (Exception e) {
            new Response("Response maker error");
            return null;
        }
    }
}
