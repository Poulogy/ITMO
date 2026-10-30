package App.managers;


import java.util.HashMap;

import java.util.logging.Logger;

public class Parse {
    public static HashMap<String, String> parseJson(String request) {
        HashMap<String, String> params = new HashMap<>();
        if (request == null || request.isEmpty()) {
            return params;
        }
        request = request.replace("{", "").replace("}", "").replace("\"", "");
        String[] pairs = request.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                params.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return params;
    }
    public static String parseToJSON(float x, float y, float r, String currentTime, boolean hit, long scriptTime) {
        return String.format("{\"x\": %.1f, \"y\": %f, \"r\": %.1f, \"currentTime\": \"%s\", \"hit\": %b, \"executionTime\": %d}",
                x, y, r, currentTime, hit, scriptTime);
    }
    public static HashMap<String, Object> parseToJSONStorage(float x, float y, float r, String currentTime, boolean hit, long scriptTime) {
        HashMap<String, Object> TableObjects = new HashMap<>();
        TableObjects.put("x", x);
        TableObjects.put("y", y);
        TableObjects.put("r", r);
        TableObjects.put("currentTime", currentTime);
        TableObjects.put("hit", hit);
        TableObjects.put("executionTime", scriptTime);
        return TableObjects;
    }

    public static String validSessionId(String cookies, Logger log) {
        if (cookies == null || cookies.isEmpty()) {
            log.info("Cookies empty");
            cookies =  java.util.UUID.randomUUID().toString().replace("-", "");
        }
        log.info("Получила айди сессии: " + cookies);
        return cookies;
    }
}


