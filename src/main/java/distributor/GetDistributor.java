package distributor;

import handler.SessionHandler;
import model.ViewData;
import processor.ResponseProcessor;
import webserver.Request;

import java.io.DataOutputStream;
import java.io.IOException;

public class GetDistributor extends Distributor {
    Request request;
    DataOutputStream dos;
    ViewData viewData;

    protected GetDistributor(Request request, DataOutputStream dos) {
        this.request = request;
        this.dos = dos;
    }

    @Override
    public void process() throws IOException {
        if (request.isQueryString()) {
            processQuery(this.dos);
        } else {
            processNonQuery(this.dos);
        }
    }

    private void processQuery(DataOutputStream dos) throws IOException {
        String path = request.getPath();
        if (path.equals("/user/create")) {
            ResponseProcessor responseProcessor = new ResponseProcessor();
            this.viewData = responseProcessor.notFoundResponse();
        }
    }

    private void processNonQuery(DataOutputStream dos) throws IOException {
        String path = request.getPath();
        if (path.equals("/logout")) {
            processLogout(dos);
        } else if (path.equals("/login/index.html")) {
            processLogin(dos, path);
        } else {
            processDefault(dos, path);
        }
    }

    private void processDefault(DataOutputStream dos, String path) throws IOException {
        System.out.println("default process");
        ResponseProcessor responseProcessor = new ResponseProcessor();
        this.viewData = responseProcessor.defaultResponse(path);
    }

    private void processLogout(DataOutputStream dos) throws IOException {
        // 세션 삭제
        String userId = request.getSessionId();
        SessionHandler.deleteSession(userId);

        ResponseProcessor responseProcessor = new ResponseProcessor();
        this.viewData = responseProcessor.logoutResponse();
    }

    private void processLogin(DataOutputStream dos, String path) throws IOException {
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
