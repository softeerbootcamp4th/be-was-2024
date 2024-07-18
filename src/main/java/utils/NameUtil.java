package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class NameUtil {
    public static String genRandomName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        Date date = new Date();
        return UUID.randomUUID() + "@" + formatter.format(date);
    }
}
