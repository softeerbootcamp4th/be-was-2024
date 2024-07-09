package processor;

import exception.StatusCodeException;
import type.MIMEType;
import type.StatusCodeType;
import utils.FileUtils;
import webserver.RequestInfo;

import java.io.IOException;

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

        insertHTMLTypeToHeader();
        setResult(StatusCodeType.OK, getResponseHeader(), body);
    }

    private void staticResource() throws StatusCodeException, IOException {
        String staticPath = STATIC_PATH + getPath();
        if (FileUtils.isFile(staticPath)) {
            byte[] body = FileUtils.readFileToBytes(staticPath);

            insertContentTypeToHeader(FileUtils.findMIME(getPath()));
            insertToResponseHeader("Content-Length", String.valueOf(body.length));

            setResult(StatusCodeType.OK, getResponseHeader(), body);
        } else throw new StatusCodeException(StatusCodeType.NOT_FOUND);
    }
}
