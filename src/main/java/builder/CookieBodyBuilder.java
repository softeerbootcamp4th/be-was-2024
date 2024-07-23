package builder;

import utils.ResourceUtil;
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
        if (viewData.getUrl().equals("//index.html") || viewData.getUrl().equals("/index.html")) {
            return getIndexHtmlBody();
        } else if (viewData.getUrl().equals("/user/list.html")) {
            return getUserListHtmlBody();
        } else if (viewData.getUrl().equals("/post/index.html")) {
            return getPostHtmlBody();
        } else {
            return getDefaultBody();
        }
    }

    private byte[] getIndexHtmlBody() throws IOException {
        String sessionId = this.cookie;
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        if (SessionHandler.verifySessionId(sessionId)) {
            // 사용자 아이디로 동적 html 생성
            String body = htmlBuilder.generateIndexHtml(true, sessionId);

            return body.getBytes();
        } else {
            // 그냥 html
            String body = htmlBuilder.generateIndexHtml(false, "");
            return body.getBytes();
        }
    }

    private byte[] getUserListHtmlBody() throws IOException {
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        String body = htmlBuilder.generateUserListHtml();
        return body.getBytes();
    }

    private byte[] getPostHtmlBody() throws IOException {
        String sessionId = this.cookie;
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        String body = htmlBuilder.generatePostHtml(sessionId, viewData.getTitle());
        return body.getBytes();
    }

    private byte[] getDefaultBody() throws IOException {
        ResourceUtil resourceUtil = new ResourceUtil();
        return resourceUtil.getByteArray(viewData.getUrl());
    }
}
