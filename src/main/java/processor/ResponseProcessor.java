package processor;

import model.ViewData;

public class ResponseProcessor {
    public ViewData defaultResponse(String path, String sessionId) {
        ViewData viewData = new ViewData.Builder()
                .url(path)
                .statusCode(200)
                .cookie(sessionId)
                .build();

        return viewData;
    }

    public ViewData notFoundResponse() {
        ViewData viewData = new ViewData.Builder()
                .url("/not_found.html")
                .statusCode(404)
                .build();

        return viewData;
    }

    public ViewData createUserResponse() {
        ViewData viewData = new ViewData.Builder()
                .url("/index.html")
                .redirectCode(302)
                .build();

        return viewData;
    }

    public ViewData loginSuccessResponse(String sessionId) {
        ViewData viewData = new ViewData.Builder()
                .url("/main/index.html")
                .cookie(sessionId)
                .redirectCode(302)
                .build();

        return viewData;
    }

    public ViewData loginFailedResponse() {
        ViewData viewData = new ViewData.Builder()
                .url("/login/login_failed.html")
                .statusCode(404)
                .build();

        return viewData;
    }

    public ViewData logoutResponse() {
        ViewData viewData = new ViewData.Builder()
                .url("/index.html")
                .redirectCode(302)
                .build();

        return viewData;
    }

    public ViewData loginSuccessWithSessionId(String sessionId) {
        ViewData viewData = new ViewData.Builder()
                .url("/main/index.html")
                .statusCode(200)
                .cookie(sessionId)
                .build();

        return viewData;
    }

    public ViewData loginResponse(String path, String sessionId) {
        ViewData viewData = new ViewData.Builder()
                .url(path)
                .statusCode(200)
                .cookie(sessionId)
                .build();

        return viewData;
    }
}
