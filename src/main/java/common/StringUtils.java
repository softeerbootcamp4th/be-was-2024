package common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StringUtils {

    public static String createRandomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * url-encoded 형태로 오는 body를 Map으로 파싱
     * TODO 현재 내용이 empty일 때 문제 발생
     */
    public static Map<String, String> parseBodyInForm(byte[] body) {
        HashMap<String, String> map = new HashMap<>();
        String bodyStr = new String(body);
        String[] chunks = bodyStr.split("&");
        for (String chunk : chunks) {
            String key = chunk.split("=")[0];
            String value = chunk.split("=")[1];
            map.put(key, value);
        }
        return map;
    }
}
