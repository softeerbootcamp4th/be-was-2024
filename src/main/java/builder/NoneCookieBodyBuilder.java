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
        ResourceHandler resourceHandler = new ResourceHandler();
        return resourceHandler.getByteArray(viewData.getUrl());
    }
}
