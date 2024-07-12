package web;

import common.ResponseUtils;
import facade.SessionFacade;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DynamicHtmlGenerator {

    public static void responseDynamicStringHtml(HttpRequest request, OutputStream dos, String contentType) throws IOException {
        String htmlTemplate = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <link href=\"./reset.css\" rel=\"stylesheet\" />\n" +
                "    <link href=\"./global.css\" rel=\"stylesheet\" />\n" +
                "    <link href=\"./main.css\" rel=\"stylesheet\" />\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class=\"container\">\n" +
                "      <header class=\"header\">\n" +
                "        <a href=\"/\"><img src=\"./img/signiture.svg\" /></a>\n" +
                "        <ul class=\"header__menu\">\n" +
                "          <!-- LOGIN_PLACEHOLDER -->\n" +
                "        </ul>\n" +
                "      </header>\n" +
                "      <div class=\"wrapper\">\n" +
                "        <div class=\"post\">\n" +
                "          <div class=\"post__account\">\n" +
                "            <img class=\"post__account__img\" />\n" +
                "            <p class=\"post__account__nickname\">account</p>\n" +
                "          </div>\n" +
                "          <img class=\"post__img\" />\n" +
                "          <div class=\"post__menu\">\n" +
                "            <ul class=\"post__menu__personal\">\n" +
                "              <li>\n" +
                "                <button class=\"post__menu__btn\">\n" +
                "                  <img src=\"./img/like.svg\" />\n" +
                "                </button>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <button class=\"post__menu__btn\">\n" +
                "                  <img src=\"./img/sendLink.svg\" />\n" +
                "                </button>\n" +
                "              </li>\n" +
                "            </ul>\n" +
                "            <button class=\"post__menu__btn\">\n" +
                "              <img src=\"./img/bookMark.svg\" />\n" +
                "            </button>\n" +
                "          </div>\n" +
                "          <p class=\"post__article\">\n" +
                "            우리는 시스템 아키텍처에 대한 일관성 있는 접근이 필요하며, 필요한\n" +
                "            모든 측면은 이미 개별적으로 인식되고 있다고 생각합니다. 즉, 응답이\n" +
                "            잘 되고, 탄력적이며 유연하고 메시지 기반으로 동작하는 시스템 입니다.\n" +
                "            우리는 이것을 리액티브 시스템(Reactive Systems)라고 부릅니다.\n" +
                "            리액티브 시스템으로 구축된 시스템은 보다 유연하고, 느슨한 결합을\n" +
                "            갖고, 확장성 이 있습니다. 이로 인해 개발이 더 쉬워지고 변경 사항을\n" +
                "            적용하기 쉬워집니다. 이 시스템은 장애 에 대해 더 강한 내성을 지니며,\n" +
                "            비록 장애가 발생 하더라도, 재난이 일어나기 보다는 간결한 방식으로\n" +
                "            해결합니다. 리액티브 시스템은 높은 응답성을 가지며 사용자 에게\n" +
                "            효과적인 상호적 피드백을 제공합니다.\n" +
                "          </p>\n" +
                "        </div>\n" +
                "        <ul class=\"comment\">\n" +
                "          <li class=\"comment__item\">\n" +
                "            <div class=\"comment__item__user\">\n" +
                "              <img class=\"comment__item__user__img\" />\n" +
                "              <p class=\"comment__item__user__nickname\">account</p>\n" +
                "            </div>\n" +
                "            <p class=\"comment__item__article\">\n" +
                "              군인 또는 군무원이 아닌 국민은 대한민국의 영역안에서는 중대한\n" +
                "              군사상 기밀·초병·초소·유독음식물공급·포로·군용물에 관한 죄중\n" +
                "              법률이 정한 경우와 비상계엄이 선포된 경우를 제외하고는 군사법원의\n" +
                "              재판을 받지 아니한다.\n" +
                "            </p>\n" +
                "          </li>\n" +
                "          <li class=\"comment__item\">\n" +
                "            <div class=\"comment__item__user\">\n" +
                "              <img class=\"comment__item__user__img\" />\n" +
                "              <p class=\"comment__item__user__nickname\">account</p>\n" +
                "            </div>\n" +
                "            <p class=\"comment__item__article\">\n" +
                "              대통령의 임기연장 또는 중임변경을 위한 헌법개정은 그 헌법개정 제안\n" +
                "              당시의 대통령에 대하여는 효력이 없다. 민주평화통일자문회의의\n" +
                "              조직·직무범위 기타 필요한 사항은 법률로 정한다.\n" +
                "            </p>\n" +
                "          </li>\n" +
                "          <li class=\"comment__item\">\n" +
                "            <div class=\"comment__item__user\">\n" +
                "              <img class=\"comment__item__user__img\" />\n" +
                "              <p class=\"comment__item__user__nickname\">account</p>\n" +
                "            </div>\n" +
                "            <p class=\"comment__item__article\">\n" +
                "              민주평화통일자문회의의 조직·직무범위 기타 필요한 사항은 법률로\n" +
                "              정한다.\n" +
                "            </p>\n" +
                "          </li>\n" +
                "          <li class=\"comment__item hidden\">\n" +
                "            <div class=\"comment__item__user\">\n" +
                "              <img class=\"comment__item__user__img\" />\n" +
                "              <p class=\"comment__item__user__nickname\">account</p>\n" +
                "            </div>\n" +
                "            <p class=\"comment__item__article\">Comment 1</p>\n" +
                "          </li>\n" +
                "          <li class=\"comment__item hidden\">\n" +
                "            <div class=\"comment__item__user\">\n" +
                "              <img class=\"comment__item__user__img\" />\n" +
                "              <p class=\"comment__item__user__nickname\">account</p>\n" +
                "            </div>\n" +
                "            <p class=\"comment__item__article\">Comment 2</p>\n" +
                "          </li>\n" +
                "          <li class=\"comment__item hidden\">\n" +
                "            <div class=\"comment__item__user\">\n" +
                "              <img class=\"comment__item__user__img\" />\n" +
                "              <p class=\"comment__item__user__nickname\">account</p>\n" +
                "            </div>\n" +
                "            <p class=\"comment__item__article\">Comment 3</p>\n" +
                "          </li>\n" +
                "          <button id=\"show-all-btn\" class=\"btn btn_ghost btn_size_m\">\n" +
                "            모든 댓글 보기(3개)\n" +
                "          </button>\n" +
                "        </ul>\n" +
                "        <nav class=\"nav\">\n" +
                "          <ul class=\"nav__menu\">\n" +
                "            <li class=\"nav__menu__item\">\n" +
                "              <a class=\"nav__menu__item__btn\" href=\"\">\n" +
                "                <img\n" +
                "                  class=\"nav__menu__item__img\"\n" +
                "                  src=\"./img/ci_chevron-left.svg\"\n" +
                "                />\n" +
                "                이전 글\n" +
                "              </a>\n" +
                "            </li>\n" +
                "            <li class=\"nav__menu__item\">\n" +
                "              <a class=\"btn btn_ghost btn_size_m\">댓글 작성</a>\n" +
                "            </li>\n" +
                "            <li class=\"nav__menu__item\">\n" +
                "              <a class=\"nav__menu__item__btn\" href=\"\">\n" +
                "                다음 글\n" +
                "                <img\n" +
                "                  class=\"nav__menu__item__img\"\n" +
                "                  src=\"./img/ci_chevron-right.svg\"\n" +
                "                />\n" +
                "              </a>\n" +
                "            </li>\n" +
                "          </ul>\n" +
                "        </nav>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";

        String loginHtml;
        String userId = SessionFacade.getUserIdFromSession(request);
        if (SessionFacade.isAuthenticatedRequest(request)) {
            loginHtml = "<li class=\"header__menu__item\"><form id=\"logout\" action=\"/logout\" method=\"POST\"><button type=\"submit\">로그아웃</button></form>" +
                    "<li class=\"header__menu__item\"><p class=\"comment__item__article\"> 안녕하세요, "+userId+"님.</p></li>";
        } else {
            loginHtml = "<li class=\"header__menu__item\"><a class=\"btn btn_contained btn_size_s\" href=\"/login\">로그인</a></li>" +
                    "<li class=\"header__menu__item\"><a class=\"btn btn_ghost btn_size_s\" href=\"/registration\">회원 가입</a></li>";
        }

        String finalHtml = htmlTemplate.replace("<!-- LOGIN_PLACEHOLDER -->", loginHtml);
        byte[] body = finalHtml.getBytes();
        HttpResponse response = ResponseUtils.responseSuccessWithFile(contentType, body);
        response.writeInBytes(dos);
    }
}
