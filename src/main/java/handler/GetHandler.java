package handler;

import http.HttpRequest;
import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Utils.ResponseWithStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static util.ResponseFactory.*;
import static util.ResponseFactory.addHeader;
import static util.Utils.*;

public class GetHandler {
    private static final Logger log = LoggerFactory.getLogger(GetHandler.class);
    private static String staticPath = getStaticPath();

    public static void sendResponse(String requestUrl, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String[] tokens = requestUrl.split("\\.");
        String type = tokens[tokens.length - 1];

        ResponseWithStatus responseWithStatus = getFileContent(staticPath + requestUrl);

        HttpStatus status = responseWithStatus.status;
        byte[] body = responseWithStatus.body;

        addHeader(dos, status);
        addContentType(dos, getContentType(type));
        addContentLength(dos, body.length);
        responseBody(dos, body);
    }
}
