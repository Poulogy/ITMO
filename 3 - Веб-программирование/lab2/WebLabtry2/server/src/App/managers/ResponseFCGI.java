package App.managers;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;

public class ResponseFCGI {
    private static final Gson gson = new Gson();
    private static final String RESPONSE_TEMPLATE_WITHCOOKIES = "set-cookie: sessionId=%s;\ncontent-Type: application/json\nContent-Length: %d\n\n%s";
    private static final String RESPONSE_TEMPLATE = "content-Type: application/json\nContent-Length: %d\n\n%s";

    public static void sendSuccessResponse(String parsedInfo){
        System.out.printf(RESPONSE_TEMPLATE + "%n", parsedInfo.getBytes(StandardCharsets.UTF_8).length, parsedInfo);
    }

    public static void sendStorageResponse(List<HashMap<String, Object>> storageInfo, String cookies){
        String jsonResponse = gson.toJson(storageInfo);
        System.out.printf(RESPONSE_TEMPLATE_WITHCOOKIES + "%n", cookies, jsonResponse.getBytes(StandardCharsets.UTF_8).length, jsonResponse);
    }

    public static void sendTextResponse(String parsedInfo){
        parsedInfo = gson.toJson(parsedInfo);
        System.out.printf(RESPONSE_TEMPLATE + "%n", parsedInfo.getBytes(StandardCharsets.UTF_8).length, parsedInfo);

    }
}
