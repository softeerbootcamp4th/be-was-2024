package webserver.enumPackage;

/**
 * Http 응답 형식 대한 정보를 담은 이넘타입
 */
public enum HtmlTemplate {
    USER_LIST("<html><head><title>User List</title></head><body>" +
            "<h1>User List</h1><ul>{{userList}}</ul></body></html>"),
    ERROR_PAGE("<html><head><title>Error</title></head><body>" +
            "<h1>Error</h1><p>{{errorMessage}}</p><script>" +
            "alert('{{errorMessage}}');window.location.href = '{{redirectUrl}}';</script>" +
            "</body></html>"),
    BOARD_LOGIN_HEADER("<!DOCTYPE html>\n" +
            "<html>\n" +
            "  <head>\n" +
            "    <meta charset=\"UTF-8\" />\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
            "    <link href=\"../reset.css\" rel=\"stylesheet\" />\n" +
            "    <link href=\"../global.css\" rel=\"stylesheet\" />\n" +
            "    <link href=\"../main.css\" rel=\"stylesheet\" />\n" +
            "  </head>\n" +
            "  <body>\n" +
            "    <div class=\"container\">\n" +
            "      <header class=\"header\">\n" +
            "        <a href=\"/main\"><img src=\"../img/signiture.svg\" /></a>\n" +
            "        <ul class=\"header__menu\">\n" +
            "          <li class=\"header__menu__item\">\n" +
            "            <span class=\"username\">{{username}}</span>\n" +
            "          </li>\n" +
            "          <li class=\"header__menu__item\">\n" +
            "            <a class=\"btn btn_contained btn_size_s\" href=\"/user/list\">유저리스트</a>\n" +
            "          </li>\n" +
            "          <li class=\"header__menu__item\">\n" +
            "            <a class=\"btn btn_contained btn_size_s\" href=\"/write\">글쓰기</a>\n" +
            "          </li>\n" +
            "          <li class=\"header__menu__item\">\n" +
            "            <form action=\"/logout\" method=\"post\" style=\"display: inline;\">\n" +
            "              <button type=\"submit\" class=\"btn btn_ghost btn_size_s\">로그아웃</button>\n" +
            "            </form>\n" +
            "          </li>\n" +
            "        </ul>\n" +
            "      </header>\n" +
            "      <div class=\"wrapper\">\n"),
    BOARD_NOT_LOGIN_HEADER("<!DOCTYPE html>\n" +
            "<html>\n" +
            "  <head>\n" +
            "    <meta charset=\"UTF-8\" />\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
            "    <link href=\"../reset.css\" rel=\"stylesheet\" />\n" +
            "    <link href=\"../global.css\" rel=\"stylesheet\" />\n" +
            "    <link href=\"../main.css\" rel=\"stylesheet\" />\n" +
            "  </head>\n" +
            "  <body>\n" +
            "    <div class=\"container\">\n" +
            "      <header class=\"header\">\n" +
            "        <a href=\"/\">\n" +
            "          <img src=\"../img/signiture.svg\" />\n" +
            "        </a>\n" +
            "        <ul class=\"header__menu\">\n" +
            "          <li class=\"header__menu__item\">\n" +
            "            <a class=\"btn btn_contained btn_size_s\" href=\"/user/list\">유저리스트</a>\n" +
            "          </li>\n" +
            "          <li class=\"header__menu__item\">\n" +
            "            <a class=\"btn btn_contained btn_size_s\" href=\"/login\">로그인</a>\n" +
            "          </li>\n" +
            "          <li class=\"header__menu__item\">\n" +
            "            <a class=\"btn btn_ghost btn_size_s\" href=\"/registration\">\n" +
            "              회원 가입\n" +
            "            </a>\n" +
            "          </li>\n" +
            "        </ul>\n" +
            "      </header>\n" +
            "      <div class=\"wrapper\">\n"),
    BOARD_BODY("<div class=\"post\">\n" +
            "          <div class=\"post__account\">\n" +
            "            <img class=\"post__account__img\" />\n" +
            "            <p class=\"post__account__nickname\">{{userId}}</p>\n" +
            "          </div>\n" +
            "          <img class=\"post__img\" src=\"{{imagePath}}\" />\n" +
            "          <div class=\"post__menu\">\n" +
            "            <ul class=\"post__menu__personal\">\n" +
            "              <li>\n" +
            "                <button class=\"post__menu__btn\">\n" +
            "                  <img src=\"../img/like.svg\" />\n" +
            "                </button>\n" +
            "              </li>\n" +
            "              <li>\n" +
            "                <button class=\"post__menu__btn\">\n" +
            "                  <img src=\"../img/sendLink.svg\" />\n" +
            "                </button>\n" +
            "              </li>\n" +
            "            </ul>\n" +
            "            <button class=\"post__menu__btn\">\n" +
            "              <img src=\"../img/bookMark.svg\" />\n" +
            "            </button>\n" +
            "          </div>\n" +
            "          <p class=\"post__article\">\n" +
            "            {{content}}\n" +
            "          </p>\n" +
            "        </div>\n"),
    BOARD_FINAL("      </div>\n" +
            "    </div>\n" +
            "  </body>\n" +
            "</html>");


    private final String template;

    HtmlTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}