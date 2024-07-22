package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.MIME;
import type.HTTPMethod;
import utils.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class RequestInfo {
    private static final Logger logger = LoggerFactory.getLogger(RequestInfo.class);

    private HTTPMethod method;
    private String path;
    private HashMap<String, String> queryParam;
    private HashMap<String, String> header;
    private HashMap<String, String> cookie;
    private byte[] body;

    public RequestInfo(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        StringBuilder reqHeader = new StringBuilder();

        String requestLine = readLine(in), line;
        reqHeader.append("  ").append(requestLine).append("\n");
        HashMap<String, String> headers = new HashMap<>();
        while ((line = readLine(in)) != null && !line.isEmpty()) {
            reqHeader.append("  ").append(line).append("\n");
            String[] KeyValue = line.split(":\\s*");
            headers.put(KeyValue[0].toLowerCase(), KeyValue[1]);
        }
        this.header = headers;
        logger.debug("\n:: Request ::\n{}", reqHeader);

        if (headers.get("content-length") != null) {
            int len = Integer.parseInt(headers.get("content-length"));
            int bytesRead;
            while (len > 0 && (bytesRead = in.read(data, 0, Math.min(data.length, len))) != -1) {
                buffer.write(data, 0, bytesRead);
                len -= bytesRead;
            }
            this.body = buffer.toByteArray();
        }

        if (headers.get("cookie") != null) this.cookie = StringUtils.paramToMap(headers.get("cookie"), ";");
        else this.cookie = new HashMap<>();

        this.method = findMethod(requestLine);
        String[] seperatedPath = requestLine.split("\\s+")[1].split("\\?");
        this.path = seperatedPath[0];
        if (seperatedPath.length > 1) this.queryParam = StringUtils.paramToMap(seperatedPath[1], "&");
        else this.queryParam = new HashMap<>();
    }

    private String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            if (c == '\r') continue;
            if (c == '\n') break;
            sb.append((char) c);
        }
        return sb.toString();
    }

    private static HTTPMethod findMethod(String requestLine) {
        return HTTPMethod.valueOf(requestLine.split("\\s+")[0]);
    }

    private static MIME findMIME(String path) {
        String[] list = path.split("\\.");
        return MIME.findByContentType(list.length > 1 ? list[list.length - 1] : "");
    }

    public String getMethod() { return method.getValue(); }

    public String getPath() { return path; }

    public HashMap<String, String> getQuery() { return queryParam; }

    public HashMap<String, String> getHeader() { return header; }

    public HashMap<String, String> getCookie() { return cookie; }

    public byte[] getBody() { return body; }
}