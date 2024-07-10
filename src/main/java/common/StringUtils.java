package common;

import java.util.UUID;

public class StringUtils {

    public static String createRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
