package distributor;

import handler.SessionHandler;
import model.ViewData;
import processor.ResponseProcessor;
import processor.UserProcessor;
import webserver.Request;

public class PostDistributor extends Distributor {
    Request request;
    UserProcessor userProcessor = new UserProcessor();
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
        if (path.equals("/user/create")) {
            processUserCreate();
        } else if (path.equals("/user/login")) {
            processUserLogin();
        }
    }

    private void processUserCreate() {
        userProcessor.createUser(request);

        String sessionId = SessionHandler.getSessionId(request.parseBody().get("userId"));

        ResponseProcessor responseProcessor = new ResponseProcessor();
        this.viewData = responseProcessor.createUserResponse(sessionId);
    }

    private void processUserLogin() {
        if (userProcessor.login(request)) {
            String sessionId = SessionHandler.getSessionId(request.parseBody().get("userId"));

            ResponseProcessor responseProcessor = new ResponseProcessor();
            this.viewData = responseProcessor.loginSuccessResponse(sessionId);
        } else {
            ResponseProcessor responseProcessor = new ResponseProcessor();
            this.viewData = responseProcessor.loginFailedResponse();
        }
    }

    @Override
    public ViewData getViewData() {
        return this.viewData;
    }
}
