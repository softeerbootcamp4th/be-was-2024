package http.utils;

import chain.record.FormItem;
import config.AppConfig;
import http.MyHttpRequest;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FormDataUtil {
    public static void addFormData(MyHttpRequest req, String key, FormItem value) {
        if(key.matches("\\w+\\[.*\\]")) {
            Object check = req.getStoreData(AppConfig.MULTIPART_PREFIX + key);
            if(check == null) {
                List<FormItem> _itemList = new ArrayList<>();
                req.setStoreData(AppConfig.MULTIPART_PREFIX + key, _itemList);
                check = _itemList;
            }
            List<FormItem> items = (List<FormItem>) check;
            items.add(value);
        } else {
            req.setStoreData(AppConfig.MULTIPART_PREFIX + key, value);
        }
    }

    public static Object getFormData(MyHttpRequest req, String key) {
        return req.getStoreData(AppConfig.MULTIPART_PREFIX + key);
    }
}
