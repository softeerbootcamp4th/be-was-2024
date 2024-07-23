package http.form;

import config.AppConfig;
import http.MyHttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 유저 측에서 전달된 multipart/form-data 데이터를 request에서 편리하게 사용하기 위한 유틸 기능
 */
public class FormDataUtil {
    /**
     * 요청 객체에 form 데이터를 삽입한다. 키 식별자 뒤에 []가 포함되면 배열로 삽입한다.
     * @param req form 데이터를 삽입할 요청 객체
     * @param key 삽입할 데이터의 식별자
     * @param value 삽입할 데이터
     */
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

    /**
     * request 객체에서 form data를 가져온다
     * @param req form 데이터를 가져올 요청 객체
     * @param key 삽입한 데이터의 식별자
     * @return 삽입한 데이터 ( 없으면 null )
     */
    public static Object getFormData(MyHttpRequest req, String key) {
        return req.getStoreData(AppConfig.MULTIPART_PREFIX + key);
    }
}
