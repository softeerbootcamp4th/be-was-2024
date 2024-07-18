package processor;

import model.ViewData;

public class ResponseProcessor {
    public ViewData defaultResponse(String path, String sessionId) {
        return new ViewData.Builder()
                .url(path)
                .statusCode(200)
                .cookie(sessionId)
                .build();
    }

    public ViewData notFoundResponse() {
        return new ViewData.Builder()
                .url("/not_found.html")
                .statusCode(404)
                .build();
    }

    public ViewData createUserResponse(String sessionId) {
        return new ViewData.Builder()
                .url("/index.html")
                .redirectCode(302)
                .cookie(sessionId)
                .build();
    }

    public ViewData loginSuccessResponse(String sessionId) {
        return new ViewData.Builder()
                .url("/index.html")
                .cookie(sessionId)
                .redirectCode(302)
                .build();
    }

    public ViewData loginFailedResponse() {
        return new ViewData.Builder()
                .url("/login/login_failed.html")
                .statusCode(404)
                .build();
    }

    public ViewData logoutResponse() {
        return new ViewData.Builder()
                .url("/index.html")
                .redirectCode(302)
                .build();
    }

    public ViewData loginSuccessWithSessionId(String sessionId) {
        return new ViewData.Builder()
                .url("/index.html")
                .statusCode(200)
                .cookie(sessionId)
                .build();
    }

    public ViewData loginResponse(String path, String sessionId) {
        return new ViewData.Builder()
                .url(path)
                .statusCode(200)
                .cookie(sessionId)
                .build();
    }

    public ViewData userListResponse() {
        return new ViewData.Builder()
                .url("/user/list.html")
                .statusCode(200)
                .build();
    }

    public ViewData unauthorizedUserListResponse() {
        return new ViewData.Builder()
                .url("/index.html")
                .redirectCode(302)
                .build();
    }

    public ViewData writeResponse() {
        return new ViewData.Builder()
                .url("/write/index.html")
                .statusCode(200)
                .build();
    }

    public ViewData unauthorizedWriteResponse() {
        return new ViewData.Builder()
                .url("/login/index.html")
                .redirectCode(302)
                .build();
    }

    public ViewData writePostResponse() {
        return new ViewData.Builder()
                .url("/index.html")
                .redirectCode(302)
                .build();
    }
}
