package Client.system;

import Client.*;
import Common.system.*;

import java.util.List;

/**
 * Модуль авторизации пользователя в приложении.
 */
public class Authorization {

    /**
     * Bot
     */
    private Bot bot;

    /**
     * Режим авторизации
     */
    private boolean authorizationDone = false;

    /**
     * Создает новый объект модуля авторизации пользователя с помощью переданного объекта сканера.
     */
    public Authorization(Bot bot) {
        this.bot = bot;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String s;

    /**
     * Проверяет результат запроса на регистрацию и выводит соответствующее сообщение.
     *
     * @param response объект ответа, содержащий информацию о результате запроса на регистрацию.
     */
    public void validateRegistration(Response response, String userId) {
        List<String> usersInfo = response.getInfo();
        if (usersInfo.size() == 2) {
            ClientInit.login = usersInfo.get(0);
            ClientInit.password = usersInfo.get(1);
            setAuthorizationDone(true);
            bot.setBotState(userId, BotState.Enter);
        }
    }

    /**
     * Метод, возвращающий информацию о том, выполнена ли авторизация в приложении.
     * @return true, если авторизация была выполнена успешно, иначе false.
     */
    public boolean isAuthorizationDone()  {
        return authorizationDone;
    }

    /**
     * Метод, устанавливающий флаг выполнения авторизации в приложении.
     * @param authorizationDone флаг выполнения авторизации.
     */
    public void setAuthorizationDone(boolean authorizationDone) {
        this.authorizationDone = authorizationDone;
    }

}