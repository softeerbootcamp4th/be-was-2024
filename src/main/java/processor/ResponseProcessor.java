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
}
