package distributor;

import processor.UserProcessor;
import webserver.Request;
import webserver.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class PostDistributor extends Distributor {
    Request request;
    Response response;
    UserProcessor userProcessor = new UserProcessor();

    PostDistributor(Request request, Response response) {
        this.request = request;
        this.response = response;
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
        response.redirect("/index.html", dos, 302);
    }

    private void processUserLogin(DataOutputStream dos) throws IOException {
        if (userProcessor.login(request)) {
            response.redirect("/index.html", dos, 302);
        } else {
            response.redirect("/login/login_failed.html", dos, 404);
        }
    }
}
