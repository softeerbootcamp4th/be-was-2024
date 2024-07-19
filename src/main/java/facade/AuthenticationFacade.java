package facade;

import common.ResponseUtils;
import web.HttpRequest;
import web.ViewPath;

import java.io.IOException;
import java.io.OutputStream;

public class AuthenticationFacade {

    public static void redirectHomeIfNotAuthenticated(HttpRequest request, OutputStream out) throws IOException {
        if(!SessionFacade.isAuthenticatedRequest(request)) {
            ResponseUtils.redirectToView(ViewPath.DEFAULT).writeInBytes(out);
        }
    }

    public static void redirectHomeIfAuthenticated(HttpRequest request, OutputStream out) throws IOException {
        if(SessionFacade.isAuthenticatedRequest(request)) {
            ResponseUtils.redirectToView(ViewPath.DEFAULT).writeInBytes(out);
        }
    }
}
