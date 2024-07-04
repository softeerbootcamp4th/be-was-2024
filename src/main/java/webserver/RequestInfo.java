package webserver;

import type.MIMEType;
import type.MethodType;
import type.StatusCodeType;
import utils.FileUtils;

public class RequestInfo {
    private final static String STATIC_PATH = "./src/main/resources/static";

    private MethodType method;
    private MIMEType mime;
    private String path;
    private Boolean isStaticRequest = false;

    public RequestInfo(String requestLine) {
        method = findMethod(requestLine);
        path = findPath(requestLine);
        mime = findMIME(path);
    }

    private static MethodType findMethod(String requestLine) {
        return MethodType.valueOf(requestLine.split(" ")[0]);
    }

    private String findPath(String requestLine) {
        String reqPath = requestLine.split(" ")[1];

        // 정적 파일이 있는지 확인
        String staticPath = STATIC_PATH + reqPath;
        if (FileUtils.isExists(staticPath)) {
            if (FileUtils.isFile(staticPath)) {
                setIsStaticRequestTrue();
                return staticPath;
            }
            staticPath += (reqPath.endsWith("/") ? "index.html" : "/index.html");
            if (FileUtils.isFile(staticPath)) {
                setIsStaticRequestTrue();
                return staticPath;
            }
        }

        return reqPath;
    }

    private static MIMEType findMIME(String path) {
        String[] list = path.split("\\.");
        return MIMEType.findByContentType(list[list.length - 1]);
    }

    private void setIsStaticRequestTrue() { this.isStaticRequest = true; }

    public String getMethod() { return method.getValue(); }

    public String getContentType() { return mime.getContentType(); }

    public String getPath() { return path; }

    public Boolean isStaticRequest() { return isStaticRequest; }
}