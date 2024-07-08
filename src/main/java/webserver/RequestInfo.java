package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.MIMEType;
import type.MethodType;
import type.StatusCodeType;
import utils.FileUtils;
import utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

public class RequestInfo {
    private static final Logger logger = LoggerFactory.getLogger(RequestInfo.class);

    private final static String STATIC_PATH = "./src/main/resources/static";

    private MethodType method;
    private MIMEType mime;
    private String path;
    private HashMap<String, String> query;
    private HashMap<String, String> header;
    private String body;

    public RequestInfo(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder reqHeader = new StringBuilder();

        String requestLine = reader.readLine(), line;
        reqHeader.append("  ").append(requestLine).append("\n");
        HashMap<String, String> headers = new HashMap<>();
        while ((line = reader.readLine()) != null && !line.equals("")) {
            reqHeader.append("  ").append(line).append("\n");
            String[] KeyValue = line.split(":\\s*");
            headers.put(KeyValue[0], KeyValue[1]);
        }
        this.header = headers;
        logger.debug("\n:: Request ::\n{}", reqHeader);

        if (headers.get("Content-Length") != null) {
            int len = Integer.parseInt(headers.get("Content-Length"));
            char[] buf = new char[len];
            reader.read(buf, 0, len);
            this.body = new String(buf);
            logger.debug("\n:: Response ::\n{}", this.body);
        }

        this.method = findMethod(requestLine);
        String[] seperatedPath = requestLine.split("\\s+")[1].split("\\?");
        this.path = seperatedPath[0];
        if (seperatedPath.length > 1) this.query = StringUtils.param2Map(seperatedPath[1]);
        this.mime = findMIME(path);
    }

    private static MethodType findMethod(String requestLine) {
        return MethodType.valueOf(requestLine.split("\\s+")[0]);
    }

    private static MIMEType findMIME(String path) {
        String[] list = path.split("\\.");
        return MIMEType.findByContentType(list[list.length - 1]);
    }

    public String getMethod() { return method.getValue(); }

    public String getContentType() { return mime.getContentType(); }

    public String getPath() { return path; }

    public HashMap<String, String> getQuery() { return query; }

    public HashMap<String, String> getHeader() { return header; }

    public String getBody() { return body; }
}