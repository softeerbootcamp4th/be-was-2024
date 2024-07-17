package builder;

import handler.ResourceHandler;
import model.ViewData;

import java.io.IOException;

public class NoneCookieBodyBuilder extends BodyBuilder {
    private final ViewData viewData;

    public NoneCookieBodyBuilder(ViewData viewData) {
        this.viewData = viewData;
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
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        String body = htmlBuilder.generateHtml(false, "");
        return body.getBytes();
    }

    private byte[] getDefaultBody() throws IOException {
        ResourceHandler resourceHandler = new ResourceHandler();
        return resourceHandler.getByteArray(viewData.getUrl());
    }
}
