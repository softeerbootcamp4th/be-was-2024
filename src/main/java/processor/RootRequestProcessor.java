package processor;

import db.Database;
import exception.StatusCodeException;
import model.User;
import type.HTTPStatusCode;
import utils.FileUtils;
import webserver.RequestInfo;

import java.io.IOException;
import java.util.HashMap;

public class RootRequestProcessor extends RequestProcessor {

    public RootRequestProcessor(RequestInfo requestInfo) throws IOException, StatusCodeException {
        init(requestInfo);

        switch (methodPath()) {
            case "GET /" -> index();
            default -> staticResource();
        }
    }

    private void index() throws IOException {
        String path = STATIC_PATH + getPath() + "index.html";
        byte[] body = FileUtils.readFileToBytes(path);

        HashMap<String, String> cookie = getCookie();

        HashMap<String, String> bodyParams = new HashMap<>();

        String sid = cookie.get("sid");
        if (sid != null) {
            if (Database.findUserIdBySessionId(sid) == null) {
                insertToResponseHeader("Set-Cookie", "sid=; Path=/; Max-Age=0");
            } else {
                bodyParams.put("hasSID", "true");
                String userId = Database.findUserIdBySessionId(sid);
                bodyParams.put("userId", userId);
                User user = Database.findUserById(userId);
                bodyParams.put("userName", user.getName());
            }
        }

        insertHTMLTypeToHeader();
        setResult(HTTPStatusCode.OK, getResponseHeader(), body, bodyParams);
    }

    private void staticResource() throws StatusCodeException, IOException {
        String staticPath = STATIC_PATH + getPath();
        if (FileUtils.isFile(staticPath)) {
            byte[] body = FileUtils.readFileToBytes(staticPath);

            insertContentTypeToHeader(FileUtils.findMIME(getPath()));
            insertToResponseHeader("Content-Length", String.valueOf(body.length));

            setResult(HTTPStatusCode.OK, getResponseHeader(), body);
        } else throw new StatusCodeException(HTTPStatusCode.NOT_FOUND);
    }
}
