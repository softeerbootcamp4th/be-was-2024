package webserver;

import exception.StatusCodeException;
import processor.RegistrationRequestProcessor;
import processor.RootRequestProcessor;
import processor.UserRequestProcessor;
import type.HTTPStatusCode;

import java.io.IOException;

public class RequestDispatcher {
    private RequestInfo requestInfo;

    public RequestDispatcher(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public RequestResult dispatch() throws IOException {
        // 요청 경로에 따른 처리 분리
        String path = requestInfo.getPath();
        RequestResult rr = null;
        try {
            if (path.startsWith("/registration")) {
                rr = new RegistrationRequestProcessor(requestInfo).getResult();
            } else if (path.startsWith("/user")) {
                rr = new UserRequestProcessor(requestInfo).getResult();
            } else rr = new RootRequestProcessor(requestInfo).getResult(); // startsWith("/")
        } catch(StatusCodeException e) {
            switch (e.getStatusCode()) {
                case NOT_FOUND -> rr = new RequestResult(HTTPStatusCode.NOT_FOUND, "404 Not Found");
            }
        }
        return rr;
    }
}
