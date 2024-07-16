package builder;

import model.ViewData;

import java.io.IOException;

public class BodyBuilder {
    public static BodyBuilder from(ViewData viewData) {
        if (viewData.getCookie() != null) {
            if (viewData.getCookie().isEmpty()) {
                return new NoneCookieBodyBuilder(viewData);
            } else {
                return new CookieBodyBuilder(viewData);
            }
        } else {
            return new NoneCookieBodyBuilder(viewData);
        }
    }

    public byte[] getBody() throws IOException {
        return null;
    }
}
