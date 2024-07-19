package view;

import config.AppConfig;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileReadUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * View를 제공하는 클래스
 */
public class MyView {
    private static final Logger logger = LoggerFactory.getLogger(MyView.class);

    public static void render(MyHttpRequest req, MyHttpResponse res, String path, Map<String,Object> items) {

        Map<String, Object> _items = new HashMap<>(req.getStore());
        if(items != null ) _items.putAll(items);

        try {
            byte[] template = FileReadUtil.read(AppConfig.TEMPLATE_BASE_PATH + path + ".html");
            String templateString = new String(template, StandardCharsets.UTF_8);

            String hydratedTemplate = ViewBuilder.build(templateString, _items);
            res.setBody(hydratedTemplate);
        }  catch (Exception e) {
            logger.error(e.getMessage());
            // 내부 오류가 발생했으므로 기존 메시지 내용 초기화
            res.setBody(new byte[0]);
            res.setStatusInfo(HttpStatusType.INTERNAL_SERVER_ERROR);
        }
    }

    public static void render(MyHttpRequest req, MyHttpResponse res, String path) {
        render(req, res, path, null);
    }
}
