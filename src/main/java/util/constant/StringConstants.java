package util.constant;

import logic.Logics;

public class StringConstants {
    public static final String SPACE = " ";
    public static final String CHARSET = "UTF-8";
    public static final String EMPTY_SPACE = "";
    public static final String COLON = ":";
    public static final String QUESTION_MARK = "?";
    public static final String AMPERSAND = "&";
    public static final String EQUAL = "=";
    public static final String CRLF = "\r\n";
    public static final String PROTOCOL_VERSION = "HTTP/1.1";
    public static final String DOT = ".";
    public static final String SEMICOLON = ";";
    public static final String UTF_8 = "UTF-8";


    //Header
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_CHARSET_UTF_8 = "charset=utf-8";

    public static final String RESOURCE_PATH = "src/main/resources/static";

    //path
    public static final String PATH_CREATE = "/create";
    public static final String PATH_LOGIN = "/login";
    public static final String PATH_LOGOUT = "/logout";
    public static final String USER_LIST = "/user/list";

    public static final Long EXPIRE_TIME = 1000000L;

    //html
    public static final String DYNAMIC_CONTENT_IS_LOGIN = "<!-- DYNAMIC_CONTENT_IS_LOGIN -->";
    public static final String DYNAMIC_CONTENT_IS_NOT_LOGIN = "<!-- DYNAMIC_CONTENT_IS_NOT_LOGIN -->";

    public static final String DYNAMIC_CONTENT_IS_LOGIN_CONTENT = """
            <form class="form" action="/logout" method="post">
                        <li class="header__menu__item">
                          <input
                                  type="submit"
                                  id="login-btn"
                                  class="btn btn_contained btn_size_s"
                                  value="로그아웃!"
                          />
                        </li>
                      </form>
                      <li class="header__menu__item">
                          <span>안녕하세요 ${님}</span>
                      </li>
            """;

    public static final String DYNAMIC_CONTENT_IS_NOT_LOGIN_CONTENT = """
            <li class="header__menu__item">
                             <a class="btn btn_contained btn_size_s" href="/login.html">로그인</a>
                           </li>
                           <li class="header__menu__item">
                             <a class="btn btn_ghost btn_size_s" href="/register.html">
                               회원 가입
                             </a>
            </li>
            """;
    public static final String makeDynamicContentIsLoginContentWithName(String userName){
        return  """
            <form class="form" action="/logout" method="post">
                        <li class="header__menu__item">
                          <input
                                  type="submit"
                                  id="login-btn"
                                  class="btn btn_contained btn_size_s"
                                  value="로그아웃!"
                          />
                        </li>
                      </form>
                      <li class="header__menu__item">
                          <span>안녕하세요!  """ + " "+userName+
                """
                      님</span>
                      
                      </li>
                """;
    }
}
