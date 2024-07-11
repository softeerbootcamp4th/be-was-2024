package processor;

import webserver.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseProcessor {
    private final DataOutputStream dos;

    protected ResponseProcessor(DataOutputStream dos) {
        this.dos = dos;
    }

    public static ResponseProcessor from(DataOutputStream dos) {
        return new ResponseProcessor(dos);
    }

    public void defaultResponse(String path) throws IOException {
        Response response = new Response.Builder()
                .url(path)
                .statusCode(200)
                .dataOutputStream(dos)
                .build();

        response.sendResponse();
    }

    public void notFoundResponse() throws IOException {
        Response response = new Response.Builder()
                .url("/not_found.html")
                .dataOutputStream(dos)
                .redirectCode(404)
                .build();

        response.sendResponse();
    }

    public void createUserResponse() throws IOException {
        Response response = new Response.Builder()
                .url("/index.html")
                .dataOutputStream(dos)
                .redirectCode(302)
                .build();

        response.sendResponse();
    }

    public void loginSuccessResponse(String sessionId) throws IOException {
        Response response = new Response.Builder()
                .url("/main/index.html")
                .dataOutputStream(dos)
                .cookie(sessionId)
                .redirectCode(302)
                .build();

        response.sendResponse();
    }

    public void loginFailedResponse() throws IOException {
        Response response = new Response.Builder()
                .url("/login/login_failed.html")
                .dataOutputStream(dos)
                .redirectCode(404)
                .build();

        response.sendResponse();
    }

    public void logoutResponse() throws IOException {
        Response response = new Response.Builder()
                .url("/index.html")
                .dataOutputStream(dos)
                .redirectCode(302)
                .build();

        response.sendResponse();
    }

    public void loginSuccessWithSessionId() throws IOException {
        Response response = new Response.Builder()
                .url("/main/index.html")
                .statusCode(200)
                .dataOutputStream(dos)
                .build();

        response.sendResponse();
    }

    public void loginResponse(String path) throws IOException {
        Response response = new Response.Builder()
                .url(path)
                .statusCode(200)
                .dataOutputStream(dos)
                .build();

        response.sendResponse();
    }
}
