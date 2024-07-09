package webserver;

import processor.UserRequestProcessor;
import type.StatusCodeType;
import utils.FileUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RequestDispatcher {

    private String requestLine;
    private RequestInfo requestInfo;

    public RequestDispatcher(String requestLine) {
        this.requestLine = requestLine;
        requestInfo = new RequestInfo(requestLine);
    }

    public RequestResult dispatch() throws IOException {
        /* Static HTTP Request */
        if (requestInfo.isStaticRequest()) {
            String staticPath = requestInfo.getPath();
            byte[] body = FileUtils.readFileToBytes(staticPath);

            HashMap<String, String> responseHeader = new HashMap<>();
            responseHeader.put("Content-Type", requestInfo.getContentType());
            responseHeader.put("Content-Length", String.valueOf(body.length));

            return new RequestResult(StatusCodeType.OK, responseHeader, body);
        }

        /* Dynamic HTTP Request */
        // Path, Query 분리
        List<String> list = Arrays.asList(requestInfo.getPath().split("\\?", 2));
        String path = list.get(0);
        HashMap<String, String> query = new HashMap<>();
        if (list.size() > 1 && !list.get(1).isEmpty()) {
            Arrays.stream(list.get(1).split("&"))
                    .forEach(key -> {
                        String[] keyValue = key.split("=");
                        query.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
                    });
        }

        // 요청 경로에 따른 처리 분리
        if (path.startsWith("/create")) {
            return new UserRequestProcessor(requestInfo.getMethod(), path, query).getResult();
        }
        return new RequestResult(StatusCodeType.NOT_FOUND, "404 Not Found");
    }
}
