package processor;

import exception.StatusCodeException;
import type.MIMEType;
import type.StatusCodeType;
import utils.FileUtils;
import webserver.RequestInfo;

import java.io.IOException;

public class RegistrationRequestProcessor extends RequestProcessor {
    public RegistrationRequestProcessor(RequestInfo requestInfo) throws StatusCodeException, IOException {
        init(requestInfo);

        switch (methodPath()) {
            case "GET /registration" -> index();
            default -> throw new StatusCodeException(StatusCodeType.NOT_FOUND);
        }
    }

    // GET /registration
    private void index() throws IOException {
        String path = STATIC_PATH + getPath() + "/index.html";
        byte[] body = FileUtils.readFileToBytes(path);

        insert2ResponseHeader("Content-Type", MIMEType.HTML.getContentType());
        insert2ResponseHeader("Content-Length", String.valueOf(body.length));

        setResult(StatusCodeType.OK, getResponseHeader(), body);
    }
}
