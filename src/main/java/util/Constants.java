package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 프로젝트 내에서 사용될 상수들을 선언한 클래스입니다.
 */
public class Constants {
    private static final Logger logger = LoggerFactory.getLogger(Constants.class);

    //Response
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String TEXT_HTML = "text/html;charset=UTF-8";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String LOCATION = "Location";
    public static final String SET_COOKIE = "Set-Cookie";

    //Request
    public static final String COOKIE = "cookie";
    public static final String SID = "sid";

    // reg
    public static final String REG_DOT = "\\.";
    public static final String REG_AMP = "&";
    public static final String REG_EQ = "=";
    public static final String REG_SPC = "\\s+";
    public static final String REG_CLN = ":";
    public static final String REG_SMCLN = ";";

    // dynamic
    public static final String USER_NAME = "userName";
    public static final String USER_LIST = "userList";

    // path
    public static String STATIC_PATH;
    public static final String PATH_HOST = "http://localhost:8080";
    public static final String PATH_ROOT = "/";
    public static final String PATH_USER = "/user";
    public static final String PATH_CREATE = "/create";
    public static final String PATH_LIST = "/list";
    public static final String PATH_LOGIN = "/login";
    public static final String PATH_LOGOUT = "/logout";
    public static final String PATH_REGISTRATION = "/registration";
    public static final String PATH_ARTICLE = "/article";
    public static final String PATH_COMMENT = "/comment";
    public static final String PATH_ERROR = "/error";

    // static file
    public static final String FILE_INDEX = "/index.html";
    public static final String FILE_USER_LIST = "/userList.html";
    public static final String FILE_NOT_FOUND = "/NOT_FOUND.html";

    //html
    public static final String TABLE_ROW_START = "<tr>";
    public static final String TABLE_ROW_END = "</tr>";
    public static final String TABLE_DATA_START = "<td>";
    public static final String TABLE_DATA_END = "</td>";

    //database
    public static String JDBC_URL;
    public static String H2_USERNAME;
    public static String H2_PASSWORD;


    private static final Properties properties = new Properties();

    /**
     * Properties 클래스를 이용해 정적 파일 경로, 데이터베이스 경로, 데이터베이스 사용자 이름, 데이터베이스 비밀번호를 로딩합니다.
     */
    static {
        try (InputStream input = Utils.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("Unable to find config.properties");
            }
            properties.load(input);
            STATIC_PATH =  properties.getProperty("staticPath");
            JDBC_URL = properties.getProperty("jdbc_url");

            H2_USERNAME = properties.getProperty("database_username");
            H2_PASSWORD = properties.getProperty("database_password");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
