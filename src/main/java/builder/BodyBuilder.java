package builder;

import model.ViewData;
import utils.CookieUtil;

import java.io.IOException;

public class BodyBuilder {
    public static BodyBuilder from(ViewData viewData) {
        if (CookieUtil.isExist(viewData.getCookie())) {
            return new CookieBodyBuilder(viewData);
        } else {
            return new NoneCookieBodyBuilder(viewData);
        }
    }

    public byte[] getBody() throws IOException {
        return null;
    }
}
