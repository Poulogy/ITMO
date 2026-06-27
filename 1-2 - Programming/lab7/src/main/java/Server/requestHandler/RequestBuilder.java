package Server.requestHandler;

import Common.system.*;
import Server.ServerInitConfig;

import java.util.function.Function;

/**
 * Класс для построения ответа на запрос клиента, используя переданную команду и запрос.
 */
public class RequestBuilder implements Function<Request, Response> {

    /**
     * Обработчик запросов.
     * @param request запрос клиента.
     * @return ответ сервера на запрос клиента.
     */
    @Override
    public Response apply(Request request) {
        if (request != null) {
            if (request.getRequestType().equals(RequestType.COMMAND)) {
                return ServerInitConfig.commandManager.initCommand(request).execute(request);
            } else if (request.getRequestType().equals(RequestType.REGISTER)) {
                return ServerInitConfig.usersManager.registerUser(request);
            } else {
                return ServerInitConfig.usersManager.logInUser(request);
            }
        } else return null;
    }
}
