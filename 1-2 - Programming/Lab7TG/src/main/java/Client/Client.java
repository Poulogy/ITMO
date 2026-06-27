package Client;

import Client.system.Authorization;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


/**
 * Главный класс, запускающий приложение клиента.
 * Содержит метод main, отвечающий за запуск приложения.
 */
public final class Client {
    private Client() {
        throw new UnsupportedOperationException("This is class, which object/example can not be created");
    }
    /**
     * Бот
     */
    public static Bot bot = new Bot();

    /**
     * Модуль авторизации.
     */
    static final Authorization authorization = new Authorization(bot);


    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        }  catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
