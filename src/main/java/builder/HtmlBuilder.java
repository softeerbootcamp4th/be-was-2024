package builder;

import utils.ResourceUtil;

import java.io.IOException;

public class HtmlBuilder {
    public String generateHtml(boolean isLoggedIn, String username) throws IOException {
        String templateFilePath = "/index.html"; // 템플릿 파일 경로
        ResourceUtil resourceUtil = new ResourceUtil();
        String template = new String(resourceUtil.getByteArray(templateFilePath));

        String loginButtonHtml, registrationButtonText, registrationButtonHref, userNameHtml, postButtonHtml;

        if (isLoggedIn) {
            loginButtonHtml = ""; // 로그인된 상태에서는 로그인 버튼을 숨김
            registrationButtonText = "로그아웃";
            registrationButtonHref = "/logout";
            userNameHtml = "<p class=\"user-name\">" + username + "</p>";
            postButtonHtml = "<li class=\"nav__menu__item\"><a class=\"btn btn_ghost btn_size_m\" href=\"/article\">글쓰기</a></li>";
        } else {
            loginButtonHtml = "<li class=\"header__menu__item\"><a class=\"btn btn_contained btn_size_s\" href=\"/login\">로그인</a></li>";
            registrationButtonText = "회원가입";
            registrationButtonHref = "/registration";
            userNameHtml = "";
            postButtonHtml = "";
        }

        // 문자열 대체
        template = template.replace("{username_placeholder}", userNameHtml)
                .replace("{login_button_placeholder}", loginButtonHtml)
                .replace("{registration_button_text}", registrationButtonText)
                .replace("{registration_button_href}", registrationButtonHref)
                .replace("{post_button_placeholder}", postButtonHtml);

        return template;
    }
}
