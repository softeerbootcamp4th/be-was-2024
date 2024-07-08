package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StringUtils {
    public static HashMap<String, String> param2Map(String str) {
        HashMap<String, String> map = new HashMap<>();
        Arrays.stream(str.split("&"))
                .forEach(val -> {
                    String[] keyValue = val.split("=");
                    map.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
                });
        return map;
    }
}
