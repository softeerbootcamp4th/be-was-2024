package distributor;

import handler.SessionHandler;
import webserver.Request;
import webserver.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class GetDistributor extends Distributor {
    Request request;
    DataOutputStream dos;

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
            Response response = new Response.Builder()
                    .url("/not_found.html")
                    .dataOutputStream(dos)
                    .redirectCode(404)
                    .build();

            response.sendResponse();
        }
    }

    private void processNonQuery(DataOutputStream dos) throws IOException {
        String path = request.getPath();
        if (path.equals("/logout")) {
            // 세션 삭제
            String userId = request.getSessionId();
            SessionHandler.deleteSession(userId);

            Response response = new Response.Builder()
                    .url("/index.html")
                    .dataOutputStream(dos)
                    .redirectCode(302)
                    .build();

            response.sendResponse();
        } else if (path.equals("/login/index.html")) {
            String sessionId = request.getSessionId();

            // 만약 세션아이디가 존재한다면 그냥 로그인 화면으로 이동
            if (SessionHandler.verifySessionId(sessionId)) {
                Response response = new Response.Builder()
                        .url("/main/index.html")
                        .statusCode(200)
                        .dataOutputStream(dos)
                        .build();

                response.sendResponse();
            } else {
                // 세션아이디가 존재하지 않는다면 그대로
                Response response = new Response.Builder()
                        .url(path)
                        .statusCode(200)
                        .dataOutputStream(dos)
                        .build();

                response.sendResponse();
            }
        } else {
            Response response = new Response.Builder()
                    .url(path)
                    .statusCode(200)
                    .dataOutputStream(dos)
                    .build();

            response.sendResponse();
        }
    }
}
