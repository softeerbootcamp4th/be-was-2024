package util;

import model.HttpRequest;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestConverter {
    private static final String CHARSET = "UTF-8";
    private static final String SPACE = " ";
    private static final String EMPTY_SPACE = "";


    /**
     * InputStream을 HttpRequest 객체로 변환하는 로직
     */
    public HttpRequest with(InputStream in) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in, CHARSET));
        String httpRequestLine = buffer.readLine();
        String httpMethod = httpRequestLine.split(SPACE)[0];
        String path = httpRequestLine.split(SPACE)[1];
        String protocolVersion = httpRequestLine.split(SPACE)[2];

        Map<String, String> headers = new HashMap<>();
        while (!httpRequestLine.isEmpty()) {
            httpRequestLine = buffer.readLine();
            httpRequestLine.replaceAll(SPACE, EMPTY_SPACE);
            String headerName = httpRequestLine.split(":")[0];
            String headerValue = httpRequestLine.split(":")[1];
            headers.put(headerName, headerValue);
        }

        return HttpRequest.from(httpMethod, path, protocolVersion, headers);

    }


}
