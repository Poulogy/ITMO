package App.managers;

import App.check.Validation;
import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import static App.check.DotTest.checkHit;

public class RequestFCGI {
    public static String readRequest() throws IOException {
        FCGIInterface.request.inStream.fill();
        int contentLength = FCGIInterface.request.inStream.available();

        if (contentLength <= 0) {
            return "";
        }

        byte[] buffer = new byte[contentLength];
        int bytesRead = FCGIInterface.request.inStream.read(buffer, 0, contentLength);

        return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
    }
    public static void checkRequest(HashMap<String, String> params, Logger log, long startTime, StorageManager storageManager) {
        DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (params.get("type").equals("Update")) {
            float x = Float.parseFloat(params.get("x"));
            float y = Float.parseFloat(params.get("y"));
            float r = Float.parseFloat(params.get("r"));
            String sessionId = params.get("sessionId");
            Validation validation = new Validation();
            log.info("Валидация параметров");
            if  (!validation.validate(x,y,r)) {
                ResponseFCGI.sendTextResponse("Ошибка валидации");
                log.info("Ошибка валидации");
            } else{
                log.info("Валидация успешная");
                boolean hit = checkHit(x, y, r);
                log.info("Проверка попадания успешна");
                String currentTime = LocalDateTime.now().format(TIME_FORMATTER);
                long executionTime = System.currentTimeMillis() - startTime;
                String parsedInfo = Parse.parseToJSON(x, y, r, currentTime, hit, executionTime);
                HashMap<String, Object> parsedInfoSt = Parse.parseToJSONStorage(x, y, r, currentTime, hit, executionTime);
                log.info("Парсинг ответа в JSON");
                storageManager.putStorage(sessionId, parsedInfoSt, log);
                log.info("Положили ответ в хранилище.");
                log.info("Отправка успешного ответа");
                ResponseFCGI.sendSuccessResponse(parsedInfo);
            }
        }
        if (params.get("type").equals("Load")) {
            String sessionId = params.get("sessionId");
            sessionId = Parse.validSessionId(sessionId, log);
            List<HashMap<String, Object>> storageInfo = storageManager.getStorage(sessionId, log);
            if (storageInfo.isEmpty()) {
                ResponseFCGI.sendTextResponse("There was no storage information in this session id");
            } else {
                log.info("Получили таблицу, отправляем её");
                ResponseFCGI.sendStorageResponse(storageInfo, sessionId);
            }
        }
        if (params.get("type").equals("Clear")) {
            String sessionId = params.get("sessionId");
            log.info("Очистка хранилища данной сессии");
            storageManager.Storage.remove(sessionId);
            ResponseFCGI.sendTextResponse("Данные хранилища очищены");
        }
    }
}
