package App;

import App.managers.StorageManager;
import com.fastcgi.FCGIInterface;
import App.managers.Parse;
import App.managers.RequestFCGI;
import App.managers.ResponseFCGI;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;


public class Main{
    private static final Logger log = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {

        FCGIInterface fcgi = new FCGIInterface();
        log.info("Сервер запущен.");
        StorageManager storageManager = new StorageManager();
        log.info("Создано хранилище для данных.");

        while (fcgi.FCGIaccept() >= 0) {
            long startTime = System.currentTimeMillis();
            String cookies = FCGIInterface.request.params.getProperty("HTTP_COOKIE");
            log.info("Получила информацию о сайте: " + cookies);
            String sessionId = Parse.validSessionId(cookies, log);

            try {
                String requestBody = RequestFCGI.readRequest();
                log.info("Получение запроса");

                HashMap<String, String> parametrs = Parse.parseJson(requestBody);
                log.info("Парсинг JSON");
                log.info("Проверка наличия параметров");
                if (!parametrs.containsKey("type")) {
                    ResponseFCGI.sendTextResponse("Недостаточно параметров");
                    continue;
                }
                RequestFCGI.checkRequest(parametrs, log, startTime, storageManager);


                // Парсинг чисел


            } catch (IOException e) {
                ResponseFCGI.sendTextResponse("Ошибка чтения запроса: " + e.getMessage());
                log.info("Ошибка чтения запроса");
            } catch (NumberFormatException e) {
                ResponseFCGI.sendTextResponse("Некорректный формат чисел");
                log.info("Некорректный формат чисел");
            } catch (Exception e) {
                ResponseFCGI.sendTextResponse("Внутренняя ошибка сервера");
                log.info("Внутренняя ошибка сервера" + e.getMessage());
            }
        }
    }

}