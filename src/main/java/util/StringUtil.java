package util;

public final class StringUtil {
    public static final String SPACES = "\\s+";
    public static final String COLON = ":";
    public static final String SEMICOLON = ";";
    public static final String BLANK = " ";
    public static final String DOT = ".";
    public static final String QUESTION_MARK = "?";
    public static final String AMPERSAND = "&";
    public static final String EQUALS = "=";
    public static final String LF = "\n";
    public static final String CRLF = "\r\n";

    public static final String ROOT_PATH = "/";
    public static final String DEFAULT_VIEW = "/index.html";

    public static final String SUPPORTED_HTTP_VERSION = "HTTP/1.1";

    public static class Header {
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String CONTENT_LENGTH = "Content-Length";
        public static final String LOCATION = "Location";
        public static final String SET_COOKIE = "Set-Cookie";
    }

    public static class Method {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
        public static final String DELETE = "DELETE";
    }

}
