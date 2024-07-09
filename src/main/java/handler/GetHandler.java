package handler;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Utils.ResponseWithStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static util.Utils.*;

public class GetHandler {
    private static final Logger log = LoggerFactory.getLogger(GetHandler.class);
    private static String staticPath = getStaticPath();

    public static void sendResponse(String requestUrl, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String[] tokens = requestUrl.split("\\.");
        String type = tokens[tokens.length - 1];

        ResponseWithStatus responseWithStatus = getFileContent(staticPath + requestUrl);

        HttpStatus httpStatus = responseWithStatus.status;
        byte[] body = responseWithStatus.body;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", getContentType(type));
        headers.put("Content-Length", String.valueOf(body.length));

        HttpResponse response = new HttpResponse(httpStatus, headers, body);
        System.out.println("response = " + response.toString());
        dos.writeBytes(response.toString());
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
