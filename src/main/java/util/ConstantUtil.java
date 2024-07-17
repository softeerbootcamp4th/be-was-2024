package util;

/**
 * 상수를 보관하는 클래스
 */
public class ConstantUtil {

    private ConstantUtil() {
    }

    public static final String SPACE = " ";
    public static final String SPACES = "\\s+";
    public static final String COLON = ":";
    public static final String COLON_WITH_SPACE = ": ";
    public static final String SEMICOLON_WITH_SPACES = ";\\s+";
    public static final String CRLF = "\r\n";
    public static final String AND = "&";
    public static final String EQUAL = "=";
    public static final String DOT = ".";
    public static final String REGDOT = "\\.";
    public static final String QUESTION_MARK = "\\?";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public static final String DYNAMIC = "dynamic";
    public static final String FAULT = "fault";
    public static final String STATIC = "static";
    public static final String COOKIE = "cookie";
    public static final String SESSION_ID = "sid";
    public static final String DOT_HTML = ".html";

    public static final String STATIC_DIR = "static.dir";
    public static final String TEMPLATES_DIR = "templates.dir";
    public static final String PROPERTIES = "project.properties";

    public static final String USER_ID = "userId";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String ARTICLE_ID = "articleId";
    public static final String TITLE = "title";
    public static final String CONTENT = "contents";
    public static final String AUTHOR_NAME = "authorName";
    public static final String IMAGE_PATH = "imagePath";
    public static final String IMAGE = "image";
    
    public static final String INVALID_SIGNUP = "Invalid Signup";
    public static final String INVALID_PATH = "Invalid Path: ";
    public static final String INVALID_HEADER = "Invalid Header: ";
    public static final String INVALID_BODY = "Invalid Body";

    public static final String USER_NOT_FOUND = "User Not Found";

    public static final String CONTENT_TYPE = "content-type";
    public static final String CONTENT_LENGTH = "content-length";
    public static final String FORM_DATA = "multipart/form-data";
    public static final String LOCATION = "Location";
    public static final String SET_COOKIE = "Set-Cookie";
}
