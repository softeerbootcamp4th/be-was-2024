package processor;

import exception.StatusCodeException;
import type.HTTPStatusCode;
import utils.FileUtils;
import webserver.RequestInfo;

import java.io.IOException;

public class RegistrationRequestProcessor extends RequestProcessor {
    public RegistrationRequestProcessor(RequestInfo requestInfo) throws StatusCodeException, IOException {
        init(requestInfo);

        switch (methodPath()) {
            case "GET /registration" -> index();
            default -> throw new StatusCodeException(HTTPStatusCode.NOT_FOUND);
        }
    }

    // GET /registration
    private void index() throws IOException {
        String path = STATIC_PATH + getPath() + "/index.html";
        byte[] body = FileUtils.readFileToBytes(path);

        insertHTMLTypeToHeader();

        setResult(HTTPStatusCode.OK, getResponseHeader(), body);
    }
}
