package distributor;

import handler.SessionHandler;
import model.ViewData;
import processor.PostProcessor;
import processor.ResponseProcessor;
import processor.UserProcessor;
import webserver.Request;

public class PostDistributor extends Distributor {
    Request request;
    UserProcessor userProcessor = new UserProcessor();
    PostProcessor postProcessor = new PostProcessor();
    ViewData viewData;

    protected PostDistributor(Request request) {
        this.request = request;
    }

    @Override
    public void process() {
        processQuery();
    }

    private void processQuery() {
        String path = request.getPath();
        switch (path) {
            case "/user/create" -> processUserCreate();
            case "/user/write" -> processUserWrite();
            case "/user/login" -> processUserLogin();
        }
    }

    private void processUserCreate() {
        userProcessor.createUser(request);

        String sessionId = SessionHandler.getSessionId(request.parseBody().get("userId"));

        ResponseProcessor responseProcessor = new ResponseProcessor();
        this.viewData = responseProcessor.createUserResponse(sessionId);
    }

    private void processUserLogin() {
        ResponseProcessor responseProcessor = new ResponseProcessor();
        if (userProcessor.login(request)) {
            String sessionId = SessionHandler.getSessionId(request.parseBody().get("userId"));

            this.viewData = responseProcessor.loginSuccessResponse(sessionId);
        } else {
            this.viewData = responseProcessor.loginFailedResponse();
        }
    }

    private void processUserWrite() {
        ResponseProcessor responseProcessor = new ResponseProcessor();

        postProcessor.addPost(request);

        this.viewData = responseProcessor.writePostResponse();
    }

    @Override
    public ViewData getViewData() {
        return this.viewData;
    }
}
