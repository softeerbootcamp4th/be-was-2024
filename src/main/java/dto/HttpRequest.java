package dto;

import constant.HttpMethod;

import java.util.Map;
import java.util.Optional;

public class HttpRequest {
    private HttpMethod httpMethod;
    private String path;
    private Map<String, String> queryParams;
    private String extensionType;

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Optional<String> getPath() {
        return Optional.ofNullable(path);
    }

    public Optional<Map<String, String>> getQueryParams() {
        return Optional.ofNullable(queryParams);
    }

    public Optional<String> getExtensionType() {
        return Optional.ofNullable(extensionType);
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = HttpMethod.valueOf(httpMethod);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public void setExtensionType(String extensionType) {
        this.extensionType = extensionType;
    }
}
