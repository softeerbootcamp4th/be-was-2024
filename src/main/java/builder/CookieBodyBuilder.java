package builder;

import handler.ResourceHandler;
import handler.SessionHandler;
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
        if (viewData.getUrl().equals("//index.html")) {
            return getIndexHtmlBody();
        } else {
            return getDefaultBody();
        }
    }

    private byte[] getIndexHtmlBody() throws IOException {
        String sessionId = viewData.getCookie();
        if (SessionHandler.verifySessionId(sessionId)) {
            // 사용자 아이디로 동적 html 생성
            return getDefaultBody();
        } else {
            // 그냥 html
            return getDefaultBody();
        }
    }

    private byte[] getDefaultBody() throws IOException {
        ResourceHandler resourceHandler = new ResourceHandler();
        return resourceHandler.getByteArray(viewData.getUrl());
    }
}
