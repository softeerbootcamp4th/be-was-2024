package builder;

import utils.ResourceUtil;
import handler.SessionHandler;
import model.User;
import model.ViewData;

import java.io.IOException;

public class CookieBodyBuilder extends BodyBuilder {
    private final ViewData viewData;
    private final String cookie;

    public CookieBodyBuilder(ViewData viewData) {
        this.viewData = viewData;
        this.cookie = viewData.getCookie();
    }

    @Override
    public byte[] getBody() throws IOException {
        if (viewData.getUrl().equals("//index.html") || viewData.getUrl().equals("/index.html")) {
            return getIndexHtmlBody();
        } else {
            return getDefaultBody();
        }
    }

    private byte[] getIndexHtmlBody() throws IOException {
        String sessionId = viewData.getCookie();
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        if (SessionHandler.verifySessionId(sessionId)) {
            // 사용자 아이디로 동적 html 생성
            User user = SessionHandler.getUser(sessionId);
            String userId = user.getUserId();

            String body = htmlBuilder.generateHtml(true, userId);

            return body.getBytes();
        } else {
            // 그냥 html
            String body = htmlBuilder.generateHtml(false, "");
            return body.getBytes();
        }
    }

    private byte[] getDefaultBody() throws IOException {
        ResourceUtil resourceUtil = new ResourceUtil();
        return resourceUtil.getByteArray(viewData.getUrl());
    }
}
