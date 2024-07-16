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
            processNonQuery();
        }
    }

    private void processQuery() {
        String path = request.getPath();
        if (path.equals("/user/create")) {
            ResponseProcessor responseProcessor = new ResponseProcessor();
            this.viewData = responseProcessor.notFoundResponse();
        }
    }

    private void processNonQuery() {
        String path = request.getPath();
        if (path.equals("/logout")) {
            processLogout();
        } else if (path.equals("/login/index.html")) {
            processLogin(path);
        } else {
            processDefault(path);
        }
    }

    private void processDefault(String path) {
        System.out.println("default process");
        ResponseProcessor responseProcessor = new ResponseProcessor();
        this.viewData = responseProcessor.defaultResponse(path);
    }

    private void processLogout() {
        // 세션 삭제
        String userId = request.getSessionId();
        SessionHandler.deleteSession(userId);

        ResponseProcessor responseProcessor = new ResponseProcessor();
        this.viewData = responseProcessor.logoutResponse();
    }

    private void processLogin(String path) {
        String sessionId = request.getSessionId();

        // 만약 세션아이디가 존재한다면 그냥 로그인 화면으로 이동
        if (SessionHandler.verifySessionId(sessionId)) {
            ResponseProcessor responseProcessor = new ResponseProcessor();
            this.viewData = responseProcessor.loginSuccessWithSessionId();
        } else {
            // 세션아이디가 존재하지 않는다면 그대로
            ResponseProcessor responseProcessor = new ResponseProcessor();
            this.viewData = responseProcessor.loginResponse(path);
        }
    }

    @Override
    public ViewData getViewData() {
        return this.viewData;
    }
}
