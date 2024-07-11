package distributor;

import handler.SessionHandler;
import processor.UserProcessor;
import webserver.Request;
import webserver.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class PostDistributor extends Distributor {
    Request request;
    UserProcessor userProcessor = new UserProcessor();

    PostDistributor(Request request) {
        this.request = request;
    }

    @Override
    public void process(DataOutputStream dos) throws IOException {
        processQuery(dos);
    }

    private void processQuery(DataOutputStream dos) throws IOException {
        String path = request.getPath();
        if (path.equals("/user/create")) {
            processUserCreate(dos);
        } else if (path.equals("/user/login")) {
            processUserLogin(dos);
        }
    }

    private void processUserCreate(DataOutputStream dos) throws IOException {
        userProcessor.createUser(request);
        Response response = new Response.Builder()
                .url("/index.html")
                .dataOutputStream(dos)
                .redirectCode(302)
                .build();

        response.sendResponse();
    }

    private void processUserLogin(DataOutputStream dos) throws IOException {
        if (userProcessor.login(request)) {
            String sessionId = SessionHandler.getSessionId(request.parseBody().get("userId"));
            Response response = new Response.Builder()
                    .url("/main/index.html")
                    .dataOutputStream(dos)
                    .cookie(sessionId)
                    .redirectCode(302)
                    .build();

            response.sendResponse();
        } else {
            Response response = new Response.Builder()
                    .url("/login/login_failed.html")
                    .dataOutputStream(dos)
                    .redirectCode(404)
                    .build();

            response.sendResponse();
        }
    }
}
