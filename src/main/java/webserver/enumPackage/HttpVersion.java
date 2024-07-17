package webserver.enumPackage;

/**
 * Http 버전에 대한 정보를 담은 이넘타입
 */
public enum HttpVersion {
    HTTP_1_1("HTTP/1.1");

    private final String version;

    HttpVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
