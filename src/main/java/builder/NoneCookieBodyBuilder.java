package builder;

import utils.ResourceUtil;
import model.ViewData;

import java.io.IOException;

public class NoneCookieBodyBuilder extends BodyBuilder {
    private final ViewData viewData;

    public NoneCookieBodyBuilder(ViewData viewData) {
        this.viewData = viewData;
    }

    @Override
    public byte[] getBody() throws IOException {
        if (viewData.getUrl().equals("//index.html") || viewData.getUrl().equals("/index.html")) {
            return getIndexHtmlBody();
        } else if (viewData.getUrl().equals("/user/list.html")) {
            return getUserListHtmlBody();
        } else if (viewData.getUrl().equals("/post/index.html")) {
            return getPostHtmlBody();
        } else if (viewData.getUrl().equals("/post/list.html")) {
            return getPostListBody();
        } else {
            return getDefaultBody();
        }
    }

    private byte[] getIndexHtmlBody() throws IOException {
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        String body = htmlBuilder.generateIndexHtml(false, "");
        return body.getBytes();
    }

    private byte[] getUserListHtmlBody() throws IOException {
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        String body = htmlBuilder.generateUserListHtml();
        return body.getBytes();
    }

    private byte[] getDefaultBody() throws IOException {
        ResourceUtil resourceUtil = new ResourceUtil();
        return resourceUtil.getByteArray(viewData.getUrl());
    }

    private byte[] getPostHtmlBody() throws IOException {
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        String body = htmlBuilder.generatePostHtml(viewData.getWriter(), viewData.getTitle());
        return body.getBytes();
    }

    private byte[] getPostListBody() throws IOException {
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        String body = htmlBuilder.generatePostListHtml();
        return body.getBytes();
    }
}
