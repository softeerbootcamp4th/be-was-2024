package utils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StringUtils {
    public static HashMap<String, String> paramToMap(String str, String token) {
        HashMap<String, String> map = new HashMap<>();
        Arrays.stream(str.split(token))
                .forEach(val -> {
                    String[] keyValue = val.split("=");
                    map.put(keyValue[0].trim(), keyValue.length > 1 ? keyValue[1].trim() : "");
                });
        return map;
    }

    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(characters.length());
            stringBuilder.append(characters.charAt(number));
        }

        return stringBuilder.toString();
    }
}
