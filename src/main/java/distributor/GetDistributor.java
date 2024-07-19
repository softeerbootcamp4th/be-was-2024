package distributor;

import handler.SessionHandler;
import model.ViewData;
import processor.ResponseProcessor;
import webserver.Request;

public class GetDistributor extends Distributor {
    Request request;
    ViewData viewData;

    protected GetDistributor(Request request) {
        this.request = request;
    }

    @Override
    public void process() {
        if (request.isQueryString()) {
            processQuery();
        } else {
            processNoneQuery();
        }
    }

    private void processQuery() {
        String path = request.getPath();
        if (path.equals("/user/create")) {
            ResponseProcessor responseProcessor = new ResponseProcessor();
            this.viewData = responseProcessor.notFoundResponse();
        } else if (path.equals("/posts")) {
            processShowPost();
        }
    }

    private void processNoneQuery() {
        String path = request.getPath();
        switch (path) {
            case "/logout" -> processLogout();
            case "/login/index.html" -> processLogin(path);
            case "/user/list" -> processUserList();
            case "/write/index.html" -> processWrite();
            default -> processDefault(path);
        }
    }

    private void processDefault(String path) {
        ResponseProcessor responseProcessor = new ResponseProcessor();
        this.viewData = responseProcessor.defaultResponse(path, request.getSessionId());
    }

    private void processLogout() {
        // 세션 삭제
        String sessionId = request.getSessionId();
        SessionHandler.deleteSession(sessionId);

        ResponseProcessor responseProcessor = new ResponseProcessor();
        this.viewData = responseProcessor.logoutResponse();
    }

    private void processLogin(String path) {
        String sessionId = request.getSessionId();
        ResponseProcessor responseProcessor = new ResponseProcessor();

        // 만약 세션아이디가 존재한다면 그냥 로그인 화면으로 이동
        if (SessionHandler.verifySessionId(sessionId)) {
            this.viewData = responseProcessor.loginSuccessWithSessionId(sessionId);
        } else {
            // 세션아이디가 존재하지 않는다면 그대로
            this.viewData = responseProcessor.loginResponse(path, sessionId);
        }
    }

    private void processUserList() {
        String sessionId = request.getSessionId();
        ResponseProcessor responseProcessor = new ResponseProcessor();

        if (SessionHandler.verifySessionId(sessionId)) {
            this.viewData = responseProcessor.userListResponse();
        } else {
            this.viewData = responseProcessor.unauthorizedUserListResponse();
        }
    }

    private void processWrite() {
        String sessionId = request.getSessionId();
        ResponseProcessor responseProcessor = new ResponseProcessor();

        if (SessionHandler.verifySessionId(sessionId)) {
            this.viewData = responseProcessor.writeResponse();
        } else {
            this.viewData = responseProcessor.unauthorizedWriteResponse();
        }
    }

    private void processShowPost() {
        String sessionId = request.getSessionId();
        String title = request.parseQueryString().get("title");
        ResponseProcessor responseProcessor = new ResponseProcessor();

        this.viewData = responseProcessor.showPostResponse(sessionId, title);
    }

    @Override
    public ViewData getViewData() {
        return this.viewData;
    }
}
