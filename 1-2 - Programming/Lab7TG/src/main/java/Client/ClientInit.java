package Client;

import Client.system.ClientChannelCH;

import Common.system.Request;
import Common.system.Response;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.nio.channels.*;
import java.util.*;


/**
 * Класс для установки соединения с сервером.
 */
public class ClientInit {

    /**
     * Логин.
     */
    public static String login;

    /**
     * Пароль.
     */
    public static String password;

    public static Bot bot = Client.bot;

    /**
     * Отправка обычного сообщения до авторизации на сервер
     * @param chId       Айди чата, который отправил сообщение
     * @param channel    объект типа SocketChannel - канал для передачи данных по сети
     * @param request    Запрос собранный из данных сообщений чата
     * @return response ответ на реквест
     */
    static Response send(SocketChannel channel, Request request, String chId) throws TelegramApiException {
        try {
            ClientChannelCH socketChannelIO = new ClientChannelCH(channel);
            socketChannelIO.send(request);
            Thread.sleep(1000);
            Response response = (Response) socketChannelIO.receive();
            return response;
        } catch (IOException e) {
            bot.sendMess("Ошибка принятия ответа от сервера", chId);
        } catch (ClassNotFoundException e) {
            bot.sendMess("Ошибка ", chId);
        } catch (InterruptedException e) {
            bot.sendMess(" ", chId);
        } catch (NullPointerException e) {
            bot.sendMess("Server response error", chId);
        }
        return null;
    }
    /**
     *
     * Отправка сообщения с командой на сервер
     * @param chI        Айди чата, который отправил сообщение
     * @param channel    объект типа SocketChannel - канал для передачи данных по сети
     * @param request    Запрос собранный из данных сообщений чата
     * @return response ответ на реквест
     */
    static Response sendCommand(SocketChannel channel, Request request, String chI) throws Exception {
        SocketChannel client = channel;
        ClientChannelCH socketChannelIO = new ClientChannelCH(client);
        request.setLogin(bot.getLogin(chI));
        request.setPassword(bot.getPassword(chI));
        socketChannelIO.send(request);
        Thread.sleep(1000);
        Response response = (Response) socketChannelIO.receive();
        return response;

    }
}